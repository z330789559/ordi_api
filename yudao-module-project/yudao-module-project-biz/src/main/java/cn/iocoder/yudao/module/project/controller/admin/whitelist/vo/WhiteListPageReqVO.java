package cn.iocoder.yudao.module.project.controller.admin.whitelist.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 白名单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WhiteListPageReqVO extends PageParam {

    @Schema(description = "地址")
    private String address;

    @Schema(description = "是否开启", example = "2")
    private Integer status;

}