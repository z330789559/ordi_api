package cn.iocoder.yudao.module.product.controller.app.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 App - 商品 SPU Response VO")
@Data
public class AppItemPageRespVO {

    @Schema(description = "商品 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "2.00")
    private BigDecimal price;

    @Schema(description = "BTC价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "2.00")
    private BigDecimal totalPrice;

    @Schema(description = "Usdt总价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "2.00")
    private BigDecimal usdtTotalPrice;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tokenId;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @Schema(description = "单位名", requiredMode = Schema.RequiredMode.REQUIRED, example = "个")
    private String unitName;

    @Schema(description = "商品销量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer salesCount;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "2.00")
    private BigDecimal vipPrice;

    @Schema(description = "商品状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;
}
