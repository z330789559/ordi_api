package cn.iocoder.yudao.module.member.service.auth;

import cn.iocoder.yudao.module.member.controller.app.auth.vo.*;

import javax.validation.Valid;

/**
 * 会员的认证 Service 接口
 *
 * 提供用户的账号密码登录、token 的校验等认证相关的功能
 *
 * @author 芋道源码
 */
public interface MemberAuthService {

    /**
     * 基于 token 退出登录
     *
     * @param token token
     */
    void logout(String token);

    /**
     * 社交登录，使用 code 授权码
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO web3Login(@Valid AppAuthWeb3LoginReqVO reqVO);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AppAuthLoginRespVO refreshToken(String refreshToken);

}
