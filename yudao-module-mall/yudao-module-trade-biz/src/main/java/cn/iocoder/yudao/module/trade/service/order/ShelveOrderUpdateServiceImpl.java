package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.product.api.item.ItemApi;
import cn.iocoder.yudao.module.product.api.item.dto.ItemCreateReqDTO;
import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.api.token.TokenApi;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppShelveOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.dal.mysql.order.ShelveOrderMapper;
import cn.iocoder.yudao.module.trade.dal.mysql.order.TradeOrderItemMapper;
import cn.iocoder.yudao.module.trade.dal.redis.no.TradeNoRedisDAO;
import cn.iocoder.yudao.module.trade.enums.OrderOperatorTerminal;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderItemStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.WALLET_BALANCE_NOT_ENOUGH;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.*;

/**
 * 交易订单【写】Service 实现类
 */
@Service
@Slf4j
public class ShelveOrderUpdateServiceImpl implements ShelveOrderUpdateService {

    @Resource
    private ShelveOrderMapper shelveOrderMapper;
    @Resource
    private TradeOrderItemMapper tradeOrderItemMapper;
    @Resource
    private TradeNoRedisDAO tradeNoRedisDAO;
    @Resource
    private ItemApi itemApi;
    @Resource
    private PayWalletApi payWalletApi;
    @Resource
    private TokenApi tokenApi;

    @Override
    public TradeOrderDO createOrder(Long userId, String userIp, AppShelveOrderCreateReqVO createReqVO, Integer terminal) {
        TokenRespDTO tokenRespDTO = tokenApi.getToken(2L);

        createReqVO.setRate(tokenRespDTO.getUsdPrice());

        BigDecimal cirPrice = BigDecimal.valueOf(createReqVO.getQuantity());
        BigDecimal sellFee = cirPrice.multiply(createReqVO.getPrice()).multiply(tokenRespDTO.getSellGasFee());
        if(sellFee.compareTo(tokenRespDTO.getSellGasLimit()) > 0) {
            sellFee = tokenRespDTO.getSellGasLimit();
        }
     // USDT转BTC
        BigDecimal sellFeeBtc = sellFee.multiply(tokenRespDTO.getUsdPrice());

        PayWalletRespDTO cirWalletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.CIRCULATION.getType());
        if (cirPrice.compareTo(cirWalletRespDTO.getBalance()) > 0) {
            throw exception(WALLET_BALANCE_NOT_ENOUGH);
        }

       BigDecimal payPrice =cirPrice.multiply(createReqVO.getPrice()).add(cirPrice.multiply(createReqVO.getPrice()).multiply(tokenRespDTO.getBuyGasFee()));
     // USDT转BTC
        BigDecimal buyPriceBtc = payPrice.multiply(tokenRespDTO.getUsdPrice());
        cirWalletRespDTO.setPayPrice(buyPriceBtc);
        PayWalletRespDTO finWalletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.FINANCE.getType());
        if (sellFeeBtc.compareTo(finWalletRespDTO.getBalance()) > 0) {
            throw exception(WALLET_BALANCE_NOT_ENOUGH);
        }
        finWalletRespDTO.setPayPrice(createReqVO.getPrice());
        finWalletRespDTO.setGasFee(sellFeeBtc);

        TradeOrderDO tradeOrderDO = submitOrder(userId, userIp, createReqVO, cirWalletRespDTO, finWalletRespDTO);

        return tradeOrderDO;
    }

    @Transactional(rollbackFor = Exception.class)
    public TradeOrderDO submitOrder(Long userId, String userIp, AppShelveOrderCreateReqVO createReqVO,
                                    PayWalletRespDTO cirWalletRespDTO, PayWalletRespDTO finWalletRespDTO) {
        // 构建商品
        ItemCreateReqDTO itemCreateReqDTO = new ItemCreateReqDTO()
                .setPrice(createReqVO.getPrice()).setQuantity(createReqVO.getQuantity())
                .setTokenId(1L).setUserId(userId);

        ItemRespDTO item = itemApi.shelveItem(itemCreateReqDTO);

        TradeOrderDO order = new TradeOrderDO();
        order.setPayTime(LocalDateTime.now()).setPayStatus(true)
                .setPayPrice(createReqVO.getPrice())
                .setTotalPrice(cirWalletRespDTO.getPayPrice())
                .setGasFee(finWalletRespDTO.getGasFee())
                .setUserIp(userIp).setUserId(userId).setType(1)
                .setRemark("上架订单");
        item.setUsdPrice(createReqVO.getRate());
        shelveOrderMapper.insert(order);

        TradeOrderItemDO orderItem = TradeOrderConvert.INSTANCE.convertItem(order, item, TradeOrderItemStatusEnum.ON_SALE.getStatus());

        tradeOrderItemMapper.insert(orderItem);

        itemApi.updateOrderItem(item.getId(), orderItem.getId());

        payWalletApi.reduceWalletBalance(order.getId(), PayWalletBizTypeEnum.SELL, cirWalletRespDTO.getId(), BigDecimal.valueOf(createReqVO.getQuantity()));
        payWalletApi.reduceWalletBalance(order.getId(), PayWalletBizTypeEnum.PAYMENT_GAS, finWalletRespDTO.getId(), finWalletRespDTO.getGasFee());

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrder(Long userId, Long billId, Integer terminal) {
        // 1. 获取我的订单
        TradeOrderItemDO item =new TradeOrderItemDO();
        if(Objects.equals(terminal, OrderOperatorTerminal.ADMIN)){
            item = tradeOrderItemMapper.selectOne(TradeOrderItemDO::getOrderId, billId);
        }else{
            item = tradeOrderItemMapper.selectById(billId);
        }
        if(item == null){
            throw exception(ORDER_ITEM_NOT_FOUND);
        }
        if (!item.getUserId().equals(userId)) {
            throw exception(ORDER_NOT_FOUND);
        }
        Long id =item.getId();
        if (!item.getStatus().equals(TradeOrderItemStatusEnum.ON_SALE.getStatus())) {
            throw exception(ORDER_STATUS_NOT_ON_SALE);
        }
        // 2. 更新我的订单状态
        tradeOrderItemMapper.updateById(new TradeOrderItemDO().setId(id)
                .setStatus(TradeOrderItemStatusEnum.OFF_SALE.getStatus()));

        //3. 更新父订单状态
        shelveOrderMapper.updateById(new TradeOrderDO().setId(item.getOrderId())
                .setStatus(TradeOrderItemStatusEnum.OFF_SALE.getStatus()));
        // 4. 更新余额
        PayWalletRespDTO cirWalletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.CIRCULATION.getType());
        payWalletApi.addWalletBalance(item.getId(), PayWalletBizTypeEnum.SHELVE_REFUND,
                cirWalletRespDTO.getId(), BigDecimal.valueOf(item.getQuantity()));

        // 4. 下架商品
        ItemRespDTO product = itemApi.getItem(item.getItemId());
        if (product == null) {
            log.error("Item-not-found {}", item.getItemId());
            throw exception(ORDER_ITEM_NOT_FOUND);
        }
        if (!product.getStatus().equals(ItemStatusEnum.ENABLE.getStatus())) {
            throw exception(ORDER_ITEM_NOT_FOUND);
        }
        itemApi.disableItem(item.getItemId());
    }

}
