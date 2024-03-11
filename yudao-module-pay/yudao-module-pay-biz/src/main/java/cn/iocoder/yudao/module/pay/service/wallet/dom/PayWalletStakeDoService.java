package cn.iocoder.yudao.module.pay.service.wallet.dom;

import java.util.List;
import java.util.Optional;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakePageReqVo;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;

/**
 * PayWalletStakeDoService
 *
 * @author libaozhong
 * @version 2024-02-27
 **/
public interface PayWalletStakeDoService {
    int createStake(PayWalletStakeDO stake);

	PayWalletStakeDO getStake(Long id);

	void updateStake(PayWalletStakeDO stake);

	PageResult<PayWalletStakeDO> getStakesByPage(Long loginUserId,Integer userType, PageParam pageReqVo);

	PayWalletStakeDO getStakes(Long loginUserId);

	Optional<List<PayWalletStakeDO>> getCanRewardStakes();

	void updateStakes(List<PayWalletStakeDO> stakes);
}
