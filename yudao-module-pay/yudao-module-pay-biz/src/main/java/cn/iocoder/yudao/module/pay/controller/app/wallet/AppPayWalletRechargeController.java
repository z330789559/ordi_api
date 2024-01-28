package cn.iocoder.yudao.module.pay.controller.app.wallet;

import cn.iocoder.yudao.framework.common.pojo.CommonOtherResult;
import cn.iocoder.yudao.framework.common.util.web3j.EthUtils;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import cn.iocoder.yudao.module.system.dal.dataobject.web3.Web3UserDO;
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
import cn.iocoder.yudao.module.system.service.web3.Web3UserService;


import javax.annotation.Resource;
import javax.validation.Valid;

import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserType;

@Tag(name = "用户 APP - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-recharge")
@Validated
@Slf4j
public class AppPayWalletRechargeController {

    @Resource
    private PayWalletRechargeService walletRechargeService;

    @Resource
    private Web3UserService web3UserService;

    @Resource
    private MemberUserService memberUserService;

    @Value("${contract.in}")
    private String inContract;


    @PostMapping("/create")
    @Operation(summary = "创建钱包充值记录（发起充值）")
    @PreAuthenticated
    public CommonOtherResult<T> createWalletRecharge(
            @Valid @RequestBody AppPayWalletRechargeCreateReqVO reqVO) {
        MemberUserDO user = memberUserService.getUser(getLoginUserId());
        Web3UserDO web3User = web3UserService.getWeb3UserById(Long.valueOf(user.getMobile()));
        BigDecimal allowanceUsdtNum = EthUtils.getErc20Allowance(web3User.getAddress(), EthUtils.btcContractAddress, inContract);
        BigDecimal payPrice = reqVO.getPayPrice();
        // 查询授权额度
        if (allowanceUsdtNum.compareTo(payPrice) < 0) {
            // 授权
            JSONObject allowanceTransaction = EthUtils.approveParam(web3User.getAddress(), inContract, EthUtils.btcContractAddress);
            return CommonOtherResult.success(null, allowanceTransaction);
        }

        PayWalletRechargeDO walletRecharge = walletRechargeService.createWalletRecharge(
                getLoginUserId(), PayWalletUserTypeEnum.FINANCE.getType(), getClientIP(), reqVO);

        // 手续费
//        BigDecimal servicefee = payPrice.multiply(BigDecimal.valueOf(0.02));

        JSONObject payTransaction = EthUtils.getMoneyParam(web3User.getAddress(), String.valueOf(reqVO.getPayPrice()), walletRecharge.getId(), inContract);

        return CommonOtherResult.success(payTransaction,null);
    }

}
