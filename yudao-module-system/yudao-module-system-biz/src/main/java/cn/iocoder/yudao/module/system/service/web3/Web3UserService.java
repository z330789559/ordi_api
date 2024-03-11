package cn.iocoder.yudao.module.system.service.web3;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.api.web3.dto.Web3UserRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.web3.Web3UserDO;

/**
 * 社交用户 Service 接口，例如说社交平台的授权登录
 *
 * @author 芋道源码
 */
public interface Web3UserService {

    /**
     * 获得社交用户
     *
     * 在认证信息不正确的情况下，也会抛出 {@link ServiceException} 业务异常
     *
     * @param userType 用户类型
     * @param address 地址
     * @param sign 签名
     * @return 社交用户
     */
    Web3UserRespDTO getWeb3User(Integer userType, String address, String sign);

    Web3UserDO getWeb3UserById(Long id);

    Web3UserRespDTO getWeb3UserByAddress(String address);
}
