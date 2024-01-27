package cn.iocoder.yudao.module.pay.api.wallet;

import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import org.springframework.stereotype.Service;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletConvert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 支付单 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class PayWalletApiImpl implements PayWalletApi {

    @Resource
    private PayWalletService payWalletService;

    @Override
    public PayWalletRespDTO getWallet(Long userId) {
        return PayWalletConvert.INSTANCE.covert03(payWalletService.getWalletByUser(userId));
    }

    @Override
    public PayWalletRespDTO getOrCreateWallet(Long userId, Integer type) {
        return PayWalletConvert.INSTANCE.covert03(payWalletService.getOrCreateWallet(userId, type));
    }

    @Override
    public void addWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice) {
        payWalletService.addWalletBalance(walletId, String.valueOf(bizId),
                bizTypeEnum, totalPrice);
    }

    @Override
    public void reduceWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice) {
        payWalletService.reduceWalletBalance(walletId, bizId,
                bizTypeEnum, totalPrice);
    }

    @Override
    public List<PayWalletRespDTO> getWalletListByUserIds(Collection<Long> ids, Integer type) {
        return PayWalletConvert.INSTANCE.convertList(payWalletService.getWalletByUserIds(ids, type));
    }

    @Override
    public void frozenWalletBalance(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice) {
        payWalletService.frozenWalletBalance(walletId, bizId, bizTypeEnum, totalPrice);
    }

    @Override
    public void unfrozenWalletBalanceNotRefund(Long bizId, PayWalletBizTypeEnum bizTypeEnum, Long walletId, BigDecimal totalPrice) {
        payWalletService.unfrozenWalletBalance(walletId, bizId, bizTypeEnum, totalPrice);
    }

}
