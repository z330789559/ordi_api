package cn.iocoder.yudao.module.pay.convert.wallet;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * PayWalletStakeConvert
 *
 * @author libaozhong
 * @version 2024-02-27
 **/


@Mapper
public interface PayWalletStakeConvert {

    PayWalletStakeConvert instance = Mappers.getMapper(PayWalletStakeConvert.class);

     AppPayWalletStakeVo fromDo(PayWalletStakeDO payWalletStakeDO);

    PayWalletStakeDO toDo(AppPayWalletStakeVo appPayWalletStakeVo);

  PageResult<AppPayWalletStakeVo> convertPage(PageResult<PayWalletStakeDO> stakesByPage);
}
