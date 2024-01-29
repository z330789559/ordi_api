package cn.iocoder.yudao.module.project.dal.dataobject.tradeorder;

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
 * 交易订单明细 DO
 *
 * @author 芋道源码
 */
@TableName("ordi_trade_order_item")
@KeySequence("ordi_trade_order_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTradeOrderItemDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 类型 1 => 购买 2 => 上架
     */
    private Integer type;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 前者id
     */
    private Long formerId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long itemId;
    /**
     * 商品名称
     */
    private String itemName;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 支付价格
     */
    private BigDecimal payPrice;
    /**
     * vip价格
     */
    private BigDecimal vipPrice;
    /**
     * USDT汇率
     */
    private BigDecimal rate;
    /**
     * 状态
     */
    private Integer status;

}