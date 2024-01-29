package cn.iocoder.yudao.module.project.dal.dataobject.wallet;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 钱包 DO
 *
 * @author 芋道源码
 */
@TableName("pay_wallet")
@KeySequence("pay_wallet_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer userType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 币种
     */
    private Long tokenId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 冻结资产
     */
    private BigDecimal frozen;
    /**
     * 充值
     */
    private BigDecimal recharge;
    /**
     * 提现
     */
    private BigDecimal withdraw;

}