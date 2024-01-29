package cn.iocoder.yudao.module.project.dal.dataobject.producttoken;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品列 DO
 *
 * @author 芋道源码
 */
@TableName("ordi_product_token")
@KeySequence("ordi_product_token_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTokenDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 父ID
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 股份
     */
    private String ticker;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 地板价
     */
    private BigDecimal price;
    /**
     * 持有人
     */
    private Integer holders;
    /**
     * 涨跌幅
     */
    private BigDecimal changeRate;
    /**
     * 图标
     */
    private String image;
    /**
     * 是否收藏
     */
    private Integer isFavorite;
    /**
     * USD价格
     */
    private BigDecimal usdPrice;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 市值
     */
    private BigDecimal marketCap;
    /**
     * 交易额
     */
    private BigDecimal volume;
    /**
     * 24小时交易额
     */
    private BigDecimal volume24h;
    /**
     * 是否可以购买
     */
    private Integer isBuy;
    /**
     * 初始化Item使用
     */
    private String itemName;
    /**
     * 出售Gas费
     */
    private BigDecimal sellGasFee;
    /**
     * 手续费上限
     */
    private BigDecimal sellGasLimit;
    /**
     * 购买Gas费
     */
    private BigDecimal buyGasFee;
    /**
     * 手续费上限
     */
    private BigDecimal buyGasLimit;
    /**
     * 取款费用
     */
    private BigDecimal withdrawGasFee;

    private Integer tradeCount;

}