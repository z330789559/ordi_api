package cn.iocoder.yudao.module.trade.dal.mysql.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderItemPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface TradeOrderItemMapper extends BaseMapperX<TradeOrderItemDO> {

    default List<TradeOrderItemDO> selectListByOrderId(Long orderId) {
        return selectList(TradeOrderItemDO::getOrderId, orderId);
    }

    default List<TradeOrderItemDO> selectListByOrderId(Collection<Long> orderIds) {
        return selectList(TradeOrderItemDO::getOrderId, orderIds);
    }


    default PageResult<TradeOrderItemDO> selectPage(AppTradeOrderItemPageReqVO reqVO, Long userId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TradeOrderItemDO>()
                .eq(TradeOrderItemDO::getUserId, userId)
                .eqIfPresent(TradeOrderItemDO::getStatus, reqVO.getStatus())
                .orderByDesc(TradeOrderItemDO::getId)); // TODO 芋艿：未来不同的 status，不同的排序
    }

    default TradeOrderItemDO selectByIdAndUserId(Long orderItemId, Long loginUserId) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderItemDO>()
                .eq(TradeOrderItemDO::getId, orderItemId)
                .eq(TradeOrderItemDO::getUserId, loginUserId));
    }

    default int selectProductSumByOrderId(@Param("orderIds") Set<Long> orderIds) {
        // SQL sum 查询
        List<Map<String, Object>> result = selectMaps(new QueryWrapper<TradeOrderItemDO>()
                .select("SUM(count) AS sumCount")
                .in("order_id", orderIds)); // 只计算选中的
        // 获得数量
        return CollUtil.getFirst(result) != null ? MapUtil.getInt(result.get(0), "sumCount") : 0;
    }

}
