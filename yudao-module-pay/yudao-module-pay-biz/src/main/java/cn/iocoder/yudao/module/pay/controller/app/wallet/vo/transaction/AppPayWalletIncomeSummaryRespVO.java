package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 收入汇总 Response VO")
@Data
public class AppPayWalletIncomeSummaryRespVO {

    @Schema(description = "业务分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer bizType;

    @Schema(description = "总金额",  example = "100")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Schema(description = "今日金额", example = "100")
    private BigDecimal todayAmount=BigDecimal.ZERO;

}
