package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderItemPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singleton;

/**
 * 交易订单【读】 Service 接口
 */
public interface TradeOrderQueryService {

    /**
     * 获得指定编号的交易订单
     *
     * @param id 交易订单编号
     * @return 交易订单
     */
    TradeOrderDO getOrder(Long id);

    /**
     * 获得指定用户，指定的交易订单
     *
     * @param userId 用户编号
     * @param id     交易订单编号
     * @return 交易订单
     */
    TradeOrderDO getOrder(Long userId, Long id);

    /**
     * 获得订单列表
     *
     * @param ids 订单编号数组
     * @return 订单列表
     */
    List<TradeOrderDO> getOrderList(Collection<Long> ids);

    /**
     * 【会员】获得交易订单分页
     *
     * @param userId 用户编号
     * @param reqVO  分页请求
     * @return 交易订单
     */
    PageResult<TradeOrderDO> getOrderPage(Long userId, AppTradeOrderPageReqVO reqVO);

    /**
     * 【会员】获得交易订单数量
     *
     * @param userId       用户编号
     * @param status       订单状态。如果为空，则不进行筛选
     * @param commonStatus 评价状态。如果为空，则不进行筛选
     * @return 订单数量
     */
    Long getOrderCount(Long userId, Integer status, Boolean commonStatus);

    PageResult<TradeOrderItemDO> getOrderItemPage(AppTradeOrderItemPageReqVO reqVO, Long userId);

    /**
     * 根据交易订单编号，查询交易订单项
     *
     * @param orderId 交易订单编号
     * @return 交易订单项数组
     */
    default List<TradeOrderItemDO> getOrderItemListByOrderId(Long orderId) {
        return getOrderItemListByOrderId(singleton(orderId));
    }

    /**
     * 根据交易订单编号数组，查询交易订单项
     *
     * @param orderIds 交易订单编号数组
     * @return 交易订单项数组
     */
    List<TradeOrderItemDO> getOrderItemListByOrderId(Collection<Long> orderIds);

}
