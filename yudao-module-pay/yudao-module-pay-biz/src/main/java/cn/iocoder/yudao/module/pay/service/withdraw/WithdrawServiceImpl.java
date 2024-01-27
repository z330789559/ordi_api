package cn.iocoder.yudao.module.pay.service.withdraw;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.common.util.web3j.EthUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.withdraw.AppWithdrawCreateReqVO;
import cn.iocoder.yudao.module.pay.convert.wallet.WithdrawConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletWithdrawD0;
import cn.iocoder.yudao.module.pay.dal.mysql.withdraw.WithdrawMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;
import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.api.token.TokenApi;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;

@Service
@Validated
public class WithdrawServiceImpl implements WithdrawService {

    @Resource
    private WithdrawMapper withdrawMapper;

    @Resource
    private TokenApi tokenApi;

    @Resource
    private PayWalletApi payWalletApi;

    @Resource
    private MemberUserApi memberApi;

    @Resource
    RedissonClient redissonClient;

    @Value("${contract.out}")
    private String outContract;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletWithdrawD0 createWithdraw(Long userId, AppWithdrawCreateReqVO createReqVO) {
        // 1. 获取Token配置
        TokenRespDTO tokenRespDTO = tokenApi.getToken(2L);

        BigDecimal feePrice = BigDecimal.ZERO;

        // 2.2 判断资产是否足够
        PayWalletRespDTO walletRespDTO = payWalletApi.getOrCreateWallet(userId, PayWalletUserTypeEnum.FINANCE.getType());
        BigDecimal finPrice = createReqVO.getPrice();
        if (finPrice.compareTo(walletRespDTO.getBalance()) > 0) {
            throw exception(WALLET_BALANCE_NOT_ENOUGH);
        }

        PayWalletWithdrawD0 withdraw = WithdrawConvert.INSTANCE.convert(createReqVO, userId, feePrice);
        withdraw.setStatus(WithdrawStatusEnum.AUDITING.getStatus());
        withdraw.setExpireTime(addTime(Duration.ofMinutes(10L)));
        withdraw.setTotalPrice(finPrice);
        withdraw.setWalletId(walletRespDTO.getId());

        return getSelf().submitOrder(userId, walletRespDTO.getId(), withdraw);
    }


    @Transactional(rollbackFor = Exception.class)
    public PayWalletWithdrawD0 submitOrder(Long userId, Long walletId, PayWalletWithdrawD0 withdraw) {

        withdrawMapper.insert(withdraw);

        // 冻结用户BTC
        payWalletApi.frozenWalletBalance(withdraw.getId(), PayWalletBizTypeEnum.WITHDRAW_FROZEN, walletId, withdraw.getPrice());
        // 冻结手续费
//        payWalletApi.frozenWalletBalance(withdraw.getId(), PayWalletBizTypeEnum.WITHDRAW_FROZEN_GAS, walletId, withdraw.getFeePrice());

//        tradeOrderHandler.afterOrderCreate(order, orderItem);

        return withdraw;
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private WithdrawServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }


    @Override
    @TenantIgnore
    public void scanWithdraw() {
        LocalDateTime now = LocalDateTime.now();

        // 所有未超时、待确认的提现记录
        List<PayWalletWithdrawD0> withdraws = withdrawMapper.selectList(new LambdaQueryWrapper<PayWalletWithdrawD0>()
                .eq(PayWalletWithdrawD0::getStatus, WithdrawStatusEnum.AUDITING.getStatus())
                .gt(PayWalletWithdrawD0::getExpireTime, now));


        for (PayWalletWithdrawD0 withdraw : withdraws) {
            RLock lock = redissonClient.getLock("withdraw_id_" + withdraw.getId());
            try {
                lock.lock(1, TimeUnit.SECONDS);

                BigDecimal payNum = EthUtils.getOrderStatus(withdraw.getId(), outContract);
                // 若合约获取金额小于等于0
                if (payNum.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                // 更新状态
                withdrawMapper.update(new LambdaUpdateWrapper<PayWalletWithdrawD0>()
                        .set(PayWalletWithdrawD0::getStatus, WithdrawStatusEnum.WITHDRAW_SUCCESS.getStatus())
                        .eq(PayWalletWithdrawD0::getId, withdraw.getId()));

                // 解冻资产
                payWalletApi.unfrozenWalletBalanceNotRefund(withdraw.getId(), PayWalletBizTypeEnum.WITHDRAW_SUCCESS_UNFROZEN, withdraw.getWalletId(), withdraw.getPrice());

            } finally {
                lock.unlock();
            }
        }

        // 超时、提现失败的记录
        withdraws = withdrawMapper.selectList(new LambdaQueryWrapper<PayWalletWithdrawD0>()
                .eq(PayWalletWithdrawD0::getStatus, WithdrawStatusEnum.AUDITING.getStatus())
                .lt(PayWalletWithdrawD0::getExpireTime, now));

        for (PayWalletWithdrawD0 withdraw : withdraws) {
            RLock lock = redissonClient.getLock("withdraw_id_" + withdraw.getId());
            try {
                lock.lock(1, TimeUnit.SECONDS);
                // 更新状态
                withdrawMapper.update(new LambdaUpdateWrapper<PayWalletWithdrawD0>()
                        .set(PayWalletWithdrawD0::getStatus, WithdrawStatusEnum.WITHDRAW_FAIL.getStatus())
                        .eq(PayWalletWithdrawD0::getId, withdraw.getId()));
                // 提现失败 退回资产
                payWalletApi.unfrozenWalletBalanceNotRefund(withdraw.getId(), PayWalletBizTypeEnum.WITHDRAW_FAIL_UNFROZEN, withdraw.getWalletId(), withdraw.getPrice());
            } finally {
                lock.unlock();
            }
        }
    }

}
