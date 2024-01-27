package cn.iocoder.yudao.module.admin.service.web3;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.admin.api.web3.dto.Web3UserRespDTO;
import cn.iocoder.yudao.module.admin.dal.dataobject.web3.Web3UserDO;
import cn.iocoder.yudao.module.admin.dal.mysql.web3.Web3UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 社交用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class Web3UserServiceImpl implements Web3UserService {

    @Resource
    private Web3UserMapper web3UserMapper;

    @Resource
    private Web3ClientService web3ClientService;

    @Override
    public Web3UserRespDTO getWeb3User(Integer userType, String address, String sign) {
        // 获得社交用户
        Web3UserDO web3User = authWeb3User(userType, address, sign);
        Assert.notNull(web3User, "社交用户不能为空");

        return new Web3UserRespDTO(web3User.getId(), web3User.getAddress());
    }

    /**
     * 授权获得对应的社交用户
     * 如果授权失败，则会抛出 {@link ServiceException} 异常
     *
     * @param userType 用户类型
     * @param address  Address
     * @return 授权用户
     */
    @NotNull
    public Web3UserDO authWeb3User(Integer userType, String address, String sign) {
        // 1. 校验签名
        String addr = web3ClientService.validateAuthUser(userType, address, sign);

        // 2. 从DB中获取
        Web3UserDO web3User = web3UserMapper.selectByAddress(addr);
        if (web3User != null) {
            return web3User;
        }

        if (web3User == null) {
            web3User = new Web3UserDO();
        }
        // 3. 保存到DB
        web3User.setAddress(addr);
        if (web3User.getId() == null) {
            web3UserMapper.insert(web3User);
        } else {
            web3UserMapper.updateById(web3User);
        }
        return web3User;
    }

    @Override
    public Web3UserDO getWeb3UserById(Long id) {
        return web3UserMapper.selectById(id);
    }
}
