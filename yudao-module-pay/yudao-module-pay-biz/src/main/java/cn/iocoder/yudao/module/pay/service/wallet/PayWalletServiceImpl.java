package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private MemberUserService memberUserService;
    @Resource
    private PayWalletTransactionService walletTransactionService;

    @Resource
    private TransactionTemplate transactionTemplate ;


    @Override
    public PayWalletDO getOrCreateWallet(Long userId, Integer userType) {
        if(userType!=1 && userType!=0){
            throw exception(WALLET_USER_TYPE_ERROR);
        }
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
     return transactionTemplate.execute((action) -> {
         PayWalletDO payWallet = getWalletWithLock(walletId);
		  if (payWallet == null) {
			  throw exception(WALLET_NOT_FOUND);
		  }
		  int updateCounts;
		  switch (bizType) {
		  case PAYMENT:
		  case PAYMENT_GAS:
          case STAKE:
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
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			  throw exception(WALLET_BALANCE_NOT_ENOUGH);
		  }
          BigDecimal afterBalance = payWallet.getBalance().subtract(price);
          WalletTransactionCreateReqBO bo = new WalletTransactionCreateReqBO().setWalletId(payWallet.getId())
                  .setPrice(price.negate()).setBalance(afterBalance).setBizId(bizId)
                  .setBizType(bizType.getType()).setTitle(bizType.getDescription());
          return walletTransactionService.createWalletTransaction(bo);
	  });
    }

    private PayWalletDO getWalletWithLock(Long walletId) {
        return walletMapper.selectByIdForUpdate(walletId);
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
               case RECHARGE_GAS:
               case REWARD_INCOME:
                case UNSTAKE:
                  walletMapper.updateWhenReward(payWallet.getId(), price);
                   break;
                case RECHARGE:
                case BUY:
                case SHELVE_REFUND:
                case TRANSFER_INCOME: { // 充值更新
                    walletMapper.updateWhenRecharge(payWallet.getId(), price);
                    break;
                }
                case REWARD_POOL_INCOME: {
                    break;
                }
            default: {
                log.error("bizType:{} 未实现", bizType);
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
    public PayWalletTransactionDO addBatchWalletBalance(Map<Long, BigDecimal> rewards, String bizId, PayWalletBizTypeEnum bizType) {
        rewards.forEach((walletId, price) -> addWalletBalance(walletId, bizId, bizType, price));
        return null;
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

    @Override
    public void settleGas(Long walletId, BigDecimal price, Long bizId) {

        PayWalletDO wallet = walletMapper.selectById(walletId);

        MemberUserDO user = memberUserService.getUser(wallet.getUserId());
        if(user.getParentId() == null){
            return;
        }
        PayWalletDO parentWallet = walletMapper.selectByUserIdAndType(user.getParentId(), PayWalletUserTypeEnum.FINANCE.getType());
        addWalletBalance(parentWallet.getId(), String.valueOf(bizId), PayWalletBizTypeEnum.RECHARGE_GAS, price.multiply(new BigDecimal("0.1")));
        addWalletBalance(walletId, String.valueOf(bizId), PayWalletBizTypeEnum.REWARD_POOL_INCOME, price.multiply(new BigDecimal("0.9")));

    }

}
