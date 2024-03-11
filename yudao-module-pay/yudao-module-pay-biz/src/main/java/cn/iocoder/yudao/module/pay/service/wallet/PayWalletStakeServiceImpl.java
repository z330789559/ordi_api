package cn.iocoder.yudao.module.pay.service.wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakePageReqVo;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeRequestDto;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeResponseVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletStakeConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.StakeStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.TokenType;
import cn.iocoder.yudao.module.pay.service.wallet.dom.PayWalletStakeDoService;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RedissonClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * PayWalletStakeServiceImpl
 *
 * @author libaozhong
 * @version 2024-02-27
 **/

@Service
public class PayWalletStakeServiceImpl implements PayWalletStakeService{

    private static final String STAKE_LOCK_KEY = "stake_lock_key";

  @Resource
  private PayWalletService payWalletService;

  @Resource
  private MemberUserService memberUserService;

    @Resource
  private TokenService tokenService;
  @Resource
  private PayWalletTransactionService walletTransactionService;

  @Resource
  private TransactionTemplate transactionTemplate ;


  @Resource
  private PayWalletStakeDoService payWalletStakeDoService;



    @Resource
    private PayNoRedisDAO noRedisDAO;




  /**
   * 创建质押
   * <p>
   *     1. validate user exist or not and get use record
   *     2. accord to user id and token type, get wallet id
   *     3. validate wallet balance
   *     4. deduct balance
   *     5. create transaction record
   *     6. create stake record
   *     7. return response
   *
   * @param requestDto
   * @return
   */
  @Override
  public AppPayWalletStakeResponseVO createStake(AppPayWalletStakeRequestDto requestDto) {
    MemberUserDO user = memberUserService.getUser(getLoginUserId());
    assert user != null;
    PayWalletDO wallet = payWalletService.getOrCreateWallet(getLoginUserId(), requestDto.getUserType().getType());
    assert wallet != null;
    // 3. validate wallet balance
    if (wallet.getBalance().compareTo(requestDto.getPrice()) < 0) {
      throw new IllegalArgumentException("余额不足");
    }

    PayWalletStakeDO stakeDo=transactionTemplate.execute(status -> {
      // 6. create stake record
         PayWalletStakeDO stake =new PayWalletStakeDO();
         stake.setAddress(user.getAddress());
         stake.setTotalPrice(requestDto.getPrice());
         stake.setPayPrice(requestDto.getPrice());
         stake.setWalletId(wallet.getId());
          stake.setUserType(requestDto.getUserType().getType());
         stake.setStakeStatus(StakeStatusEnum.Pay.getCode());
          stake.setPayTime(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
         stake.setRefundPayPrice(requestDto.getPrice());
         stake.setRefundTotalPrice(requestDto.getPrice());
          stake.setUserId(user.getId());
         payWalletStakeDoService.createStake(stake);
        // 4. deduct balance
         payWalletService.reduceWalletBalance(wallet.getId(), stake.getId(), PayWalletBizTypeEnum.STAKE, requestDto.getPrice());
         return stake;
    });
	  assert stakeDo != null;
	  return new AppPayWalletStakeResponseVO(stakeDo.getId());
  }

    /**
     * 解除质押
     * <p>
     *     1. validate stake exist or not
     *     2. validate stake status
     *     3. update stake status
     *     4. return wallet balance
     *     5. record transaction
     *     6. return response
     * </p>
     * @param id
     * @return
     */
    @Override
    public Boolean unstake(Long id) {
        PayWalletStakeDO stake = payWalletStakeDoService.getStake(id);
        int  days=noRedisDAO.getUnStakeLimit(STAKE_LOCK_KEY);

        assert stake != null;
        if( stake.getCreateTime().isAfter(DateUtil.offsetDay(DateUtil.date(), -days).toLocalDateTime())){
            throw new IllegalArgumentException("质押时间不足"+days+"天");
        };



        if (stake.getStakeStatus() != StakeStatusEnum.Pay.getCode()) {
            throw new IllegalArgumentException("质押状态不正确");
        }
//        computerBonusPrice(stake);
        stake.setStakeStatus(StakeStatusEnum.UnStake.getCode());
       return  transactionTemplate.execute(status -> {
            // 3. update stake status
            payWalletStakeDoService.updateStake(stake);
            // 4. return wallet balance

            payWalletService.addWalletBalance(stake.getWalletId(), String.valueOf(id), PayWalletBizTypeEnum.UNSTAKE, stake.getPayPrice());
            // 5. record transaction
            return true;
        });
    }


    @Override
    public PageResult<AppPayWalletStakeVo> getStakes(Long loginUserId, AppPayWalletStakePageReqVo pageReqVo) {


        return PayWalletStakeConvert.instance.convertPage(payWalletStakeDoService.getStakesByPage(loginUserId,pageReqVo.getUserType(), pageReqVo));
    }

    @Override
    public AppPayWalletStakeVo getMyStake(Long loginUserId) {
        PayWalletStakeDO  stakes = payWalletStakeDoService.getStakes(loginUserId);
        return  PayWalletStakeConvert.instance.fromDo(stakes);
    }

    @Override
    public void assiginStakeReward() {
        TenantContextHolder.setIgnore(true);
        TokenDO btcToken = tokenService.getToken(2L);
        assert btcToken != null;
        TokenDO bt2nToken = tokenService.getToken(1L);
        assert bt2nToken != null;
        payWalletStakeDoService.getCanRewardStakes().ifPresent(stakes -> {
             stakes.forEach(stake->computerBonusPrice(stake,btcToken.getMarketCap(),bt2nToken.getMarketCap()));
            payWalletStakeDoService.updateStakes(stakes);
            stakes.forEach(
					this::dropRewardToWallet
            );
        });
    }

    private void dropRewardToWallet(PayWalletStakeDO stake) {
        PayWalletDO financialWallet = payWalletService.getOrCreateWallet(stake.getUserId(), UserTypeEnum.BTC.getValue());
        payWalletService.addWalletBalance(financialWallet.getId(), String.valueOf(stake.getId()), PayWalletBizTypeEnum.UNSTAKE, stake.getRefundBonusPrice());
    }


    private BigDecimal getDatInterest( int level) {
      switch (level) {
                case 0:
                return new BigDecimal("0.005");
                case 1:
                    return new BigDecimal("0.0055");
                case 2:
                    return new BigDecimal("0.006");
                case 3:
                    return new BigDecimal("0.0065");
                case 4:
                    return new BigDecimal("0.007");
                case 5:
                    return new BigDecimal("0.0075");
                default:
                    return new BigDecimal("0");
            }
    }
    private void computerBonusPrice(PayWalletStakeDO stake,BigDecimal btcPrice,BigDecimal bt2nPrice){
        // 1. 计算赠送金额
        MemberUserDO user = memberUserService.getUser(stake.getUserId());
        assert user != null;
      //铭文数量*目前的地板价*日息/BTC的价格
        BigDecimal dayInterest = getDatInterest(user.getLevel());
        BigDecimal reward = stake.getTotalPrice().multiply(bt2nPrice).multiply(dayInterest)
                .divide(btcPrice, 10, RoundingMode.HALF_UP);
        stake.setRefundBonusPrice(reward);
        stake.setRefundTotalPrice(stake.getPayPrice().add(stake.getRefundBonusPrice()));

    }
}
