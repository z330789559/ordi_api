package cn.iocoder.yudao.module.pay.job.withdraw;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RechargeScanJob
 *
 * @author libaozhong
 * @version 2024-01-27
 **/

@Component
@EnableScheduling
public class RechargeScanJob  implements JobHandler {

  @Resource
    private PayWalletRechargeService rechargeService;

  @Override
  public String execute(String param) throws Exception {
    return null;
  }

  @Scheduled(cron = "0/10 * * * * ?") // 每隔10秒执行一次
  public void executeTask(){
    rechargeService.scanRechargeOrder();
  }
}
