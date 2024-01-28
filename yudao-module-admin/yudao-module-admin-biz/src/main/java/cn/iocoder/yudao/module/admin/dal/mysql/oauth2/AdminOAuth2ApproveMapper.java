package cn.iocoder.yudao.module.admin.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.dal.dataobject.oauth2.AdminOAuth2ApproveDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminOAuth2ApproveMapper extends BaseMapperX<AdminOAuth2ApproveDO> {

    default int update(AdminOAuth2ApproveDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<AdminOAuth2ApproveDO>()
                .eq(AdminOAuth2ApproveDO::getUserId, updateObj.getUserId())
                .eq(AdminOAuth2ApproveDO::getUserType, updateObj.getUserType())
                .eq(AdminOAuth2ApproveDO::getClientId, updateObj.getClientId())
                .eq(AdminOAuth2ApproveDO::getScope, updateObj.getScope()));
    }

    default List<AdminOAuth2ApproveDO> selectListByUserIdAndUserTypeAndClientId(Long userId, Integer userType, String clientId) {
        return selectList(new LambdaQueryWrapperX<AdminOAuth2ApproveDO>()
                .eq(AdminOAuth2ApproveDO::getUserId, userId)
                .eq(AdminOAuth2ApproveDO::getUserType, userType)
                .eq(AdminOAuth2ApproveDO::getClientId, clientId));
    }

}
