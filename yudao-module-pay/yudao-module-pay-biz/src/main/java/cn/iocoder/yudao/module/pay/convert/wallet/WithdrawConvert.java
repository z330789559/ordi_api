package cn.iocoder.yudao.module.pay.convert.wallet;

import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.withdraw.AppWithdrawCreateReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletWithdrawD0;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

/**
 * 佣金提现 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface WithdrawConvert {

    WithdrawConvert INSTANCE = Mappers.getMapper(WithdrawConvert.class);

    PayWalletWithdrawD0 convert(AppWithdrawCreateReqVO createReqVO, Long userId, BigDecimal feePrice);

}
