package cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo;

import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderItemDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 交易订单新增/修改 Request VO")
@Data
public class TradeOrderSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7736")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String no;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "终端", requiredMode = Schema.RequiredMode.REQUIRED)
   private Integer terminal;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7830")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "用户ip", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userIp;

    @Schema(description = "用户备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    private String userRemark;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4710")
    @NotNull(message = "产品数量不能为空")
    private Integer productCount;

    @Schema(description = "取消类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "取消类型不能为空")
    private Integer cancelType;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    private String remark;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "支付状态不能为空")
    private Boolean payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "结束时间")
    private LocalDateTime finishTime;

    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;

    @Schema(description = "总价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "6802")
    @NotNull(message = "总价格不能为空")
    private BigDecimal totalPrice;

    @Schema(description = "支付价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25291")
    @NotNull(message = "支付价格不能为空")
    private BigDecimal payPrice;

    @Schema(description = "vip 价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "24098")
    private BigDecimal vipPrice;

    @Schema(description = "调整价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25238")
    @NotNull(message = "调整价格不能为空")
    private BigDecimal adjustPrice;

    @Schema(description = "gas 费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "gas 费用不能为空")
    private BigDecimal gasFee;

    @Schema(description = "付款订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32024")
    @NotNull(message = "付款订单id不能为空")
    private Long payOrderId;

    @Schema(description = "支付通道", requiredMode = Schema.RequiredMode.REQUIRED)
    private String payChannelCode;

    @Schema(description = "age用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16450")
    private Long brokerageUserId;

    @Schema(description = "退款价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "23710")
    private BigDecimal refundPrice;

    @Schema(description = "交易订单明细列表")
    private List<AdminTradeOrderItemDO> tradeOrderItems;

}