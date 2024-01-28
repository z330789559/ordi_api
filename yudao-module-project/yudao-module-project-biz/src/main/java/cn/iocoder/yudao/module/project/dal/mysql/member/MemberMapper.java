package cn.iocoder.yudao.module.project.dal.mysql.member;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.member.vo.*;

/**
 * 会员用户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberMapper extends BaseMapperX<MemberDO> {

    default PageResult<MemberDO> selectPage(MemberPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberDO>()
                .eqIfPresent(MemberDO::getParentId, reqVO.getParentId())
                .eqIfPresent(MemberDO::getMobile, reqVO.getMobile())
                .eqIfPresent(MemberDO::getParentAddress, reqVO.getParentAddress())
                .eqIfPresent(MemberDO::getAddress, reqVO.getAddress())
                .eqIfPresent(MemberDO::getPassword, reqVO.getPassword())
                .eqIfPresent(MemberDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MemberDO::getRegisterIp, reqVO.getRegisterIp())
                .eqIfPresent(MemberDO::getRegisterTerminal, reqVO.getRegisterTerminal())
                .eqIfPresent(MemberDO::getLoginIp, reqVO.getLoginIp())
                .betweenIfPresent(MemberDO::getLoginDate, reqVO.getLoginDate())
                .likeIfPresent(MemberDO::getNickname, reqVO.getNickname())
                .eqIfPresent(MemberDO::getAvatar, reqVO.getAvatar())
                .likeIfPresent(MemberDO::getName, reqVO.getName())
                .eqIfPresent(MemberDO::getSex, reqVO.getSex())
                .eqIfPresent(MemberDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(MemberDO::getBirthday, reqVO.getBirthday())
                .eqIfPresent(MemberDO::getMark, reqVO.getMark())
                .eqIfPresent(MemberDO::getPoint, reqVO.getPoint())
                .eqIfPresent(MemberDO::getTagIds, reqVO.getTagIds())
                .eqIfPresent(MemberDO::getLevelId, reqVO.getLevelId())
                .eqIfPresent(MemberDO::getExperience, reqVO.getExperience())
                .eqIfPresent(MemberDO::getGroupId, reqVO.getGroupId())
                .eqIfPresent(MemberDO::getInvitationCode, reqVO.getInvitationCode())
                .betweenIfPresent(MemberDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(MemberDO::getInvitationTime, reqVO.getInvitationTime())
                .eqIfPresent(MemberDO::getTree, reqVO.getTree())
                .eqIfPresent(MemberDO::getLevel, reqVO.getLevel())
                .orderByDesc(MemberDO::getId));
    }

}