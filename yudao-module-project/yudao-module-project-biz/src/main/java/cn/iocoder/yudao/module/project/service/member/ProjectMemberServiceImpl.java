package cn.iocoder.yudao.module.project.service.member;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.member.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.member.MemberMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 会员用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProjectMemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Override
    public Long createMember(MemberSaveReqVO createReqVO) {
        // 插入
        MemberDO member = BeanUtils.toBean(createReqVO, MemberDO.class);
        memberMapper.insert(member);
        // 返回
        return member.getId();
    }

    @Override
    public void updateMember(MemberSaveReqVO updateReqVO) {
        // 校验存在
        validateMemberExists(updateReqVO.getId());
        // 更新
        MemberDO updateObj = BeanUtils.toBean(updateReqVO, MemberDO.class);
        memberMapper.updateById(updateObj);
    }

    @Override
    public void deleteMember(Long id) {
        // 校验存在
        validateMemberExists(id);
        // 删除
        memberMapper.deleteById(id);
    }

    private void validateMemberExists(Long id) {
        if (memberMapper.selectById(id) == null) {
            throw exception(MEMBER_NOT_EXISTS);
        }
    }

    @Override
    public MemberDO getMember(Long id) {
        return memberMapper.selectById(id);
    }

    @Override
    public PageResult<MemberDO> getMemberPage(MemberPageReqVO pageReqVO) {
        return memberMapper.selectPage(pageReqVO);
    }

}