package cn.iocoder.yudao.module.trade.controller.app.order.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppProductItemRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "商品 名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "iphone18")
    private String name;
}
