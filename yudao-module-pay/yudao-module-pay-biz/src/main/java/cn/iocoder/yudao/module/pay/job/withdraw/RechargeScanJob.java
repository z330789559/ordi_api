package cn.iocoder.yudao.module.pay.job.withdraw;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletStakeService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletTransactionService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RechargeScanJob
 *
 * @author libaozhong
 * @version 2024-01-27
 **/

@Slf4j
@Component
@EnableScheduling
public class RechargeScanJob  implements JobHandler {

  @Resource
    private PayWalletRechargeService rechargeService;
  @Resource
  private PayWalletTransactionService payWalletTransactionService;


  @Resource
  private PayWalletStakeService payWalletStakeService;

  @Override
  public String execute(String param) throws Exception {
    return null;
  }

  @Scheduled(cron = "0/10 * * * * ?") // 每隔10秒执行一次
  public void executeTask(){
    rechargeService.scanRechargeOrder();
  }



//  @Scheduled(cron = "0 */30 * * * ?") // 每30分钟执行一次
  public void dropReWard(){
    payWalletTransactionService.assiginReward();
  }



//每天早晨1点执行
   // @Scheduled(cron = "0 0 1 * * ?") // 每天早晨1点执行

 // 每天下午2点执行
 @Scheduled(cron = "0 01 13 * * ?")
  public void dropStakeReward(){
       log.info("开始分配质押奖励 {}", DateUtil.date());
    payWalletStakeService.assiginStakeReward();
  }
}
