package cn.iocoder.yudao.module.admin.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.admin.dal.dataobject.oauth2.AdminOAuth2CodeDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminOAuth2CodeMapper extends BaseMapperX<AdminOAuth2CodeDO> {

    default AdminOAuth2CodeDO selectByCode(String code) {
        return selectOne(AdminOAuth2CodeDO::getCode, code);
    }

}
