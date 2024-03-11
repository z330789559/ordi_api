package cn.iocoder.yudao.module.project.dal.mysql.wallet;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletMemberDO;
import cn.iocoder.yudao.module.project.dal.mysql.member.MemberMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.wallet.vo.*;

/**
 * 钱包 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminWalletMapper extends BaseMapperX<WalletDO> {

//      return selectJoinPage(reqVO, AdminTradeOrderPageDO .class, new MPJLambdaWrapper<AdminTradeOrderDO>()
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
    default PageResult<WalletMemberDO> selectPage(WalletPageReqVO reqVO) {
        return selectJoinPage(reqVO, WalletMemberDO.class,new MPJLambdaWrapper<WalletDO>()
                .selectAll(WalletDO.class)
                .select(MemberDO::getAddress)
                .join("INNER JOIN", MemberDO.class,
            on -> on.eq(WalletMemberDO::getUserId, MemberDO::getId)
                    .eq(null!=reqVO.getId(),WalletDO::getId,reqVO.getId())
                    .eq(null!=reqVO.getAddress(),MemberDO::getAddress,reqVO.getAddress())
                    .eq(null!=reqVO.getUserType(),WalletDO::getUserType,reqVO.getUserType())
                    .eq(null!=reqVO.getUserId(),WalletDO::getUserId,reqVO.getUserId())
                    .eq(null!=reqVO.getTokenId(),WalletDO::getTokenId,reqVO.getTokenId())
                    .between(null!=reqVO.getCreateTime() && reqVO.getCreateTime().length==2
                            ,WalletDO::getCreateTime,null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[0]:null,
                            null!=reqVO.getCreateTime()&& reqVO.getCreateTime().length==2?reqVO.getCreateTime()[1]:null)

                )
                .orderByDesc(WalletDO::getId));
    }





}