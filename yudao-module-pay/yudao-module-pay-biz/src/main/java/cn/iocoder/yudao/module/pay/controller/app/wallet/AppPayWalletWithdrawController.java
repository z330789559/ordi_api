package cn.iocoder.yudao.module.pay.controller.app.wallet;

import cn.iocoder.yudao.framework.common.pojo.CommonOtherResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.web3j.EthUtils;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.TokenType;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import cn.iocoder.yudao.module.system.dal.dataobject.web3.Web3UserDO;
import cn.iocoder.yudao.module.system.service.web3.Web3UserService;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.withdraw.AppWithdrawCreateReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletWithdrawD0;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import cn.iocoder.yudao.module.pay.service.withdraw.WithdrawService;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;


import static cn.iocoder.yudao.framework.common.util.web3j.EthUtils.treasuryAccount;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-withdraw")
@Validated
@Slf4j
public class AppPayWalletWithdrawController {


    @Resource
    private TokenService tokenService;

    @Resource
    private WithdrawService brokerageWithdrawService;

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private Web3UserService web3UserService;


    @Value("${contract.out}")
    private String outContract;
    @Value("${contract.outKey}")
    private String outPrivateKey;

    private BigDecimal maxSubmitPrice = new BigDecimal("3000000000");

    @PostMapping("/create")
    @Operation(summary = "创建提现")
    @PreAuthenticated
    public CommonOtherResult<T> createBrokerageWithdraw(@RequestBody @Valid AppWithdrawCreateReqVO createReqVO) {

        Long uid = getLoginUserId();
        MemberUserDO user = memberUserService.getUser(getLoginUserId());
        Web3UserDO web3User = web3UserService.getWeb3UserById(Long.valueOf(user.getMobile()));

        TokenDO eth = tokenService.getToken(3L);
        TokenDO Btc = tokenService.getToken(2L);

        BigDecimal balance = createReqVO.getPrice().multiply(Btc.getMarketCap()).divide(eth.getMarketCap(), 18, RoundingMode.HALF_UP);
        if(TokenType.BTC.getType().equals(createReqVO.getTokenType())) {
            BigDecimal hasBtc = EthUtils.getErc20Balance(outContract, EthUtils.ethContractAddress);
            BigDecimal addressHasBtc = EthUtils.getErc20Balance(treasuryAccount, EthUtils.ethContractAddress);
            if ((hasBtc == null || hasBtc.compareTo(balance) < 0) && (addressHasBtc==null || addressHasBtc.compareTo(balance) < 0)) {
                return CommonOtherResult.error(1001,"链上拥堵");
            }
            BigDecimal usdtPrice = createReqVO.getPrice().multiply(Btc.getMarketCap());
            if(usdtPrice.compareTo(maxSubmitPrice) > 0 ) {
                return CommonOtherResult.error(1002,"提现金额超过最大限制");
            }
        }

        PayWalletWithdrawD0 withdraw = brokerageWithdrawService.createWithdraw(uid, createReqVO, balance);

        JSONObject jsonObject = EthUtils.cashOut(withdraw.getId(), withdraw.getPrice(), web3User.getAddress(), outContract, outPrivateKey);

        return CommonOtherResult.success(jsonObject,null);
    }

}
