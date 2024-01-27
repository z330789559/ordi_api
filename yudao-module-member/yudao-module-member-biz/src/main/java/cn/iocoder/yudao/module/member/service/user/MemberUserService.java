package cn.iocoder.yudao.module.member.service.user;

import cn.iocoder.yudao.framework.common.enums.TerminalEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.member.controller.app.user.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 会员用户 Service 接口
 *
 * @author 芋道源码
 */
public interface MemberUserService {

    /**
     * 通过手机查询用户
     *
     * @param mobile 手机
     * @return 用户对象
     */
    MemberUserDO getUserByMobile(String mobile);

    MemberUserDO getUserByAddress(String address);

    /**
     * 基于手机号创建用户。
     * 如果用户已经存在，则直接进行返回
     *
     * @param mobile     手机号
     * @param registerIp 注册 IP
     * @param terminal   终端 {@link TerminalEnum}
     * @return 用户对象
     */
    MemberUserDO createUserIfAbsent(@Mobile String mobile, String address, String registerIp, Integer terminal);

    /**
     * 更新用户的最后登陆信息
     *
     * @param id      用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long id, String loginIp);

    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    MemberUserDO getUser(Long id);


    /**
     * 通过用户 ID 查询用户们
     *
     * @param ids 用户 ID
     * @return 用户对象信息数组
     */
    List<MemberUserDO> getUserList(Collection<Long> ids);

    void updateBind(Long userId, String invitationCode);

    List<MemberUserDO> getUserPageRecursion(AppMemberUserPageReqVO pageReqVO);

    PageResult<MemberUserDO> getUserPage(AppMemberUserPageReqVO pageReqVO);

}
