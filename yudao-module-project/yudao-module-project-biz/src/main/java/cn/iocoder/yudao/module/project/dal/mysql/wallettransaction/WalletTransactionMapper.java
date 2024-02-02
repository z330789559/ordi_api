package cn.iocoder.yudao.module.project.dal.mysql.wallettransaction;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionAddressDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo.*;

/**
 * 钱包流水 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WalletTransactionMapper extends BaseMapperX<WalletTransactionDO> {

//          return selectJoinPage(reqVO, AdminTradeOrderPageDO .class, new MPJLambdaWrapper<AdminTradeOrderDO>()
//                .selectAll(AdminTradeOrderDO.class)
//                .select(MemberDO::getAddress)
//                .join("INNER JOIN", MemberDO.class,
//            on -> on.eq(AdminTradeOrderDO::getUserId, MemberDO::getId)
//            .eq(null!=reqVO.getAddress(),MemberDO::getAddress,reqVO.getAddress())
//            .eq(null!=reqVO.getNo(),AdminTradeOrderDO::getNo,reqVO.getNo())
//            .eq(null!=reqVO.getType(),AdminTradeOrderDO::getType,reqVO.getType())
//            .eq(null!=reqVO.getUserId(),AdminTradeOrderDO::getUserId,reqVO.getUserId())
//            .eq(null!=reqVO.getStatus(),AdminTradeOrderDO::getStatus,reqVO.getStatus())
//            .eq(null!=reqVO.getPayStatus(),AdminTradeOrderDO::getPayStatus,reqVO.getPayStatus())
//            .between(null!=reqVO.getCreateTime() && reqVO.getCreateTime().length==2
//            ,AdminTradeOrderDO::getCreateTime,null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[0]:null,
//            null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[1]:null)
//            )


    default PageResult<WalletTransactionAddressDO> selectPage(WalletTransactionPageReqVO reqVO) {
        return selectJoinPage(reqVO,WalletTransactionAddressDO.class, new MPJLambdaWrapper<WalletTransactionDO>()
                .selectAll(WalletTransactionDO.class)
                .select(MemberDO::getAddress)
                .join("INNER JOIN", MemberDO.class,
                        on -> on.eq(WalletTransactionDO::getWalletId, MemberDO::getId)
                                .eq(null!=reqVO.getAddress(),MemberDO::getAddress,reqVO.getAddress())
                                .eq(null!=reqVO.getNo(),WalletTransactionDO::getNo,reqVO.getNo())
                                .eq(null!=reqVO.getWalletId(),WalletTransactionDO::getWalletId,reqVO.getWalletId())
                                .eq(null!=reqVO.getBizType(),WalletTransactionDO::getBizType,reqVO.getBizType())
                                .eq(null!=reqVO.getBizId(),WalletTransactionDO::getBizId,reqVO.getBizId())
                                .between(null!=reqVO.getCreateTime() && reqVO.getCreateTime().length==2
                                        ,WalletTransactionDO::getCreateTime,null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[0]:null,
                                        null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[1]:null)
                )
                .orderByDesc(WalletTransactionDO::getId));
    }

}