package cn.iocoder.yudao.module.pay.controller.app.wallet;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakePageReqVo;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeRequestDto;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeResponseVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletStakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * AppPayWalletStakeController
 *
 * @author libaozhong
 * @version 2024-02-27
 **/
@Tag(name = "用户 APP - 钱包质押")
@RestController
@RequestMapping("/pay/wallet-stake")
@Validated
@Slf4j
public class AppPayWalletStakeController {

    @Resource
   public PayWalletStakeService payWalletStakeService;


    @PostMapping("/create")
    @Operation(summary = "创建质押")
    @PreAuthenticated
    public CommonResult<AppPayWalletStakeResponseVO> createStake( @Valid @RequestBody AppPayWalletStakeRequestDto requestDto){

        return CommonResult.success(payWalletStakeService.createStake(requestDto));
    }



    @PutMapping("/unstake/{id}")
    @Operation(summary = "解质押")
    @PreAuthenticated
    public CommonResult<Boolean> unstake( @Valid @PathVariable("id") Long id){

        return CommonResult.success(payWalletStakeService.unstake(id));
    }


    @GetMapping("/page")
    @Operation(summary = "查询质押")
    @PreAuthenticated
    public CommonResult<PageResult<AppPayWalletStakeVo>> getStakePage(@Valid AppPayWalletStakePageReqVo pageReqVo){

        return CommonResult.success(payWalletStakeService.getStakes(getLoginUserId(),pageReqVo));
    }

}
