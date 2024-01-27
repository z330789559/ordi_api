package cn.iocoder.yudao.module.admin.api.web3;

import cn.iocoder.yudao.module.admin.api.web3.dto.Web3UserRespDTO;
import cn.iocoder.yudao.module.admin.service.web3.Web3UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * web3用户的 API 实现类
 */
@Service
@Validated
public class Web3UserApiImpl implements Web3UserApi {
    @Resource
    private Web3UserService web3UserService;

    @Override
    public Web3UserRespDTO getWeb3User(Integer userType, String address, String sign) {
       return web3UserService.getWeb3User(userType, address, sign);
    }
}