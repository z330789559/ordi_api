package cn.iocoder.yudao.module.member.service.auth;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.member.convert.auth.AuthConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.member.util.InvitationCodeGenerator;
import cn.iocoder.yudao.module.admin.api.oauth2.OAuth2TokenApi;
import cn.iocoder.yudao.module.admin.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.module.admin.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.module.admin.api.web3.Web3UserApi;
import cn.iocoder.yudao.module.admin.api.web3.dto.Web3UserRespDTO;
import cn.iocoder.yudao.module.admin.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.admin.enums.oauth2.OAuth2ClientConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 会员的认证 Service 接口
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class MemberAuthServiceImpl implements MemberAuthService {

    @Resource
    private MemberUserService userService;
    @Resource
    private Web3UserApi web3UserApi;
    @Resource
    private OAuth2TokenApi oauth2TokenApi;
    @Resource
    private MemberUserMapper memberUserMapper;

    @Override
    @Transactional
    public AppAuthLoginRespVO web3Login(AppAuthWeb3LoginReqVO reqVO) {
        String userIp = getClientIP();

        // 使用 code 授权码，进行登录。然后，获得到绑定的用户编号
        Web3UserRespDTO web3User = web3UserApi.getWeb3User(UserTypeEnum.MEMBER.getValue(),
                reqVO.getAddress(), reqVO.getSignature());

        // 获得获得注册用户
        MemberUserDO user = userService.createUserIfAbsent(String.valueOf(web3User.getId()), web3User.getAddress(), userIp, 1);
        Assert.notNull(user, "获取用户失败，结果为空");

        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 判断用户是否已经有邀请码
        if (user.getInvitationCode().length() < 1) {
            // 更新邀请码
            memberUserMapper.updateById(new MemberUserDO().setId(user.getId())
                    .setInvitationCode(InvitationCodeGenerator.toSerialCode(user.getId())));
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, user.getMobile(), LoginLogTypeEnum.LOGIN_SOCIAL, web3User.getAddress());
    }

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(MemberUserDO user, String mobile,
                                                            LoginLogTypeEnum logType, String address) {
        // 创建 Token 令牌
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(user.getId()).setUserType(getUserType().getValue())
                .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenRespDTO, address);
    }

    @Override
    public void logout(String token) {
        // 删除访问令牌
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oauth2TokenApi.removeAccessToken(token);
        if (accessTokenRespDTO == null) {
            return;
        }
    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenRespDTO accessTokenDO = oauth2TokenApi.refreshAccessToken(refreshToken,
                OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AuthConvert.INSTANCE.convert(accessTokenDO, null);
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }

}
