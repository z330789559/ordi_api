package cn.iocoder.yudao.module.pay.dal.mysql.wallet;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface PayWalletMapper extends BaseMapperX<PayWalletDO> {

    default PayWalletDO selectByUserIdAndType(Long userId, Integer userType) {
        return selectOne(PayWalletDO::getUserId, userId,
                PayWalletDO::getUserType, userType);
    }

    /**
     * 当充值的时候，更新钱包
     *
     * @param id 钱包 id
     * @param price 钱包金额
     */
    default int updateWhenRecharge(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price
                        + ", recharge = recharge + " + price)
                .eq(PayWalletDO::getId, id);
        return update(null, lambdaUpdateWrapper);
    }


    /**
     * 当充值的时候，更新钱包
     *
     * @param id 钱包 id
     * @param price 钱包金额
     */
    default int updateWhenReward(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price)
                .eq(PayWalletDO::getId, id);
        return update(null, lambdaUpdateWrapper);
    }


    /**
     * 冻结钱包部分余额
     *
     * @param id 钱包 id
     * @param price 冻结金额
     */
    default int freezePrice(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance - " + price
                        + ", frozen = frozen + " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getBalance, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 解冻钱包余额
     *
     * @param id 钱包 id
     * @param price 解冻金额
     */
    default int unFreezePrice(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price
                        + ", frozen = frozen - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getFrozen, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    default int unFreezePriceNotRefund(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" frozen = frozen - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getFrozen, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 当充值退款时, 更新钱包
     *
     * @param id 钱包 id
     * @param price 退款金额
     */
    default  int updateWhenRechargeRefund(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" frozen = frozen - " + price
                        + ", recharge = recharge - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getFrozen, price)
                .ge(PayWalletDO::getRecharge, price);// cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 当消费时候， 更新钱包
     *
     * @param price 消费金额
     * @param id 钱包 id
     */
    default int updateWhenConsumption(Long id, BigDecimal price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getBalance, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    @Select("select * from pay_wallet where id = #{walletId} for update")
    PayWalletDO selectByIdForUpdate(Long walletId);
}




