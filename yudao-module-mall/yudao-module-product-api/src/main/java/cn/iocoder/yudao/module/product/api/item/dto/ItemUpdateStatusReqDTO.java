package cn.iocoder.yudao.module.product.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品 更新库存 Request DTO
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateStatusReqDTO {

    /**
     * 商品 SKU
     */
    @NotNull(message = "商品 SKU 不能为空")
    private List<Item> items;

    private Integer status;

    @Data
    public static class Item {

        /**
         * 商品 SKU 编号
         */
        @NotNull(message = "商品 SKU 编号不能为空")
        private Long id;
    }

}

