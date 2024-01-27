package cn.iocoder.yudao.module.member.service.wallet;

import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * 用户分组 Service 接口
 *
 * @author owen
 */
public interface MemberWalletService {

    /**
     * 获得用户分组列表
     *
     * @param id 编号
     * @return 用户分组列表
     */
    List<PayWalletRespDTO> getWalletListByUserIds(Collection<Long> id, Integer type);

}
