package cn.iocoder.yudao.module.admin.dal.mysql.tenant;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.controller.admin.tenant.vo.tenant.TenantPageReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.tenant.AdminTenantDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 租户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminTenantMapper extends BaseMapperX<AdminTenantDO> {

    default PageResult<AdminTenantDO> selectPage(TenantPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminTenantDO>()
                .likeIfPresent(AdminTenantDO::getName, reqVO.getName())
                .likeIfPresent(AdminTenantDO::getContactName, reqVO.getContactName())
                .likeIfPresent(AdminTenantDO::getContactMobile, reqVO.getContactMobile())
                .eqIfPresent(AdminTenantDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminTenantDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AdminTenantDO::getId));
    }

    default AdminTenantDO selectByName(String name) {
        return selectOne(AdminTenantDO::getName, name);
    }

    default AdminTenantDO selectByWebsite(String website) {
        AdminTenantDO tenant = selectOne(AdminTenantDO::getWebsite, website);
        if(tenant == null){
            return selectOne(AdminTenantDO::getWebsite, "www.iocoder.cn");
        }
      return tenant;
    }

    default Long selectCountByPackageId(Long packageId) {
        return selectCount(AdminTenantDO::getPackageId, packageId);
    }

    default List<AdminTenantDO> selectListByPackageId(Long packageId) {
        return selectList(AdminTenantDO::getPackageId, packageId);
    }

}
