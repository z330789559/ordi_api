package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;

/**
 * 交易订单【写】Service 接口
 */
public interface TradeOrderUpdateService {

    TradeOrderDO createOrder(Long userId, String userIp, AppTradeOrderCreateReqVO createReqVO, Integer terminal);

}
