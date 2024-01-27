package cn.iocoder.yudao.module.pay.enums.wallet;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 钱包交易业务分类
 *
 * @author jason
 */
@AllArgsConstructor
@Getter
public enum PayWalletBizTypeEnum implements IntArrayValuable {

    RECHARGE(1, "充值"),
    RECHARGE_REFUND(2, "充值退款"),
    PAYMENT(3, "支付"),
    PAYMENT_REFUND(4, "支付退款"),
    BUY(5, "购买"),
    SELL(6, "出售"),
    RECHARGE_GAS(7, "收入Gas"),
    PAYMENT_GAS(8, "支出Gas"),
    WITHDRAW_FROZEN(9, "提现冻结"),
    WITHDRAW_SUCCESS_UNFROZEN(10, "提现成功解冻"),
    WITHDRAW_FAIL_UNFROZEN(11, "提现失败解冻"),
    WITHDRAW_FROZEN_GAS(12, "冻结Gas"),
    WITHDRAW_SUCCESS_UNFROZEN_GAS(13, "提现成功解冻Gas"),
    WITHDRAW_FAIL_UNFROZEN_GAS(14, "提现失败解冻Gas"),
    SHELVE_REFUND(15, "下架"),
    SHELVE_ON(16, "上架"),
    REWARD_INCOME(17, "分佣收益"),
    REWARD_PAYMENT(18, "分佣支出"),
    TRANSFER_INCOME(19, "转入"),
    TRANSFER_PAYMENT(20, "转出");

    // TODO 后续增加

    /**
     * 业务分类
     */
    private final Integer type;
    /**
     * 说明
     */
    private final String description;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayWalletBizTypeEnum::getType).toArray();

    @Override
    public int[] array() {
         return ARRAYS;
    }
}
