package cn.iocoder.yudao.module.pay.convert.wallet;

import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayWalletConvert {

    PayWalletConvert INSTANCE = Mappers.getMapper(PayWalletConvert.class);

    PayWalletRespDTO covert03(PayWalletDO bean);

    List<PayWalletRespDTO> convertList(List<PayWalletDO> list);

    AppPayWalletRespVO convert(PayWalletDO bean);

}
