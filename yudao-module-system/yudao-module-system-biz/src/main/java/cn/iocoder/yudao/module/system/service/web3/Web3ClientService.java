package cn.iocoder.yudao.module.system.service.web3;

/**
 * 社交应用 Service 接口
 *
 * @author 芋道源码
 */
public interface Web3ClientService {
    /**
     * 请求社交平台，获得授权的用户
     *
     * @param userType  用户类型
     * @param address   地址
     * @param signature 签名
     * @return 授权的用户
     */
    String validateAuthUser(Integer userType, String address, String signature);

}
