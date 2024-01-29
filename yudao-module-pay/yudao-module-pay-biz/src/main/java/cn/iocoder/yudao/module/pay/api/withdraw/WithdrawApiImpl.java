package cn.iocoder.yudao.module.pay.api.withdraw;

import javax.annotation.Resource;

import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;
import cn.iocoder.yudao.module.pay.service.withdraw.WithdrawService;

import org.springframework.stereotype.Service;

/**
 * WithdrawApiImpl
 *
 * @author libaozhong
 * @version 2024-01-29
 **/

@Service
public class WithdrawApiImpl implements  WithDrawApi{

  @Resource
  WithdrawService withdrawService;

  @Override
  public void aduitWithdraw(Long id, WithdrawStatusEnum status, String remark) {
    withdrawService.aduitWithdraw(id, status, remark);
  }
}
