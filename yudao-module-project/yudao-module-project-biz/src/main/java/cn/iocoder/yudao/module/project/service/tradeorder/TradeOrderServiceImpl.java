package cn.iocoder.yudao.module.project.service.tradeorder;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderItemDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;
import cn.iocoder.yudao.module.project.dal.mysql.tradeorder.AdminTradeOrderItemMapper;
import cn.iocoder.yudao.module.project.dal.mysql.tradeorder.AdminTradeOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 交易订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TradeOrderServiceImpl implements TradeOrderService {

    @Resource
    private AdminTradeOrderMapper adminTradeOrderMapper;
    @Resource
    private AdminTradeOrderItemMapper adminTradeOrderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTradeOrder(TradeOrderSaveReqVO createReqVO) {
        // 插入
        AdminTradeOrderDO tradeOrder = BeanUtils.toBean(createReqVO, AdminTradeOrderDO.class);
        adminTradeOrderMapper.insert(tradeOrder);

        // 插入子表
        createTradeOrderItemList(tradeOrder.getId(), createReqVO.getTradeOrderItems());
        // 返回
        return tradeOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTradeOrder(TradeOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateTradeOrderExists(updateReqVO.getId());
        // 更新
        AdminTradeOrderDO updateObj = BeanUtils.toBean(updateReqVO, AdminTradeOrderDO.class);
        adminTradeOrderMapper.updateById(updateObj);

        // 更新子表
        updateTradeOrderItemList(updateReqVO.getId(), updateReqVO.getTradeOrderItems());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTradeOrder(Long id) {
        // 校验存在
        validateTradeOrderExists(id);
        // 删除
        adminTradeOrderMapper.deleteById(id);

        // 删除子表
        deleteTradeOrderItemByOrderId(id);
    }

    private void validateTradeOrderExists(Long id) {
        if (adminTradeOrderMapper.selectById(id) == null) {
            throw exception(TRADE_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public AdminTradeOrderDO getTradeOrder(Long id) {
        return adminTradeOrderMapper.selectById(id);
    }

    @Override
    public PageResult<AdminTradeOrderPageDO> getTradeOrderPage(TradeOrderPageReqVO pageReqVO) {
        return adminTradeOrderMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（交易订单明细） ====================

    @Override
    public List<AdminTradeOrderItemDO> getTradeOrderItemListByOrderId(Long orderId) {
        return adminTradeOrderItemMapper.selectListByOrderId(orderId);
    }

    private void createTradeOrderItemList(Long orderId, List<AdminTradeOrderItemDO> list) {
        list.forEach(o -> o.setOrderId(orderId));
        adminTradeOrderItemMapper.insertBatch(list);
    }

    private void updateTradeOrderItemList(Long orderId, List<AdminTradeOrderItemDO> list) {
        deleteTradeOrderItemByOrderId(orderId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createTradeOrderItemList(orderId, list);
    }

    private void deleteTradeOrderItemByOrderId(Long orderId) {
        adminTradeOrderItemMapper.deleteByOrderId(orderId);
    }

}