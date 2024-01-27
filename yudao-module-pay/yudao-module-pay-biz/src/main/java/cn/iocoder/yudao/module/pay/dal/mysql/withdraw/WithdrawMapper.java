package cn.iocoder.yudao.module.pay.dal.mysql.withdraw;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletWithdrawD0;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithdrawMapper extends BaseMapperX<PayWalletWithdrawD0> {
    
//    default PageResult<PayWalletWithdrawD0> selectPage(BrokerageWithdrawPageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<PayWalletWithdrawD0>()
//                .eqIfPresent(PayWalletWithdrawD0::getUserId, reqVO.getUserId())
//                .eqIfPresent(PayWalletWithdrawD0::getType, reqVO.getType())
//                .likeIfPresent(PayWalletWithdrawD0::getName, reqVO.getName())
//                .eqIfPresent(PayWalletWithdrawD0::getAccountNo, reqVO.getAccountNo())
//                .likeIfPresent(PayWalletWithdrawD0::getBankName, reqVO.getBankName())
//                .eqIfPresent(PayWalletWithdrawD0::getStatus, reqVO.getStatus())
//                .betweenIfPresent(PayWalletWithdrawD0::getCreateTime, reqVO.getCreateTime())
//                .orderByAsc(PayWalletWithdrawD0::getStatus).orderByDesc(PayWalletWithdrawD0::getId));
//    }

    default int updateByIdAndStatus(Integer id, Integer status, PayWalletWithdrawD0 updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<PayWalletWithdrawD0>()
                .eq(PayWalletWithdrawD0::getId, id)
                .eq(PayWalletWithdrawD0::getStatus, status));
    }

//    default List<BrokerageWithdrawSummaryRespBO> selectCountAndSumPriceByUserIdAndStatus(Collection<Long> userIds, Integer status) {
//        List<Map<String, Object>> list = selectMaps(new MPJLambdaWrapper<PayWalletWithdrawD0>()
//                .select(PayWalletWithdrawD0::getUserId)
//                .selectCount(PayWalletWithdrawD0::getId, BrokerageWithdrawSummaryRespBO::getCount)
//                .selectSum(PayWalletWithdrawD0::getPrice)
//                .in(PayWalletWithdrawD0::getUserId, userIds)
//                .eq(PayWalletWithdrawD0::getStatus, status)
//                .groupBy(PayWalletWithdrawD0::getUserId));
//        return BeanUtil.copyToList(list, BrokerageWithdrawSummaryRespBO.class);
//        // selectJoinList有BUG，会与租户插件冲突：解析SQL时，发生异常 https://gitee.com/best_handsome/mybatis-plus-join/issues/I84GYW
////        return selectJoinList(UserWithdrawSummaryBO.class, new MPJLambdaWrapper<PayWalletWithdrawD0>()
////                .select(PayWalletWithdrawD0::getUserId)
////                    .selectCount(PayWalletWithdrawD0::getId, UserWithdrawSummaryBO::getCount)
////                .selectSum(PayWalletWithdrawD0::getPrice)
////                .in(PayWalletWithdrawD0::getUserId, userIds)
////                .eq(PayWalletWithdrawD0::getStatus, status)
////                .groupBy(PayWalletWithdrawD0::getUserId));
//    }

}
