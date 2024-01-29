package cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 交易订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TradeOrderRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7736")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String no;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("TokenType") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "终端", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("终端")
    private Integer terminal;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7830")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "用户ip", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("用户ip")
    private String userIp;

    @Schema(description = "用户备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    @ExcelProperty("用户备注")
    private String userRemark;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4710")
    @ExcelProperty("产品数量")
    private Integer productCount;

    @Schema(description = "取消类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("取消类型")
    private Integer cancelType;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("支付状态")
    private Boolean payStatus;

    @Schema(description = "支付时间")
    @ExcelProperty("支付时间")
    private LocalDateTime payTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime finishTime;

    @Schema(description = "取消时间")
    @ExcelProperty("取消时间")
    private LocalDateTime cancelTime;

    @Schema(description = "总价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "6802")
    @ExcelProperty("总价格")
    private BigDecimal totalPrice;

    @Schema(description = "支付价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25291")
    @ExcelProperty("支付价格")
    private BigDecimal payPrice;

    @Schema(description = "vip 价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "24098")
    @ExcelProperty("vip 价格")
    private BigDecimal vipPrice;

    @Schema(description = "调整价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25238")
    @ExcelProperty("调整价格")
    private BigDecimal adjustPrice;

    @Schema(description = "gas 费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("gas 费用")
    private BigDecimal gasFee;

    @Schema(description = "付款订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32024")
    @ExcelProperty("付款订单id")
    private Long payOrderId;

    @Schema(description = "支付通道", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("支付通道")
    private String payChannelCode;

    @Schema(description = "age用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16450")
    @ExcelProperty("age用户id")
    private Long brokerageUserId;

    @Schema(description = "退款价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "23710")
    @ExcelProperty("退款价格")
    private BigDecimal refundPrice;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}