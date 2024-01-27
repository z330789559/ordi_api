package cn.iocoder.yudao.module.member.dal.mysql.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.app.user.vo.AppMemberUserPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 会员 User Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberUserMapper extends BaseMapperX<MemberUserDO> {

    default MemberUserDO selectByMobile(String mobile) {
        return selectOne(MemberUserDO::getMobile, mobile);
    }

    default MemberUserDO selectByAddress(String address) {
        return selectOne(MemberUserDO::getAddress, address);
    }

    default MemberUserDO selectByInvitationCode(String code) {
        return selectOne(MemberUserDO::getInvitationCode, code);
    }

    default List<MemberUserDO> selectListByParentId(Long id) {
        return selectList(new LambdaQueryWrapperX<MemberUserDO>()
                .eqIfPresent(MemberUserDO::getParentId, id));
    }

    default PageResult<MemberUserDO> selectPage(AppMemberUserPageReqVO reqVO) {
        // 分页查询
        PageResult<MemberUserDO> pageResult = selectPage(reqVO, new LambdaQueryWrapperX<MemberUserDO>()
                .eqIfPresent(MemberUserDO::getParentId, reqVO.getParentId())
                .orderByDesc(MemberUserDO::getId));

        for (MemberUserDO item : pageResult.getList()) {
            Integer teamMemberNum = selectTeamMemberNum(item.getInvitationCode());
            item.setTeamMemberNum(teamMemberNum);
            BigDecimal teamBalance = selectTeamBalance(item.getInvitationCode(), PayWalletUserTypeEnum.CIRCULATION.getType());
            item.setTeamBalance(teamBalance);
        }

        return pageResult;
    }

    @Select("select invitation_code from member_user where parent_id = #{parentId}")
    List<String> selectDirectCodeByParentId(@Param("parentId") Long parentId);


    @Select("select ifnull(sum(balance),0) from pay_wallet " +
            "where user_type = 1 " +
            "and user_id in (" +
            "   select id from member_user where tree like concat('%',#{code},'%') or invitation_code = #{code} " +
            ")")
    BigDecimal getCommunityAchievementByCode(@Param("code") String code);


    @Select("select * from member_user where invitation_code != '' and #{tree} like concat('%',invitation_code,'%')")
    List<MemberUserDO> selectParentListByCode(@Param("tree") String tree);

    @Select("select ifnull(sum(balance),0) from pay_wallet where user_type = #{userType} and user_id in (" +
            "select id from member_user where tree like concat('%',#{code},'%') or invitation_code = #{code}" +
            ")")
    BigDecimal selectTeamBalance(@Param("code") String code,@Param("userType") Integer userType);

    @Select("select count(id) from member_user where tree like concat('%',#{code},'%') or invitation_code = #{code}")
    Integer selectTeamMemberNum(@Param("code") String code);
}
