package cn.iocoder.yudao.module.pay.service.wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletTransactionConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum.PAYMENT_GAS;

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
    private MemberUserService memberUserService;

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
        if(Objects.equals(bo.getBizType(), PAYMENT_GAS.getType()) && bo.getPrice().negate().compareTo(new BigDecimal(0.0000001)) > 0){
            //更新用户的钱包余额
            payWalletService.settleGas(bo.getWalletId(), bo.getPrice().negate(),bo.getBizId());
        }
        return transaction;
    }

    @Override
    public AppPayWalletIncomeSummaryRespVO getIncomeSummary(Integer bizType, Long userId) {
        //查询父亲id是userid的所有子用户的金融钱吧id

        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, PayWalletUserTypeEnum.FINANCE.getType());
        if(wallet==null){
            return new AppPayWalletIncomeSummaryRespVO();
        }
        AppPayWalletIncomeSummaryRespVO respvo = payWalletTransactionMapper.selectIncomeSummary(bizType, wallet.getId());
        return respvo;
    }

}
