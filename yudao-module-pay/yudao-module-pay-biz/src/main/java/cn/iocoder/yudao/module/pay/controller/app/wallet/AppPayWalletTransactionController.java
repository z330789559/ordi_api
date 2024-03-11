package cn.iocoder.yudao.module.pay.controller.app.wallet;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionRespVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletTransactionConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.UnAssignRewardSummaryDo;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 钱包余额明细")
@RestController
@RequestMapping("/pay/wallet-transaction")
@Validated
@Slf4j
public class AppPayWalletTransactionController {

    @Resource
    private PayWalletTransactionService payWalletTransactionService;

    @GetMapping("/page")
    @Operation(summary = "获得钱包流水分页")
    public CommonResult<PageResult<AppPayWalletTransactionRespVO>> getWalletTransactionPage(
            @Valid AppPayWalletTransactionPageReqVO pageReqVO) {
        PageResult<PayWalletTransactionDO> result = payWalletTransactionService.getWalletTransactionPage(getLoginUserId(),
                UserTypeEnum.MEMBER.getValue(), pageReqVO);
        return success(PayWalletTransactionConvert.INSTANCE.convertPage(result));
    }

    @GetMapping("/all/page")
    @Operation(summary = "获得钱包流水分页")
    @PermitAll
    public CommonResult<PageResult<AppPayWalletTransactionRespVO>> getWalletTransactionAllPage(
            @Valid AppPayWalletTransactionPageReqVO pageReqVO) {
        PageResult<PayWalletTransactionDO> result = payWalletTransactionService.getWalletTransactionAllPage(pageReqVO);
        return success(PayWalletTransactionConvert.INSTANCE.convertPage(result));
    }


    @GetMapping("/reward-summary")
    @Operation(summary = "奖金池金额")
    public CommonResult<AppPayWalletIncomeSummaryRespVO> getRewardSummary() {
        UnAssignRewardSummaryDo rewardPool= payWalletTransactionService.getUnAssignRewardSummary();
        AppPayWalletIncomeSummaryRespVO respVO = new AppPayWalletIncomeSummaryRespVO();
        respVO.setTodayAmount(rewardPool.getTodayAmount());
        respVO.setTotalAmount(rewardPool.getTotalAmount());
        respVO.setUpLimit(rewardPool.getUpLimit());
        return success(respVO);
    }
}
