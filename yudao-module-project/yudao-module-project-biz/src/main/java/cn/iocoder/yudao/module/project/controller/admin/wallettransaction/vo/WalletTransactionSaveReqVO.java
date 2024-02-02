package cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 钱包流水新增/修改 Request VO")
@Data
public class WalletTransactionSaveReqVO {
    private Long id;

    @Schema(description = "流水号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "流水号不能为空")
    private String no;

    @Schema(description = "钱包ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7097")
    @NotNull(message = "钱包ID不能为空")
    private Long walletId;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务类型不能为空")
    private Integer bizType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "497")
    @NotNull(message = "业务ID不能为空")
    private Long bizId;

    @Schema(description = "流水说明", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "流水说明不能为空")
    private String title;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "20132")
    @NotNull(message = "金额不能为空")
    private BigDecimal price;

    @Schema(description = "余额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "余额不能为空")
    private BigDecimal balance;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @NotEmpty(message = "备注不能为空")
    private String remark;

}