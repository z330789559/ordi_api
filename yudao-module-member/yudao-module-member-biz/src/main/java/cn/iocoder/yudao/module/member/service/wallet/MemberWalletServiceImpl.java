package cn.iocoder.yudao.module.member.service.wallet;


import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户分组 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class MemberWalletServiceImpl implements MemberWalletService {

    @Resource
    private PayWalletApi payWalletApi;

    @Override
    public List<PayWalletRespDTO> getWalletListByUserIds(Collection<Long> id, Integer type) {
        // 获取用户余额
        return payWalletApi.getWalletListByUserIds(id, type);
    }

}
