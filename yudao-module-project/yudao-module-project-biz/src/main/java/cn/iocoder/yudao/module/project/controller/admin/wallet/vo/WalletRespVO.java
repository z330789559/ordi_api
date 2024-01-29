package cn.iocoder.yudao.module.project.controller.admin.wallet.vo;

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

@Schema(description = "管理后台 - 钱包 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WalletRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "29076")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "类型 0 => 金融类型（BTC） 1 => 流通货币（NUBT）", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1530")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED, example = "23450")
    @ExcelProperty("币种")
    private Long tokenId;

    @Schema(description = "余额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("余额")
    private BigDecimal balance;

    @Schema(description = "冻结资产", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("冻结资产")
    private BigDecimal frozen;

    @Schema(description = "充值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("充值")
    private BigDecimal recharge;

    @Schema(description = "提现", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("提现")
    private BigDecimal withdraw;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}