package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletTransactionConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 钱包流水 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
@Validated
public class PayWalletTransactionServiceImpl implements PayWalletTransactionService {

    /**
     * 钱包流水的 no 前缀
     */
    private static final String WALLET_NO_PREFIX = "W";

    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;
    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(Long userId, Integer userType,
                                                                       AppPayWalletTransactionPageReqVO pageVO) {
        PayWalletDO btcWallet = payWalletService.getOrCreateWallet(userId, UserTypeEnum.BTC.getValue());
        PayWalletDO nubtWallet = payWalletService.getOrCreateWallet(userId, UserTypeEnum.NUBT.getValue());
        return payWalletTransactionMapper.selectPage(new Long[]{btcWallet.getId(),nubtWallet.getId()}, pageVO.getType(),pageVO.getBizType(), pageVO);
    }

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionAllPage(AppPayWalletTransactionPageReqVO pageVO) {
        return payWalletTransactionMapper.selectPage(pageVO.getType(),pageVO.getBizType(), pageVO);
    }

    @Override
    public PayWalletTransactionDO createWalletTransaction(WalletTransactionCreateReqBO bo) {
        PayWalletTransactionDO transaction = PayWalletTransactionConvert.INSTANCE.convert(bo)
                .setNo(noRedisDAO.generate(WALLET_NO_PREFIX));
        payWalletTransactionMapper.insert(transaction);
        return transaction;
    }

    @Override
    public AppPayWalletIncomeSummaryRespVO getIncomeSummary(Integer bizType, Long userId) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, PayWalletUserTypeEnum.FINANCE.getType());
        return payWalletTransactionMapper.selectIncomeSummary(bizType, wallet.getId());
    }

}
