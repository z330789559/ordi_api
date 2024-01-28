package cn.iocoder.yudao.module.admin.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.dal.dataobject.oauth2.AdminOAuth2RefreshTokenDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminOAuth2RefreshTokenMapper extends BaseMapperX<AdminOAuth2RefreshTokenDO> {

    default int deleteByRefreshToken(String refreshToken) {
        return delete(new LambdaQueryWrapperX<AdminOAuth2RefreshTokenDO>()
                .eq(AdminOAuth2RefreshTokenDO::getRefreshToken, refreshToken));
    }

    default AdminOAuth2RefreshTokenDO selectByRefreshToken(String refreshToken) {
        return selectOne(AdminOAuth2RefreshTokenDO::getRefreshToken, refreshToken);
    }

}
