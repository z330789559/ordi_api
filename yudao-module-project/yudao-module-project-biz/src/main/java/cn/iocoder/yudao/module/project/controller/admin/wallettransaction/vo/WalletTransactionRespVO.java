package cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo;

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

@Schema(description = "管理后台 - 钱包流水 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WalletTransactionRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18613")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "流水号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("流水号")
    private String no;

    @Schema(description = "钱包ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7097")
    @ExcelProperty("钱包ID")
    private Long walletId;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "业务类型", converter = DictConvert.class)
    @DictFormat("字典业务类型") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer bizType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "497")
    @ExcelProperty("业务ID")
    private Long bizId;

    @Schema(description = "流水说明", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("流水说明")
    private String title;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "20132")
    @ExcelProperty("金额")
    private BigDecimal price;

    @Schema(description = "余额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("余额")
    private BigDecimal balance;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}