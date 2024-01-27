package cn.iocoder.yudao.module.pay.api.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付单信息 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class MemberWalletRespDTO {

    private Long id;

    private Long userId;

    private BigDecimal balance;

    private BigDecimal payPrice;

    private BigDecimal gasFee;
}
