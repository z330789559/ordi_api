package cn.iocoder.yudao.module.pay.dal.dataobject.wallet;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员钱包充值
 */
@TableName(value ="pay_wallet_recharge")
@Data
public class PayWalletRechargeDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 钱包编号
     *
     * 关联 {@link PayWalletDO#getId()}
     */
    private Long walletId;

    /**
     * 用户实际到账余额
     *
     * 例如充 100 送 20，则该值是 120
     */
    private BigDecimal totalPrice;
    /**
     * 实际支付金额
     */
    private BigDecimal payPrice;

    /**
     * 是否已支付
     *
     * true - 已支付
     * false - 未支付
     */
    private Boolean payStatus;

    /**
     * 支付订单编号
     */
    private Long payOrderId;

    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
