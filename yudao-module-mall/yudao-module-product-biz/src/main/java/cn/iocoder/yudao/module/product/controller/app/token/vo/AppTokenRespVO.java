package cn.iocoder.yudao.module.product.controller.app.token.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "用户 APP - 商品 Response VO")
public class AppTokenRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long id;

    @Schema(description = "父编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "父编号不能为空")
    private Long parentId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "办公文具")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Schema(description = "图片", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "图片不能为空")
    private String image;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "图片不能为空")
    private Integer status;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "价格不能为空")
    private BigDecimal price;

    @Schema(description = "持有人数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "持有人数不能为空")
    private Integer holders;

    @Schema(description = "涨跌幅", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "涨跌幅不能为空")
    private BigDecimal changeRate;

    @Schema(description = "USD Price", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "USD Price不能为空")
    private BigDecimal usdPrice;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "市值", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal marketCap;

    @Schema(description = "交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal volume;

    @Schema(description = "24小时交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal volume24h;

    @Schema(description = "是否可以购买", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isBuy;

    @Schema(description = "上架Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal sellGasFee;

    @Schema(description = "购买Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal buyGasFee;
}
