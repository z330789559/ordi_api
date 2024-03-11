package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletIncomeSummaryRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.UnAssignRewardSummaryDo;
import cn.iocoder.yudao.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;

import javax.validation.Valid;

/**
 * 钱包余额流水 Service 接口
 *
 * @author jason
 */
public interface PayWalletTransactionService {

    PageResult<PayWalletTransactionDO> getWalletTransactionPage(Long userId, Integer userType,
                                                                AppPayWalletTransactionPageReqVO pageVO);

    PageResult<PayWalletTransactionDO> getWalletTransactionAllPage(AppPayWalletTransactionPageReqVO pageVO);

    PayWalletTransactionDO createWalletTransaction(@Valid WalletTransactionCreateReqBO bo);

    AppPayWalletIncomeSummaryRespVO getIncomeSummary(Integer bizType, Long userId);

    UnAssignRewardSummaryDo getUnAssignRewardSummary();

    void assiginReward();

    PayWalletTransactionDO getRecordByBizIdAndUserType(Long bid, Integer type);
}
