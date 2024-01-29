package cn.iocoder.yudao.module.project.controller.admin.producttoken.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 产品列 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductTokenRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11841")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "父ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21580")
    @ExcelProperty("父ID")
    private Long parentId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "股份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("股份")
    private String ticker;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "地板价", requiredMode = Schema.RequiredMode.REQUIRED, example = "24684")
    @ExcelProperty("地板价")
    private BigDecimal price;

    @Schema(description = "持有人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("持有人")
    private Integer holders;

    @Schema(description = "涨跌幅", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("涨跌幅")
    private BigDecimal changeRate;

    @Schema(description = "图标", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("图标")
    private String image;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否收藏")
    private Integer isFavorite;

    @Schema(description = "USD价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "14271")
    @ExcelProperty("USD价格")
    private BigDecimal usdPrice;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "市值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("市值")
    private BigDecimal marketCap;

    @Schema(description = "交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交易额")
    private BigDecimal volume;

    @Schema(description = "24小时交易额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("24小时交易额")
    private BigDecimal volume24h;

    @Schema(description = "是否可以购买", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否可以购买")
    private Integer isBuy;

    @Schema(description = "初始化Item使用", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("初始化Item使用")
    private String itemName;

    @Schema(description = "出售Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出售Gas费")
    private BigDecimal sellGasFee;

    @Schema(description = "手续费上限", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("手续费上限")
    private BigDecimal sellGasLimit;

    @Schema(description = "购买Gas费", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("购买Gas费")
    private BigDecimal buyGasFee;

    @Schema(description = "手续费上限", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("手续费上限")
    private BigDecimal buyGasLimit;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "取款费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("取款费用")
    private BigDecimal withdrawGasFee;

    @Schema(description = "交易笔数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交易笔数")
    private Integer tradeCount;

}