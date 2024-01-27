package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 App - 转账订单创建 Request VO")
@Data
public class AppPayWalletTransferCreateReqVO {
    @Schema(description = "金额项数组", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "金额项不能为空")
    private List<Item> items;

    private Long userId;

    @NotEmpty(message = "币种不能为空")
    private String symbol;

    @Data
    @Schema(description = "用户 App - 金额项")
    @Valid
    public static class Item {

        @Schema(description = "钱包地址", example = "2048")
        @NotNull(message = "钱包地址不能为空")
        private String address;

        @Schema(description = "金额", example = "1")
        private BigDecimal price;

        @AssertTrue(message = "商品不正确")
        @JsonIgnore
        public boolean isValid() {
            return address != null && price != null;
        }
    }
}
