package cn.iocoder.yudao.module.product.api.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 复制商品创建请求 DTO
 */
@Data
public class ItemCreateReqDTO {

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @NotNull(message = "金额不能为空")
    private BigDecimal price;

    private Long tokenId;

    private Long userId;

}
