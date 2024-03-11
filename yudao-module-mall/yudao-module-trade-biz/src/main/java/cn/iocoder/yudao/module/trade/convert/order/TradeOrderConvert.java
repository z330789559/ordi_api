package cn.iocoder.yudao.module.trade.convert.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.api.item.dto.ItemUpdateStatusReqDTO;
import cn.iocoder.yudao.module.product.api.item.dto.ItemUpdateStockReqDTO;
import cn.iocoder.yudao.module.trade.api.order.dto.TradeOrderRespDTO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.*;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TradeOrderConvert {

    TradeOrderConvert INSTANCE = Mappers.getMapper(TradeOrderConvert.class);

    TradeOrderRespDTO convert(TradeOrderDO orderDO);

    default TradeOrderItemDO convertItem(TradeOrderDO tradeOrderDO, ItemRespDTO itemRespDTO, Integer status) {
        return new TradeOrderItemDO().setOrderId(tradeOrderDO.getId())
                .setType(tradeOrderDO.getType()).setUserId(tradeOrderDO.getUserId())
                .setItemId(itemRespDTO.getId()).setItemName(itemRespDTO.getName())
                .setPrice(itemRespDTO.getPrice())
                .setPayPrice(tradeOrderDO.getTotalPrice()).setStatus(status) // 购买
                .setRate(itemRespDTO.getUsdPrice())
                .setQuantity(itemRespDTO.getStock());
    }

    PageResult<AppTradeOrderItemRespVO> convert05(PageResult<TradeOrderItemDO> pageResult);

    AppTradeOrderDetailRespVO convert3(TradeOrderDO order, List<TradeOrderItemDO> items);

    AppTradeOrderItemRespVO convert03(TradeOrderItemDO bean);

    @Named("convertList04")
    List<TradeOrderRespDTO> convertList04(List<TradeOrderDO> list);

    default ItemUpdateStockReqDTO convert(List<TradeOrderItemDO> list) {
        List<ItemUpdateStockReqDTO.Item> items = CollectionUtils.convertList(list, item ->
                new ItemUpdateStockReqDTO.Item().setId(item.getItemId()).setIncrCount(item.getQuantity()));
        return new ItemUpdateStockReqDTO(items);
    }

    default ItemUpdateStockReqDTO convertNegative(List<TradeOrderItemDO> list) {
        List<ItemUpdateStockReqDTO.Item> items = CollectionUtils.convertList(list, item ->
                new ItemUpdateStockReqDTO.Item().setId(item.getItemId()).setIncrCount(-item.getQuantity()));
        return new ItemUpdateStockReqDTO(items);
    }

    default ItemUpdateStatusReqDTO convertStatus(List<TradeOrderItemDO> list, Integer status) {
        List<ItemUpdateStatusReqDTO.Item> items = CollectionUtils.convertList(list, item ->
                new ItemUpdateStatusReqDTO.Item().setId(item.getItemId()));
        return new ItemUpdateStatusReqDTO(items, status);
    }
}
