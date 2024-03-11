package cn.iocoder.yudao.module.project.controller.admin.whitelist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 白名单新增/修改 Request VO")
@Data
public class WhiteListSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8634")
    private Integer id;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "地址不能为空")
    private String address;

    @Schema(description = "是否开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "是否开启不能为空")
    private Integer status;

}