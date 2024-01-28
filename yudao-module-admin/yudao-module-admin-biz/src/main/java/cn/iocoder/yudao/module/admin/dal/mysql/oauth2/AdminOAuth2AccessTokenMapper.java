package cn.iocoder.yudao.module.admin.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.oauth2.AdminOAuth2AccessTokenDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdminOAuth2AccessTokenMapper extends BaseMapperX<AdminOAuth2AccessTokenDO> {

    default AdminOAuth2AccessTokenDO selectByAccessToken(String accessToken) {
        return selectOne(AdminOAuth2AccessTokenDO::getAccessToken, accessToken);
    }

    default List<AdminOAuth2AccessTokenDO> selectListByRefreshToken(String refreshToken) {
        return selectList(AdminOAuth2AccessTokenDO::getRefreshToken, refreshToken);
    }

    default PageResult<AdminOAuth2AccessTokenDO> selectPage(OAuth2AccessTokenPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminOAuth2AccessTokenDO>()
                .eqIfPresent(AdminOAuth2AccessTokenDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AdminOAuth2AccessTokenDO::getUserType, reqVO.getUserType())
                .likeIfPresent(AdminOAuth2AccessTokenDO::getClientId, reqVO.getClientId())
                .gt(AdminOAuth2AccessTokenDO::getExpiresTime, LocalDateTime.now())
                .orderByDesc(AdminOAuth2AccessTokenDO::getId));
    }

}
