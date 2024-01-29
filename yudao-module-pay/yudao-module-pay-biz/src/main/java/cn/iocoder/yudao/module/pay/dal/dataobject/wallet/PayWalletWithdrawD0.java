package cn.iocoder.yudao.module.pay.dal.dataobject.wallet;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("pay_wallet_withdraw")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayWalletWithdrawD0 extends BaseDO {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 字段
     */
    private Long userId;

    /**
     * 提现金额，单位：分
     */
    private BigDecimal price;
    /**
     * 提现手续费，单位：分
     */
    private BigDecimal feePrice;
    /**
     * 当前总佣金，单位：分
     */
    private BigDecimal totalPrice;
    /**
     * 提现类型
     * <p>
     */
    private Integer type;

    /**
     * 状态
     * <p>
     * 枚举 {@link WithdrawStatusEnum}
     */
    private Integer status;
    /**
     * 审核驳回原因
     */
    private String auditReason;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 备注
     */
    private String remark;

    private LocalDateTime expireTime;

    private Long walletId;

    private String address;
}
