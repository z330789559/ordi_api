package cn.iocoder.yudao.module.pay.dal.dataobject.wallet;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员钱包 DO
 */
@TableName(value ="pay_wallet")
@Data
public class PayWalletDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 用户 id
     *
     * 关联 MemberUserDO 的 id 编号
     * 关联 AdminUserDO 的 id 编号
     */
    private Long userId;
    /**
     * 用户类型, 预留 多商户转帐可能需要用到
     *
     * 关联 {@link UserTypeEnum}
     */
    private Integer userType;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozen;

    /**
     * 累计提现
     */
    private BigDecimal withdraw;
    /**
     * 累计充值
     */
    private BigDecimal recharge;

    @TableField(exist = false)
    private BigDecimal btcBalance;

    @TableField(exist = false)
    private BigDecimal nubtBalance;

    @TableField(exist = false)
    private BigDecimal btcBalanceValue;

    @TableField(exist = false)
    private BigDecimal nubtBalanceValue;

}
