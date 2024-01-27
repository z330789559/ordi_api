package cn.iocoder.yudao.module.product.api.item.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品 Item 信息 Response DTO
 */
@Data
public class TokenRespDTO {
    /**
     * 编号，自增
     */
    private Long id;

    private BigDecimal buyGasFee;

    private BigDecimal sellGasFee;

    private BigDecimal usdPrice;

    private BigDecimal withdrawGasFee;

    private BigDecimal buyGasLimit;

    private BigDecimal sellGasLimit;

    private BigDecimal marketCap;

}
