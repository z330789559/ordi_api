package cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo;

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

@Schema(description = "管理后台 - 提现 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WalletWithdrawRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18582")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "钱包id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23724")
    @ExcelProperty("钱包id")
    private Long walletId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10200")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "8758")
    @ExcelProperty("价格")
    private BigDecimal price;

    @Schema(description = "费用", requiredMode = Schema.RequiredMode.REQUIRED, example = "5990")
    @ExcelProperty("费用")
    private BigDecimal feePrice;

    @Schema(description = "总价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "25298")
    @ExcelProperty("总价格")
    private BigDecimal totalPrice;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("TokenType") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("crm_product_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "审核原因", example = "不香")
    @ExcelProperty("审核原因")
    private String auditReason;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private String auditTime;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "过期时间")
    @ExcelProperty("过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("提现地址")
    private String address;

    @ExcelProperty("Brc地址")
    private String brcAddress;

}