package cn.iocoder.yudao.module.product.controller.app.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 App - 商品 明细 Response VO")
@Data
public class AppItemDetailRespVO {

    @Schema(description = "商品 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "商品销量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer salesCount;

    @Schema(description = "单位名", requiredMode = Schema.RequiredMode.REQUIRED, example = "stats")
    private String unit;

    private BigDecimal vipPrice;

    private BigDecimal price;
}
