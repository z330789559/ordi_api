package cn.iocoder.yudao.module.trade.dal.dataobject.order;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 交易订单项 DO
 *
 * @author 芋道源码
 */
@TableName(value = "ordi_trade_order_item", autoResultMap = true)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TradeOrderItemDO extends BaseDO {

    // ========== 订单项基本信息 ==========
    /**
     * 编号
     */
    private Long id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 编号
     */
    private Long userId;
    /**
     * 前一个持有者
     *
     * 关联 MemberUserDO 的 id 编号
     */
    private Long formerId;
    /**
     * 订单编号
     *
     * 关联 {@link TradeOrderDO#getId()}
     */
    private Long orderId;

    /**
     * 商品 SPU 编号
     *
     * 关联 ProductSkuDO 的 spuId 编号
     */
    private Long itemId;
    /**
     * 商品 SPU 名称
     *
     * 冗余 ProductSkuDO 的 spuName 编号
     */
    private String itemName;
    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品原价（单），单位：分
     *
     * 对应 ItemDO 的 price 字段
     */
    private BigDecimal price;

    /**
     * 应付金额（总），单位：分
     *
     * = {@link #price} * {@link #quantity}
     * - {@link #vipPrice}
     */
    private BigDecimal payPrice;

    /**
     * VIP 减免金额，单位：分
     */
    private BigDecimal vipPrice;

    private Integer status;

    private BigDecimal rate;
}

