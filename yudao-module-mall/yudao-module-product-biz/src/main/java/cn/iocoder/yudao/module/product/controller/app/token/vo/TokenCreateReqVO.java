package cn.iocoder.yudao.module.product.controller.app.token.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 商品分类创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TokenCreateReqVO extends TokenBaseVO {

    @Schema(description = "分类描述", example = "描述")
    private String description;

}
