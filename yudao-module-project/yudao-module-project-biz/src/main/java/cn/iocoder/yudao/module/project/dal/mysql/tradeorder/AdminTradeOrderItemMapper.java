package cn.iocoder.yudao.module.project.dal.mysql.tradeorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易订单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminTradeOrderItemMapper extends BaseMapperX<AdminTradeOrderItemDO> {

    default List<AdminTradeOrderItemDO> selectListByOrderId(Long orderId) {
        return selectList(AdminTradeOrderItemDO::getOrderId, orderId);
    }

    default int deleteByOrderId(Long orderId) {
        return delete(AdminTradeOrderItemDO::getOrderId, orderId);
    }

}