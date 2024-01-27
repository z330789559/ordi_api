package cn.iocoder.yudao.module.trade.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


@Schema(description = "用户 App - 上架订单创建 Request VO")
@Data
public class AppShelveOrderCreateReqVO {

    @Schema(description = "份数", example = "100")
    private Integer quantity;

    @Schema(description = "上架数量")
    private BigDecimal price;

    private BigDecimal rate;
}
