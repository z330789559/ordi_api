package cn.iocoder.yudao.module.admin.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.oauth2.AdminOAuth2ClientDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * OAuth2 客户端 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OAuthAdmin2ClientMapper extends BaseMapperX<AdminOAuth2ClientDO> {

    default PageResult<AdminOAuth2ClientDO> selectPage(OAuth2ClientPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminOAuth2ClientDO>()
                .likeIfPresent(AdminOAuth2ClientDO::getName, reqVO.getName())
                .eqIfPresent(AdminOAuth2ClientDO::getStatus, reqVO.getStatus())
                .orderByDesc(AdminOAuth2ClientDO::getId));
    }

    default AdminOAuth2ClientDO selectByClientId(String clientId) {
        return selectOne(AdminOAuth2ClientDO::getClientId, clientId);
    }

}
