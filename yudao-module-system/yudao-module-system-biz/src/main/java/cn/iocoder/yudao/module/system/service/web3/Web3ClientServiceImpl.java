package cn.iocoder.yudao.module.system.service.web3;

import cn.iocoder.yudao.module.system.util.web3.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.WEB3_ADDRESS_ERROR;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.WEB3_SIGNATURE_ERROR;

/**
 * 社交应用 Service 实现类
 */
@Service
@Slf4j
public class Web3ClientServiceImpl implements Web3ClientService {
    private static final String signText = "Welcome to Scorpio!\n\nClick to sign in and accept the Scorpio Terms of Service \n\nYour authentication status will reset after 1 days.\n\nWallet address:\n%s";

    @Override
    public String validateAuthUser(Integer userType, String address, String signature) {
        if (!WalletUtils.isValidAddress(address)) {
            // 不合法直接返回错误
            throw exception(WEB3_ADDRESS_ERROR);
        }
        // 校验签名信息
        if (!CryptoUtils.validate(signature, String.format(signText, address.toLowerCase()), address)) {
            throw exception(WEB3_SIGNATURE_ERROR);
        }
        return address;
    }
}
