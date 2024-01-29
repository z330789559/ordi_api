package cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 交易订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TradeOrderPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String no;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "用户id", example = "7830")
    private Long userId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "支付状态", example = "1")
    private Boolean payStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}