package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.common.util.web3j.EthUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletRechargeMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;
import static cn.iocoder.yudao.module.pay.convert.wallet.PayWalletRechargeConvert.INSTANCE;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;

/**
 * 钱包充值 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
public class PayWalletRechargeServiceImpl implements PayWalletRechargeService {

    /**
     * TODO 芋艿：放到 payconfig
     */
    private static final Long WALLET_PAY_APP_ID = 8L;

    private static final String WALLET_RECHARGE_ORDER_SUBJECT = "钱包余额充值";

    @Resource
    private PayWalletRechargeMapper walletRechargeMapper;
    @Resource
    private PayWalletService payWalletService;

    @Resource
    private RedissonClient redissonClient;

    @Value("${contract.in}")
    private String inContract;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletRechargeDO createWalletRecharge(Long userId, Integer userType, String userIp,
                                                    AppPayWalletRechargeCreateReqVO reqVO) {

        // 1.1 计算充值金额
        BigDecimal payPrice;
        BigDecimal bonusPrice = BigDecimal.ZERO;
        payPrice = reqVO.getPayPrice();
        // 1.2 插入充值记录
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        PayWalletRechargeDO recharge = INSTANCE.convert(wallet.getId(), payPrice, bonusPrice, reqVO.getPackageId());
        recharge.setExpireTime(addTime(Duration.ofMinutes(10L)));

        walletRechargeMapper.insert(recharge);

        return recharge;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargerPaid(Long id, Long payOrderId) {
        // 1.1 获取钱包充值记录
        PayWalletRechargeDO walletRecharge = walletRechargeMapper.selectById(id);
        if (walletRecharge == null) {
            log.error("[updateWalletRechargerPaid][钱包充值记录不存在，钱包充值记录 id({})]", id);
            throw exception(WALLET_RECHARGE_NOT_FOUND);
        }

        // 2. 更新钱包充值的支付状态
        int updateCount = walletRechargeMapper.updateByIdAndPaid(id, false,
                new PayWalletRechargeDO().setId(id).setPayStatus(true).setPayTime(LocalDateTime.now()));
        if (updateCount == 0) {
            throw exception(WALLET_RECHARGE_UPDATE_PAID_STATUS_NOT_UNPAID);
        }

        // 3. 更新钱包余额
        // TODO @jason：这样的话，未来提现会不会把充值的，也提现走哈。类似先充 100，送 110；然后提现 110；
        // TODO 需要钱包中加个可提现余额
        payWalletService.addWalletBalance(walletRecharge.getWalletId(), String.valueOf(id),
                PayWalletBizTypeEnum.RECHARGE, walletRecharge.getTotalPrice());
    }


    // 充值扫描
    @Override
    @TenantIgnore
    public void scanRechargeOrder() {
        LocalDateTime now = LocalDateTime.now();
        List<PayWalletRechargeDO> recharges = walletRechargeMapper.selectList(new LambdaQueryWrapper<PayWalletRechargeDO>()
                .eq(PayWalletRechargeDO::getPayStatus, false)
                .gt(PayWalletRechargeDO::getExpireTime, now));

        for (PayWalletRechargeDO recharge : recharges) {
            RLock lock = redissonClient.getLock("recharge_id_" + recharge.getId());
            try {
                lock.lock(1, TimeUnit.SECONDS);
                BigDecimal payNum = EthUtils.getOrderStatus(recharge.getId(), inContract);
                // 若合约获取金额小于支付金额
                if (recharge.getPayPrice().compareTo(payNum) > 0) {
                    continue;
                }
                updateWalletRechargerPaid(recharge.getId(), 0L);
            } finally {
                lock.unlock();
            }
        }

    }


}
