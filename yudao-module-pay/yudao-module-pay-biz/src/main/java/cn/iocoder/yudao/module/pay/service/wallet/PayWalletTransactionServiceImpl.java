package cn.iocoder.yudao.module.pay.service.wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.enums.MemberLevelConstants;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletTransactionConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.UnAssignRewardSummaryDo;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.TokenType;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.api.token.TokenApi;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum.PAYMENT_GAS;
import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum.REWARD_INCOME;
import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum.REWARD_POOL_INCOME;

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

    private static final String REWARD_NO_PREFIX = "";

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private PayWalletService payWalletService;

    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;
    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Resource
    private TokenApi tokenApi;


    @Resource
    private Executor executor;

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

    @Override
    public UnAssignRewardSummaryDo getUnAssignRewardSummary() {
        TokenRespDTO token = tokenApi.getToken(Long.valueOf(TokenType.BTC.getType()));
        UnAssignRewardSummaryDo res = payWalletTransactionMapper.selectRewardSummary();
        res.setTodayAmount(res.getTodayAmount().divide(token.getUsdPrice(),RoundingMode.DOWN));
        res.setTotalAmount(res.getTotalAmount().divide(token.getUsdPrice(),RoundingMode.DOWN));
        res.setUpLimit(new BigDecimal(noRedisDAO.getRewardLimit()));
        return res;
    }

    @Override
    public void assiginReward() {
        TenantContextHolder.setIgnore(true);
        UnAssignRewardSummaryDo rewardAmountDo = payWalletTransactionMapper.selectRewardSummary();
        TokenRespDTO token = tokenApi.getToken(Long.valueOf(TokenType.BTC.getType()));
        if(rewardAmountDo.getTodayAmount().divide(token.getUsdPrice(),RoundingMode.DOWN).compareTo(new BigDecimal(noRedisDAO.getRewardLimit()))<0){
            return;
        }
        String bizId = noRedisDAO.generate(REWARD_NO_PREFIX);
         lockAllUnAssignReward(bizId);
         Map<Long,BigDecimal> cacheAmount =new HashMap<>();
        List<Long> ids = memberUserService.getMemberUserWalletListByLevel(MemberLevelConstants.LEVEL_1);
        if(!ids.isEmpty()){
            BigDecimal perAmount =  rewardAmountDo.getTodayAmount()
                    .multiply(MemberLevelConstants.LEVEL_1_REWARD_RATE).divide(new BigDecimal(ids.size()), 8, RoundingMode.HALF_UP);
            cacheAmount.putAll(ids.stream().collect(Collectors.toMap(id->id, id->perAmount)));
        }
        ids = memberUserService.getMemberUserWalletListByLevel(MemberLevelConstants.LEVEL_2);
        if(!ids.isEmpty()){
            BigDecimal perAmount =rewardAmountDo.getTodayAmount()
                    .multiply(MemberLevelConstants.LEVEL_2_REWARD_RATE).divide(new BigDecimal(ids.size()), 8, RoundingMode.HALF_UP);
            Map<Long,BigDecimal> cacheAmount2 =new HashMap<>();
            cacheAmount2.putAll(ids.stream().collect(Collectors.toMap(id->id, id->perAmount)));
            cacheAmount2.forEach((k, v) -> cacheAmount.merge(k, v, BigDecimal::add));
        }
        ids = memberUserService.getMemberUserWalletListByLevel(MemberLevelConstants.LEVEL_3);
        if(!ids.isEmpty()){
            BigDecimal perAmount =rewardAmountDo.getTodayAmount()
                    .multiply(MemberLevelConstants.LEVEL_3_REWARD_RATE).divide(new BigDecimal(ids.size()), 8, RoundingMode.HALF_UP);
            Map<Long,BigDecimal> cacheAmount3 =new HashMap<>();
            cacheAmount3.putAll(ids.stream().collect(Collectors.toMap(id->id, id->perAmount)));
            cacheAmount3.forEach((k, v) -> cacheAmount.merge(k, v, BigDecimal::add));
        }

        ids = memberUserService.getMemberUserWalletListByLevel(MemberLevelConstants.LEVEL_4);
        if(!ids.isEmpty()){
            BigDecimal perAmount =rewardAmountDo.getTodayAmount()
                    .multiply(MemberLevelConstants.LEVEL_4_REWARD_RATE).divide(new BigDecimal(ids.size()), 8, RoundingMode.HALF_UP);
            Map<Long,BigDecimal> cacheAmount4 =new HashMap<>();
            cacheAmount4.putAll(ids.stream().collect(Collectors.toMap(id->id, id->perAmount)));
            cacheAmount4.forEach((k, v) -> cacheAmount.merge(k, v, BigDecimal::add));
        }
        ids = memberUserService.getMemberUserWalletListByLevel(MemberLevelConstants.LEVEL_5);
        if(!ids.isEmpty()){
            BigDecimal perAmount =rewardAmountDo.getTodayAmount()
                    .multiply(MemberLevelConstants.LEVEL_5_REWARD_RATE).divide(new BigDecimal(ids.size()), 8, RoundingMode.HALF_UP);
            Map<Long,BigDecimal> cacheAmount5 =new HashMap<>();
            cacheAmount5.putAll(ids.stream().collect(Collectors.toMap(id->id, id->perAmount)));
            cacheAmount5.forEach((k, v) -> cacheAmount.merge(k, v, BigDecimal::add));
        }
        //cacheAmount 中重复的key的值相加合并
        executor.execute(() -> {
            assignLevelReward(cacheAmount, bizId);
        });
    }

    @Override
    public PayWalletTransactionDO getRecordByBizIdAndUserType(Long bid, Integer type) {
        return   payWalletTransactionMapper.selectOne(new LambdaUpdateWrapper<PayWalletTransactionDO>()
                .eq(PayWalletTransactionDO::getBizId, bid)
                .eq(PayWalletTransactionDO::getBizType, type)
                .eq(PayWalletTransactionDO::getDeleted, 0));
    }

    private void lockAllUnAssignReward(String bizId) {
        payWalletTransactionMapper.update(null, new LambdaUpdateWrapper<PayWalletTransactionDO>()
                .setSql("remark='"+bizId+"',deleted=1")
                .eq(PayWalletTransactionDO::getBizType, REWARD_POOL_INCOME.getType())
                .eq(PayWalletTransactionDO::getDeleted, 0));
    }

    public  void assignLevelReward(Map<Long, BigDecimal> rewards,String bizId) {
        payWalletService.addBatchWalletBalance(rewards, bizId,REWARD_INCOME);
    }


}
