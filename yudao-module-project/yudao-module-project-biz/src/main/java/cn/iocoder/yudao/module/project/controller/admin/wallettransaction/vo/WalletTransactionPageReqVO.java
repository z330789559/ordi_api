package cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 钱包流水分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WalletTransactionPageReqVO extends PageParam {

    @Schema(description = "流水号")
    private String no;

    @Schema(description = "钱包ID", example = "7097")
    private Long walletId;

    @Schema(description = "业务类型", example = "2")
    private Integer bizType;

    @Schema(description = "业务ID", example = "497")
    private Long bizId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private String address;

}