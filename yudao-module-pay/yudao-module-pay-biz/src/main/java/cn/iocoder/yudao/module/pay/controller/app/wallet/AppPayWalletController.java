package cn.iocoder.yudao.module.pay.controller.app.wallet;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletTransactionService;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenListReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @author jason
 */
@Tag(name = "用户 APP - 钱包")
@RestController
@RequestMapping("/pay/wallet")
@Validated
@Slf4j
public class AppPayWalletController {

    @Resource
    private PayWalletService payWalletService;

    @Resource
    private TokenService tokenService;

    @Resource
    private PayWalletTransactionService transactionService;

    @GetMapping("/get")
    @Operation(summary = "获取钱包")
    @PreAuthenticated
    public CommonResult<AppPayWalletRespVO> getPayWallet() {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(getLoginUserId(), UserTypeEnum.BTC.getValue());
        PayWalletDO nubtWallet = payWalletService.getOrCreateWallet(getLoginUserId(), UserTypeEnum.NUBT.getValue());

        wallet.setBtcBalance(wallet.getBalance());
        wallet.setNubtBalance(nubtWallet.getBalance());
        wallet.setBalance(BigDecimal.ZERO);

        List<TokenDO> tokenList = tokenService.getEnableTokenList(new TokenListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
        for (TokenDO token : tokenList) {
            if (token.getId() == 2L) {
                BigDecimal btcValue = wallet.getBtcBalance().multiply(token.getMarketCap());
                wallet.setBtcBalanceValue(btcValue);
                wallet.setBalance(wallet.getBalance().add(btcValue));
            }
            if (token.getId() == 1L) {
                BigDecimal nubtValue = wallet.getNubtBalance().multiply(token.getMarketCap());
                wallet.setNubtBalanceValue(nubtValue);
                wallet.setBalance(wallet.getBalance().add(nubtValue));
            }
        }

        AppPayWalletRespVO convert = PayWalletConvert.INSTANCE.convert(wallet);

        // 获取今日收入手续费
        AppPayWalletIncomeSummaryRespVO incomeSummary = transactionService.getIncomeSummary(7, getLoginUserId());
        if(incomeSummary != null) {
            convert.setTotalIncomeGas(incomeSummary.getTotalAmount());
            convert.setTodayIncomeGas(incomeSummary.getTodayAmount());
        }

        return success(convert);
    }

}
