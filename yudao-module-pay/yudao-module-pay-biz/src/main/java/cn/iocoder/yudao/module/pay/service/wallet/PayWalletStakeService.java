package cn.iocoder.yudao.module.pay.service.wallet;

import java.util.List;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakePageReqVo;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeRequestDto;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeResponseVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;

/**
 * PayWalletStakeService
 *
 * @author libaozhong
 * @version 2024-02-27
 **/
public interface PayWalletStakeService {
	AppPayWalletStakeResponseVO createStake(AppPayWalletStakeRequestDto requestDto);

	Boolean unstake(Long id);


	PageResult<AppPayWalletStakeVo> getStakes(Long loginUserId, AppPayWalletStakePageReqVo pageReqVo);

	AppPayWalletStakeVo getMyStake(Long loginUserId);

	void assiginStakeReward();
}
