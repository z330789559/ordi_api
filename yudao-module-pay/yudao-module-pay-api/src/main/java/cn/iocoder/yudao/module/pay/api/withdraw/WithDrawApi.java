package cn.iocoder.yudao.module.pay.api.withdraw;

import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;

/**
 * WithDrawAoi
 *
 * @author libaozhong
 * @version 2024-01-29
 **/
public interface WithDrawApi {


	void aduitWithdraw(Long id, WithdrawStatusEnum status, String remark);

}
