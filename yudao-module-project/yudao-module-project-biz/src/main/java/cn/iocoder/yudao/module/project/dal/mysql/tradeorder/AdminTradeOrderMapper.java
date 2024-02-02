package cn.iocoder.yudao.module.project.dal.mysql.tradeorder;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo.*;

/**
 * 交易订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminTradeOrderMapper extends BaseMapperX<AdminTradeOrderDO> {

    default PageResult<AdminTradeOrderPageDO> selectPage(TradeOrderPageReqVO reqVO) {



        return selectJoinPage(reqVO,AdminTradeOrderPageDO.class, new MPJLambdaWrapper<AdminTradeOrderDO>()
                .selectAll(AdminTradeOrderDO.class)
                .select(MemberDO::getAddress)
                .join("INNER JOIN", MemberDO.class,
                        on -> on.eq(AdminTradeOrderDO::getUserId, MemberDO::getId)
                                .eq(null!=reqVO.getAddress(),MemberDO::getAddress,reqVO.getAddress())
                                .eq(null!=reqVO.getNo(),AdminTradeOrderDO::getNo,reqVO.getNo())
                                .eq(null!=reqVO.getType(),AdminTradeOrderDO::getType,reqVO.getType())
                                .eq(null!=reqVO.getUserId(),AdminTradeOrderDO::getUserId,reqVO.getUserId())
                                .eq(null!=reqVO.getStatus(),AdminTradeOrderDO::getStatus,reqVO.getStatus())
                                .eq(null!=reqVO.getPayStatus(),AdminTradeOrderDO::getPayStatus,reqVO.getPayStatus())
                                .between(null!=reqVO.getCreateTime() && reqVO.getCreateTime().length==2
                                        ,AdminTradeOrderDO::getCreateTime,null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[0]:null,
                                        null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[1]:null)
                )
                .orderByDesc(AdminTradeOrderDO::getId));
    }

}