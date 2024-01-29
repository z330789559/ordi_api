package cn.iocoder.yudao.module.project.dal.dataobject.tradeorder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 交易订单 DO
 *
 * @author 芋道源码
 */
@TableName("ordi_trade_order")
@KeySequence("ordi_trade_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTradeOrderDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 订单号
     */
    private String no;
    /**
     * 类型
     *
     */
    private Integer type;
    /**
     * 终端
     */
    private Integer terminal;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户ip
     */
    private String userIp;
    /**
     * 用户备注
     */
    private String userRemark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 产品数量
     */
    private Integer productCount;
    /**
     * 取消类型
     */
    private Integer cancelType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 支付状态
     */
    private Boolean payStatus;
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    /**
     * 结束时间
     */
    private LocalDateTime finishTime;
    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;
    /**
     * 总价格
     */
    private BigDecimal totalPrice;
    /**
     * 支付价格
     */
    private BigDecimal payPrice;
    /**
     * vip 价格
     */
    private BigDecimal vipPrice;
    /**
     * 调整价格
     */
    private BigDecimal adjustPrice;
    /**
     * gas 费用
     */
    private BigDecimal gasFee;
    /**
     * 付款订单id
     */
    private Long payOrderId;
    /**
     * 支付通道
     */
    private String payChannelCode;
    /**
     * age用户id
     */
    private Long brokerageUserId;
    /**
     * 退款价格
     */
    private BigDecimal refundPrice;

}