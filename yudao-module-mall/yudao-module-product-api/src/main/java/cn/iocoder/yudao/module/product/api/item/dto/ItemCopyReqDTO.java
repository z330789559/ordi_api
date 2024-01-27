package cn.iocoder.yudao.module.product.api.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 复制商品创建请求 DTO
 */
@Data
public class ItemCopyReqDTO {

    @NotNull(message = "id")
    private Long itemId;

    /**
     * 购买人
     */
    @NotNull(message = "购买人不能为空")
    private Long userId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

}
