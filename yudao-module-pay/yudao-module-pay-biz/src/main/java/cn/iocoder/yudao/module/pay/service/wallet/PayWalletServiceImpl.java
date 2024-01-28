package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum.PAYMENT;

/**
 * 钱包 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
public class PayWalletServiceImpl implements PayWalletService {

    @Resource
    private PayWalletMapper walletMapper;
    @Resource
    private PayWalletTransactionService walletTransactionService;

    @Override
    public PayWalletDO getOrCreateWallet(Long userId, Integer userType) {
        PayWalletDO wallet = walletMapper.selectByUserIdAndType(userId, userType);
        if (wallet == null) {
            wallet = (PayWalletDO) new PayWalletDO().setUserId(userId).setUserType(userType)
                    .setBalance(BigDecimal.ZERO).setRecharge(BigDecimal.ZERO).setWithdraw(BigDecimal.ZERO).setDeleted(false);
            wallet.setCreateTime(LocalDateTime.now());
            walletMapper.insert(wallet);
        }
        return wallet;
    }

    @Override
    public PayWalletDO getWallet(Long walletId) {
        return walletMapper.selectById(walletId);
    }

    @Override
    public PayWalletDO getWalletByUser(Long userId) {
        return walletMapper.selectByUserIdAndType(userId, 1);
    }

    @Override
    public List<PayWalletDO> getWalletByUserIds(Collection<Long> ids, Integer type) {
        return walletMapper.selectList(new LambdaQueryWrapperX<PayWalletDO>()
                .inIfPresent(PayWalletDO::getUserId, ids)
                .eqIfPresent(PayWalletDO::getUserType, type)
                .orderByDesc(PayWalletDO::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletTransactionDO orderPay(Long userId, Integer userType, String outTradeNo, BigDecimal price) {
        PayWalletDO wallet = getOrCreateWallet(userId, userType);
        // 2. 扣减余额
        return reduceWalletBalance(wallet.getId(), 0L, PAYMENT, price);
    }

    @Override
    public PayWalletTransactionDO reduceWalletBalance(Long walletId, Long bizId,
                                                      PayWalletBizTypeEnum bizType, BigDecimal price) {
        // 1. 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            throw exception(WALLET_NOT_FOUND);
        }

        // 2.1 扣除余额
        int updateCounts;
        switch (bizType) {
            case PAYMENT:
            case PAYMENT_GAS:
            case SELL:
            case REWARD_PAYMENT: {
                updateCounts = walletMapper.updateWhenConsumption(payWallet.getId(), price);
                break;
            }
            case RECHARGE_REFUND: {
                updateCounts = walletMapper.updateWhenRechargeRefund(payWallet.getId(), price);
                break;
            }
            default: {
                throw new UnsupportedOperationException("待实现");
            }
        }
        if (updateCounts == 0) {
            throw exception(WALLET_BALANCE_NOT_ENOUGH);
        }
        // 2.2 生成钱包流水
        BigDecimal afterBalance = payWallet.getBalance().subtract(price);
        WalletTransactionCreateReqBO bo = new WalletTransactionCreateReqBO().setWalletId(payWallet.getId())
                .setPrice(price.negate()).setBalance(afterBalance).setBizId(bizId)
                .setBizType(bizType.getType()).setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(bo);
    }

    @Override
    public PayWalletTransactionDO addWalletBalance(Long walletId, String bizId,
                                                   PayWalletBizTypeEnum bizType, BigDecimal price) {
        // 1.1 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            throw exception(WALLET_NOT_FOUND);
        }
        // 1.2 更新钱包金额
        switch (bizType) {
            case RECHARGE:
            case RECHARGE_GAS:
            case BUY:
            case SHELVE_REFUND:
            case REWARD_INCOME:
            case TRANSFER_INCOME: { // 充值更新
                walletMapper.updateWhenRecharge(payWallet.getId(), price);
                break;
            }
            default: {
                throw new UnsupportedOperationException("待实现");
            }
        }

        // 2. 生成钱包流水
        WalletTransactionCreateReqBO transactionCreateReqBO = new WalletTransactionCreateReqBO()
                .setWalletId(payWallet.getId()).setPrice(price).setBalance(payWallet.getBalance().add(price))
                .setBizId(Long.valueOf(bizId)).setBizType(bizType.getType()).setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(transactionCreateReqBO);
    }

    @Override
    public PayWalletTransactionDO frozenWalletBalance(Long walletId, Long bizId, PayWalletBizTypeEnum bizType, BigDecimal price) {
        // 1.1 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            throw exception(WALLET_NOT_FOUND);
        }
        // 1.2 更新钱包金额
        switch (bizType) {
            case WITHDRAW_FROZEN_GAS:
            case WITHDRAW_FROZEN: { // 冻结
                walletMapper.freezePrice(payWallet.getId(), price);
                break;
            }
            default: {
                throw new UnsupportedOperationException("待实现");
            }
        }

        // 2. 生成钱包流水
        WalletTransactionCreateReqBO transactionCreateReqBO = new WalletTransactionCreateReqBO();
        transactionCreateReqBO.setWalletId(payWallet.getId());
        transactionCreateReqBO.setPrice(price);
        transactionCreateReqBO.setBalance(payWallet.getBalance().subtract(price));
        transactionCreateReqBO.setBizId(bizId);
        transactionCreateReqBO.setBizType(bizType.getType());
        transactionCreateReqBO.setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(transactionCreateReqBO);
    }

    @Override
    public PayWalletTransactionDO unfrozenWalletBalance(Long walletId, Long bizId, PayWalletBizTypeEnum bizType, BigDecimal price) {
        // 1.1 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            throw exception(WALLET_NOT_FOUND);
        }
        BigDecimal balance = payWallet.getBalance();
        // 1.2 更新钱包金额
        switch (bizType) {
            case WITHDRAW_SUCCESS_UNFROZEN_GAS:
            case WITHDRAW_SUCCESS_UNFROZEN: { // 提现成功解冻资产
                walletMapper.unFreezePriceNotRefund(payWallet.getId(), price);
                break;
            }
            case WITHDRAW_FAIL_UNFROZEN_GAS:
            case WITHDRAW_FAIL_UNFROZEN: { // 提现失败，解冻资产，退回资产
                walletMapper.unFreezePrice(payWallet.getId(), price);
                balance = balance.add(price);
                break;
            }
            default: {
                throw new UnsupportedOperationException("待实现");
            }
        }

        // 2. 生成钱包流水
        WalletTransactionCreateReqBO transactionCreateReqBO = new WalletTransactionCreateReqBO();
        transactionCreateReqBO.setWalletId(payWallet.getId());
        transactionCreateReqBO.setPrice(price);
        transactionCreateReqBO.setBalance(balance);
        transactionCreateReqBO.setBizId(bizId);
        transactionCreateReqBO.setBizType(bizType.getType());
        transactionCreateReqBO.setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(transactionCreateReqBO);
    }

    @Override
    public void freezePrice(Long id, BigDecimal price) {
        int updateCounts = walletMapper.freezePrice(id, price);
        if (updateCounts == 0) {
            throw exception(WALLET_BALANCE_NOT_ENOUGH);
        }
    }

    @Override
    public void unfreezePrice(Long id, BigDecimal price) {
        int updateCounts = walletMapper.unFreezePrice(id, price);
        if (updateCounts == 0) {
            throw exception(WALLET_FREEZE_PRICE_NOT_ENOUGH);
        }
    }

    @Override
    public void commission(long uid, BigDecimal free) {

    }

}
