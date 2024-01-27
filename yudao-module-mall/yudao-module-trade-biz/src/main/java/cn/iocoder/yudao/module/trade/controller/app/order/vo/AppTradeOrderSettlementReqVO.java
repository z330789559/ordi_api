package cn.iocoder.yudao.module.trade.controller.app.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "用户 App - 交易订单结算 Request VO")
@Data
@Valid
public class AppTradeOrderSettlementReqVO {

    @Schema(description = "商品项数组", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品不能为空")
    private List<Item> items;

    @Data
    @Schema(description = "用户 App - 商品项")
    @Valid
    public static class Item {

        @Schema(description = "商品 编号", example = "2048")
        @NotNull(message = "商品 编号不能为空")
        private Long itemId;

        @Schema(description = "购买数量", example = "1")
        @Min(value = 1, message = "购买数量最小值为 {value}")
        private Integer quantity;

        @AssertTrue(message = "商品不正确")
        @JsonIgnore
        public boolean isValid() {
            return itemId != null && quantity != null;
        }
    }
}
