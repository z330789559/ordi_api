package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.withdraw;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Schema(description = "用户 App - 提现创建 Request VO")
@Data
public class AppWithdrawCreateReqVO {
    @Schema(description = "提现金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @PositiveOrZero(message = "提现金额不能小于 0")
    @NotNull(message = "提现金额不能为空")
    private BigDecimal price;

    @Min(value = 1, message = "token类型不能小于1")
    @Max(value = 2, message = "token类型不能大于2")
    private Integer tokenType;

    @NotEmpty
    private String address;
}
