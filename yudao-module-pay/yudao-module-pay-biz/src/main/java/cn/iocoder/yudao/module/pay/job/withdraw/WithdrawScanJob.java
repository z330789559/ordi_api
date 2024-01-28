package cn.iocoder.yudao.module.pay.job.withdraw;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import cn.iocoder.yudao.module.pay.service.withdraw.WithdrawService;
import cn.iocoder.yudao.module.product.service.token.TokenService;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
public class WithdrawScanJob implements JobHandler {
    @Resource
    private WithdrawService withdrawService;

    @Resource
    private TokenService tokenService;

    // 不好用。。。。。同一时间会执行多次
    @Override
    @TenantJob
    public String execute(String param) {
//        withdrawService.scanWithdraw();
        return null;
    }

    @Scheduled(cron = "0/10 * * * * ?") // 每隔10秒执行一次
    public void executeTask(){
        withdrawService.scanWithdraw();
    }



    @Scheduled(cron = "0/10 * * * * ?") // 每隔10秒执行一次
    public void scanBtc(){
        tokenService.scanChainTokenPrice();
    }

    @Scheduled(cron = "0/10 * * * * ?") // 每隔10秒执行一次
    public void scanNub(){
        tokenService.scanTokenPrice();
    }

}
