package cn.iocoder.yudao.module.project.controller.admin.wallet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 钱包新增/修改 Request VO")
@Data
public class WalletSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "29076")
    private Long id;

    @Schema(description = "类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）不能为空")
    private Integer userType;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1530")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED, example = "23450")
    @NotNull(message = "币种不能为空")
    private Long tokenId;

    @Schema(description = "余额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "余额不能为空")
    private BigDecimal balance;

    @Schema(description = "冻结资产", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "冻结资产不能为空")
    private BigDecimal frozen;

    @Schema(description = "充值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "充值不能为空")
    private BigDecimal recharge;

    @Schema(description = "提现", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "提现不能为空")
    private BigDecimal withdraw;

}