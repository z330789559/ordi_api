package cn.iocoder.yudao.module.product.controller.app.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 商品 SPU 详细 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemDetailRespVO extends ItemRespVO {

    // ========== SKU 相关字段 =========

    @Schema(description = "SKU 数组")
    private List<ItemRespVO> skus;

}
