package cn.iocoder.yudao.module.project.controller.admin.wallet.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 钱包分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WalletPageReqVO extends PageParam {


    private Long id;

    @Schema(description = "类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）", example = "2")
    private Integer userType;

    @Schema(description = "用户ID", example = "1530")
    private Long userId;

    @Schema(description = "币种", example = "23450")
    private Long tokenId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private String address;

}