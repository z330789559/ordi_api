package cn.iocoder.yudao.module.pay.service.withdraw;

import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.withdraw.AppWithdrawCreateReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletWithdrawD0;
import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;

public interface WithdrawService {
    /**
     * 【会员】创建提现
     *
     * @param userId      会员用户编号
     * @param createReqVO 创建信息
     */
    PayWalletWithdrawD0 createWithdraw(Long userId, AppWithdrawCreateReqVO createReqVO);


    void scanWithdraw();
}
