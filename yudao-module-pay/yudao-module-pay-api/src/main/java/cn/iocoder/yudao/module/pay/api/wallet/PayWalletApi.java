package cn.iocoder.yudao.module.pay.api.wallet;

import cn.iocoder.yudao.module.pay.api.wallet.dto.MemberWalletRespDTO;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 支付单 API 接口
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
public interface PayWalletApi {
    /**
     * 获得支付单
     *
     * @param userId 用户ID
     * @return 支付单
     */
    PayWalletRespDTO getWallet(Long userId);

    PayWalletRespDTO getOrCreateWallet(Long userId, Integer type);

    void addWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice);

    void reduceWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice);

    void frozenWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice);

    void unfrozenWalletBalanceNotRefund(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice);

    List<PayWalletRespDTO> getWalletListByUserIds(Collection<Long> ids, Integer type);

}