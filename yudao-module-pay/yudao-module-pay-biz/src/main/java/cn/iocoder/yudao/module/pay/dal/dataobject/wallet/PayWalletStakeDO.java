package cn.iocoder.yudao.module.pay.dal.dataobject.wallet;

import java.math.BigDecimal;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.pay.enums.wallet.StakeStatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * PayWalletStakeDo
 *
 * @author libaozhong
 * @version 2024-02-27
 **/


/**
 * reate table ordinals.pay_wallet_stake
 * (
 *     id                 bigint auto_increment comment '编号'
 *         primary key,
 *     wallet_id          bigint                                not null comment '会员钱包 id',
 *     total_price        decimal(40, 8)                        not null comment '用户实际到账余额，例如充 100 送 20，则该值是 120',
 *     pay_price          decimal(40, 8)                        not null comment '实际支付金额',
 *     bonus_price          decimal(40, 8)           default 0                 not null comment '钱包赠送金额',
 *     package_id         bigint                                null comment '充值套餐编号',
 *     stake_status         bit         default b'0'              not null comment '质押状态：[0:未支付 1:已经支付过质押中,2 已经退款]',
 *     pay_order_id       bigint                                null comment '钱包支付流水id',
 *     pay_time           datetime                              null comment '订单支付时间',
 *     pay_refund_id      bigint                                null comment '钱包退款流水id',
 *     refund_total_price   decimal(40, 8)           default 0                 not null comment '退款金额，包含赠送金额',
 *     refund_pay_price     decimal(40, 8)           default 0                 not null comment '退款支付金额',
 *     refund_bonus_price   decimal(40, 8)           default 0                 not null comment '退款钱包赠送金额',
 *     refund_time        datetime                              null comment '退款时间',
 *     creator            varchar(64) default ''                null comment '创建者',
 *     create_time        datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
 *     updater            varchar(64) default ''                null comment '更新者',
 *     update_time        datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
 *     deleted            bit         default b'0'              not null comment '是否删除',
 *     tenant_id          bigint      default 0                 not null comment '租户编号'
 * )
 *     comment '会员质押' row_format = DYNAMIC;
 */

@TableName(value ="pay_wallet_stake")
@Data
public class PayWalletStakeDO extends BaseDO {

         private Long id;
        private Long walletId;
        private Long userId;
        private Integer userType;
        private String address;
        private BigDecimal totalPrice;
        private BigDecimal payPrice;
        private BigDecimal bonusPrice;
        private Long packageId;

        /**
         * @link {{@link StakeStatusEnum}}
         */
        private Integer stakeStatus;
        private Long payOrderId;
        private String payTime;
        private Long payRefundId;
        private BigDecimal refundTotalPrice;
        private BigDecimal refundPayPrice;
        private BigDecimal refundBonusPrice;
        private String refundTime;
}
