package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppShelveOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易订单【写】Service 接口
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
public interface ShelveOrderUpdateService {

    TradeOrderDO createOrder(Long userId, String userIp, AppShelveOrderCreateReqVO createReqVO, Integer terminal);

    @Transactional(rollbackFor = Exception.class)
    void closeOrder(Long userId, Long id,Integer terminal);

}
