package cn.iocoder.yudao.module.member.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.member.controller.app.user.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.member.util.InvitationCodeGenerator;
import cn.iocoder.yudao.module.pay.api.wallet.PayWalletApi;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum.FINANCE;

/**
 * 会员 User Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Valid
@Slf4j
public class MemberUserServiceImpl implements MemberUserService {

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private PayWalletApi payWalletApi;

    @Override
    public MemberUserDO getUserByMobile(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public MemberUserDO getUserByAddress(String address) {
        return memberUserMapper.selectByAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberUserDO createUserIfAbsent(String mobile, String address, String registerIp, Integer terminal) {
        // 用户已经存在
        MemberUserDO user = memberUserMapper.selectByMobile(mobile);
        if (user != null) {
            return user;
        }
        // 用户不存在，则进行创建
        return createUser(mobile, address, registerIp, terminal);
    }

    private MemberUserDO createUser(String mobile, String address, String registerIp, Integer terminal) {

        // 生成密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        MemberUserDO user = new MemberUserDO();
        user.setMobile(mobile);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setRegisterIp(registerIp);
        user.setRegisterTerminal(terminal);
        user.setAddress(address);
        memberUserMapper.insert(user);

        String invitationCodde = InvitationCodeGenerator.toSerialCode(user.getId());
        user.setInvitationCode(invitationCodde);
        // 更新邀请码
        memberUserMapper.updateById(new MemberUserDO().setId(user.getId()).setInvitationCode(invitationCodde));

        return user;
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        memberUserMapper.updateById(new MemberUserDO().setId(id)
                .setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public MemberUserDO getUser(Long id) {
        return memberUserMapper.selectById(id);
    }

    @Override
    public List<MemberUserDO> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return memberUserMapper.selectBatchIds(ids);
    }

    @Override
    public void updateBind(Long userId, String invitationCode) {
        MemberUserDO user = memberUserMapper.selectById(userId);
        if (!user.getParentId().equals(0L)) {
            throw exception(USER_BIND_NOT_REPEAT);
        }

        // 根据邀请码获取用户
        MemberUserDO parent = memberUserMapper.selectByInvitationCode(invitationCode);
        if (parent == null) {
            throw exception(USER_BIND_NOT_EXISTS);
        }
        if (parent.getId().equals(userId)) {
            throw exception(USER_BIND_NOT_SELF);
        }
        if (parent.getParentId().equals(userId)) {
            throw exception(USER_BIND_NOT_FATHER);
        }
        if (parent.getParentId().equals(0L)){
            throw exception(USER_BIND_FATHER_NOT_BIND);
        }

        memberUserMapper.updateById(new MemberUserDO().setId(userId)
                .setParentId(parent.getId())
                .setParentAddress(parent.getAddress())
                .setInvitationTime(LocalDateTime.now())
                .setTree(parent.getTree() + "/" + parent.getInvitationCode())
        );
    }

    @Override
    public List<MemberUserDO> getUserPageRecursion(AppMemberUserPageReqVO pageReqVO) {
        List<MemberUserDO> users = new ArrayList<>();
        recursiveFindChildren(users, pageReqVO.getParentId());
        List<Long> ids = new ArrayList<>();
        users.forEach(user -> {
            ids.add(user.getId());
        });

        // 获取用户余额
        List<PayWalletRespDTO> wallets = payWalletApi.getWalletListByUserIds(ids, 1);
        Map<Long, PayWalletRespDTO> walletMap = CollectionUtils.convertMap(wallets, PayWalletRespDTO::getUserId);

        users.forEach(user -> {
            user.setBalance(BigDecimal.ZERO);
            PayWalletRespDTO wallet = walletMap.get(user.getId());
            if (wallet != null) {
                user.setBalance(wallet.getBalance());
            }
        });
        return users;
    }

    private void recursiveFindChildren(List<MemberUserDO> users, long parentId) {
        List<MemberUserDO> children = memberUserMapper.selectListByParentId(parentId);
        if (!children.isEmpty()) {
            for (MemberUserDO child : children) {
                users.add(child);

                // 再次递归查询当前子节点的子节点
                recursiveFindChildren(users, child.getId());
            }
        }
    }

    @Override
    public PageResult<MemberUserDO> getUserPage(AppMemberUserPageReqVO pageReqVO) {
        return memberUserMapper.selectPage(pageReqVO);
    }

    @Override
    public Integer getDirectInvitedNum(Long loginUserId) {
        return  memberUserMapper.selectCountByParentId(loginUserId);
    }

    @Override
    public void bindBrcAddress(Long loginUserId, String brcAddress) {
          MemberUserDO user = memberUserMapper.selectById(loginUserId);
            if (user == null) {
                throw exception(USER_NOT_EXISTS);
            }
            if (StringUtils.hasText(user.getBrcAddress()) ) {
                throw exception(USER_BIND_BRC_ADDRESS_NOT_REPEAT);
            }
            memberUserMapper.updateById(new MemberUserDO().setId(loginUserId).setBrcAddress(brcAddress));
    }

    @Override
    public List<Long> getDirectInvitedUserId(Long userId) {
        List<MemberUserDO> children = memberUserMapper.selectListByParentId(userId);
        return children.stream().map(MemberUserDO::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getMemberUserWalletListByLevel(Integer level1) {
        return  memberUserMapper.selectWalletIdByLevel(level1,FINANCE.getType());
    }

}
