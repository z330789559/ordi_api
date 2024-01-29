package cn.iocoder.yudao.module.project.controller.admin.producttoken.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 产品列新增/修改 Request VO")
@Data
public class ProductTokenSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11841")
    private Long id;

    @Schema(description = "父ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21580")
    @NotNull(message = "父ID不能为空")
    private Long parentId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "股份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "股份不能为空")
    private String ticker;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "地板价", requiredMode = Schema.RequiredMode.REQUIRED, example = "24684")
    @NotNull(message = "地板价不能为空")
    private BigDecimal price;

    @Schema(description = "持有人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "持有人不能为空")
    private Integer holders;

    @Schema(description = "涨跌幅", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "涨跌幅不能为空")
    private BigDecimal changeRate;

    @Schema(description = "图标", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "图标不能为空")
    private String image;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否收藏不能为空")
    private Integer isFavorite;

    @Schema(description = "USD价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "14271")
    @NotNull(message = "USD价格不能为空")
    private BigDecimal usdPrice;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "市值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "市值不能为空")
    private BigDecimal marketCap;

    @Schema(description = "交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "交易额不能为空")
    private BigDecimal volume;

    @Schema(description = "24小时交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "24小时交易额不能为空")
    private BigDecimal volume24h;

    @Schema(description = "是否可以购买", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否可以购买不能为空")
    private Integer isBuy;

    @Schema(description = "初始化Item使用", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "初始化Item使用不能为空")
    private String itemName;

    @Schema(description = "出售Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出售Gas费不能为空")
    private BigDecimal sellGasFee;

    @Schema(description = "手续费上限", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "手续费上限不能为空")
    private BigDecimal sellGasLimit;

    @Schema(description = "购买Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "购买Gas费不能为空")
    private BigDecimal buyGasFee;

    @Schema(description = "手续费上限", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "手续费上限不能为空")
    private BigDecimal buyGasLimit;

    @Schema(description = "取款费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "取款费用不能为空")
    private BigDecimal withdrawGasFee;

    @Schema(description = "交易笔数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer tradeCount;

}