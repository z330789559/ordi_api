package cn.iocoder.yudao.module.project.controller.admin.whitelist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 白名单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WhiteListRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8634")
    @ExcelProperty("id")
    private Integer id;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "是否开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "是否开启", converter = DictConvert.class)
    @DictFormat("StartStatus") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}