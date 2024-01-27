package cn.iocoder.yudao.module.trade.dal.dataobject.order;

import cn.iocoder.yudao.framework.common.enums.TerminalEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易订单 DO
 *
 * @author 芋道源码
 */
@TableName("ordi_trade_order")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeOrderDO extends BaseDO {

    // ========== 订单基本信息 ==========
    /**
     * 订单编号，主键自增
     */
    private Long id;
    /**
     * 订单流水号
     *
     * 例如说，1146347329394184195
     */
    private String no;
    /**
     * 订单类型
     *
     * 枚举 {@link TradeOrderTypeEnum}
     */
    private Integer type;
    /**
     * 订单来源
     *
     * 枚举 {@link TerminalEnum}
     */
    private Integer terminal;
    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 编号
     */
    private Long userId;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 用户备注
     */
    private String userRemark;
    /**
     * 订单状态
     *
     * 枚举 {@link TradeOrderStatusEnum}
     */
    private Integer status;
    /**
     * 购买的商品数量
     */
    private Integer productCount;
    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;
    /**
     * 取消类型
     *
     * 枚举 {@link TradeOrderCancelTypeEnum}
     */
    private Integer cancelType;
    /**
     * 商家备注
     */
    private String remark;

    // ========== 价格 + 支付基本信息 ==========

    // 价格文档 - 淘宝：https://open.taobao.com/docV3.htm?docId=108471&docType=1
    // 价格文档 - 京东到家：https://openo2o.jddj.com/api/getApiDetail/182/4d1494c5e7ac4679bfdaaed950c5bc7f.htm
    // 价格文档 - 有赞：https://doc.youzanyun.com/detail/API/0/906

    /**
     * 支付订单编号
     *
     * 对接 pay-module-biz 支付服务的支付订单编号，即 PayOrderDO 的 id 编号
     */
    private Long payOrderId;
    /**
     * 是否已支付
     *
     * true - 已经支付过
     * false - 没有支付过
     */
    private Boolean payStatus;
    /**
     * 付款时间
     */
    private LocalDateTime payTime;
    /**
     * 支付渠道
     *
     * 对应 PayChannelEnum 枚举
     */
    private String payChannelCode;

    /**
     * 商品原价，单位：分
     *
     * totalPrice = {@link TradeOrderItemDO#getPrice()} * {@link TradeOrderItemDO#getQuantity()} 求和
     *
     * 对应 taobao 的 trade.total_fee 字段
     */
    private BigDecimal totalPrice;

    /**
     * 应付金额（总），单位：分
     *
     * = {@link #totalPrice}
     * - {@link #vipPrice}
     */
    private BigDecimal payPrice;

    private BigDecimal vipPrice;

    /**
     * 订单调价，单位：分
     *
     * 正数，加价；负数，减价
     */
    private BigDecimal adjustPrice;

    private BigDecimal gasFee;
}
