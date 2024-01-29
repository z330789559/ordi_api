package cn.iocoder.yudao.module.project.dal.dataobject.walletwithdraw;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 提现 DO
 *
 * @author 芋道源码
 */
@TableName("pay_wallet_withdraw")
@KeySequence("pay_wallet_withdraw_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletWithdrawDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 钱包id
     */
    private Long walletId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 费用
     */
    private BigDecimal feePrice;
    /**
     * 总价格
     */
    private BigDecimal totalPrice;
    /**
     * 类型
     *
     */
    private Integer type;
    /**
     * 状态
     *
     */
    private Integer status;
    /**
     * 审核原因
     */
    private String auditReason;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 提现地址
     */
    private String address;

}