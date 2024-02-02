package cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 钱包流水 DO
 *
 * @author 芋道源码
 */
@TableName("pay_wallet_transaction")
@KeySequence("pay_wallet_transaction_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 流水号
     */
    private String no;
    /**
     * 钱包ID
     */
    private Long walletId;
    /**
     * 业务类型
     *
     * 枚举 {@link TODO 字典业务类型 对应的类}
     */
    private Integer bizType;
    /**
     * 业务ID
     */
    private Long bizId;
    /**
     * 流水说明
     */
    private String title;
    /**
     * 金额
     */
    private BigDecimal price;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 备注
     */
    private String remark;


}