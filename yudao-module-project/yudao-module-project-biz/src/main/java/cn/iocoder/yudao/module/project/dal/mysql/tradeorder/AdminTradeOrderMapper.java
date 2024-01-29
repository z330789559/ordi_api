package cn.iocoder.yudao.module.project.dal.mysql.tradeorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo.*;

/**
 * 交易订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminTradeOrderMapper extends BaseMapperX<AdminTradeOrderDO> {

    default PageResult<AdminTradeOrderDO> selectPage(TradeOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminTradeOrderDO>()
                .eqIfPresent(AdminTradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(AdminTradeOrderDO::getType, reqVO.getType())
                .eqIfPresent(AdminTradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AdminTradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AdminTradeOrderDO::getPayStatus, reqVO.getPayStatus())
                .betweenIfPresent(AdminTradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AdminTradeOrderDO::getId));
    }

}