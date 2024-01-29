package cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 提现新增/修改 Request VO")
@Data
public class WalletWithdrawSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18582")
    private Long id;

    @Schema(description = "钱包id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23724")
    @NotNull(message = "钱包id不能为空")
    private Long walletId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10200")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "8758")
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @Schema(description = "费用", requiredMode = Schema.RequiredMode.REQUIRED, example = "5990")
    @NotNull(message = "费用不能为空")
    private BigDecimal feePrice;

    @Schema(description = "总价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25298")
    @NotNull(message = "总价格不能为空")
    private BigDecimal totalPrice;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "审核原因", example = "不香")
    private String auditReason;

    @Schema(description = "审核时间")
    private String auditTime;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

}