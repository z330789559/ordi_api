package cn.iocoder.yudao.module.trade.service.order;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.product.api.item.ItemApi;
import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.api.token.TokenApi;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.dal.mysql.order.TradeOrderItemMapper;
import cn.iocoder.yudao.module.trade.dal.mysql.order.TradeOrderMapper;
import cn.iocoder.yudao.module.trade.dal.redis.no.TradeNoRedisDAO;
import cn.iocoder.yudao.module.trade.enums.order.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.*;

/**
 * 交易订单【写】Service 实现类
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
@Service
@Slf4j
public class TradeOrderUpdateServiceImpl implements TradeOrderUpdateService {

    @Resource
    private TradeOrderMapper tradeOrderMapper;
    @Resource
    private TradeOrderItemMapper tradeOrderItemMapper;
    @Resource
    private TradeNoRedisDAO tradeNoRedisDAO;
    @Resource
    private PayWalletApi payWalletApi;
    @Resource
    private TokenApi tokenApi;
    @Resource
    private ItemApi itemApi;

    @Override
    public TradeOrderDO createOrder(Long userId, String userIp, AppTradeOrderCreateReqVO createReqVO, Integer terminal) {
        TokenRespDTO tokenRespDTO = tokenApi.getToken(2L);

        ItemRespDTO item = itemApi.getItem(createReqVO.getItems().get(0).getItemId());
        if (item.getStock() < 1) throw exception(ITEM_STOCK_NOT_ENOUGH);
        if (!Objects.equals(item.getStatus(), ItemStatusEnum.ENABLE.getStatus())) {
            throw exception(ITEM_STATUS_ERROR);
        }
        if (item.getUserId().equals(userId)) {
            throw exception(ITEM_BELONG_YOUR_SELF);
        }
        item.setUsdPrice(tokenRespDTO.getUsdPrice());
        BigDecimal quantity = BigDecimal.valueOf(item.getStock());
        BigDecimal totalPrice = item.getPrice().multiply(quantity);

        BigDecimal gasFee = totalPrice.multiply(tokenRespDTO.getBuyGasFee());
        if (gasFee.compareTo(tokenRespDTO.getBuyGasLimit()) > 0) {
            gasFee = tokenRespDTO.getBuyGasLimit();
        }
        gasFee = gasFee.multiply(tokenRespDTO.getUsdPrice());
        BigDecimal finPrice = totalPrice.multiply(tokenRespDTO.getUsdPrice());

        PayWalletRespDTO buyerFinWalletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.FINANCE.getType());
        if (finPrice.add(gasFee).compareTo(buyerFinWalletRespDTO.getBalance()) > 0) {
            throw exception(BUYER_WALLET_BALANCE_NOT_ENOUGH);
        }
        buyerFinWalletRespDTO.setPayPrice(finPrice);
        buyerFinWalletRespDTO.setGasFee(gasFee);

        PayWalletRespDTO buyerCirWalletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.CIRCULATION.getType());

        PayWalletRespDTO selleFinWalletRestDTO = payWalletApi.getOrCreateWallet(item.getUserId(), PayWalletUserTypeEnum.FINANCE.getType());

        return submitOrder(userId, userIp, item, buyerFinWalletRespDTO, buyerCirWalletRespDTO,
                selleFinWalletRestDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public TradeOrderDO submitOrder(Long userId, String userIp, ItemRespDTO item, PayWalletRespDTO buyerFinWalletRespDTO, PayWalletRespDTO buyerCirWalletRespDTO,
                                    PayWalletRespDTO sellerFinWalletRespDTO) {
        TradeOrderDO order = new TradeOrderDO();
        order.setNo(tradeNoRedisDAO.generate(TradeNoRedisDAO.TRADE_ORDER_NO_PREFIX))
                .setPayTime(LocalDateTime.now()).setPayStatus(true)
                .setPayPrice(buyerFinWalletRespDTO.getPayPrice())
                .setTotalPrice(buyerFinWalletRespDTO.getPayPrice().add(buyerFinWalletRespDTO.getGasFee()))
                .setGasFee(buyerFinWalletRespDTO.getGasFee())
                .setUserIp(userIp).setUserId(userId).setType(0)
                .setRemark("购买订单");

        tradeOrderMapper.insert(order);

        TradeOrderItemDO orderItem = TradeOrderConvert.INSTANCE.convertItem(order, item, TradeOrderItemStatusEnum.PURCHASE.getStatus());

        itemApi.sellItem(item.getId());
        tradeOrderItemMapper.insert(orderItem);

        BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
        payWalletApi.addWalletBalance(order.getId(), PayWalletBizTypeEnum.BUY, buyerCirWalletRespDTO.getId(), quantity);
        payWalletApi.reduceWalletBalance(order.getId(), PayWalletBizTypeEnum.PAYMENT, buyerFinWalletRespDTO.getId(), order.getPayPrice());
        payWalletApi.reduceWalletBalance(order.getId(), PayWalletBizTypeEnum.PAYMENT_GAS, buyerFinWalletRespDTO.getId(), order.getGasFee());
        payWalletApi.addWalletBalance(order.getId(), PayWalletBizTypeEnum.BUY, sellerFinWalletRespDTO.getId(), order.getPayPrice());

        if (item.getOrderItemId() > 0) {
            tradeOrderItemMapper.updateById(new TradeOrderItemDO().setId(item.getOrderItemId())
                    .setStatus(TradeOrderItemStatusEnum.SOLD.getStatus()));
        }

        return order;
    }

}

