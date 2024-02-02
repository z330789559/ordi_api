package cn.iocoder.yudao.module.project.service.tradeorder;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderItemDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;

/**
 * 交易订单 Service 接口
 *
 * @author 芋道源码
 */
public interface TradeOrderService {

    /**
     * 创建交易订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTradeOrder(@Valid TradeOrderSaveReqVO createReqVO);

    /**
     * 更新交易订单
     *
     * @param updateReqVO 更新信息
     */
    void updateTradeOrder(@Valid TradeOrderSaveReqVO updateReqVO);

    /**
     * 删除交易订单
     *
     * @param id 编号
     */
    void deleteTradeOrder(Long id);

    /**
     * 获得交易订单
     *
     * @param id 编号
     * @return 交易订单
     */
    AdminTradeOrderDO getTradeOrder(Long id);

    /**
     * 获得交易订单分页
     *
     * @param pageReqVO 分页查询
     * @return 交易订单分页
     */
    PageResult<AdminTradeOrderPageDO> getTradeOrderPage(TradeOrderPageReqVO pageReqVO);

    // ==================== 子表（交易订单明细） ====================

    /**
     * 获得交易订单明细列表
     *
     * @param orderId 订单id
     * @return 交易订单明细列表
     */
    List<AdminTradeOrderItemDO> getTradeOrderItemListByOrderId(Long orderId);

}