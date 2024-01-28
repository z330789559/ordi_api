package cn.iocoder.yudao.module.admin.dal.mysql.tenant;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.controller.admin.tenant.vo.packages.TenantPackagePageReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.tenant.AdminTenantPackageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 租户套餐 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminTenantPackageMapper extends BaseMapperX<AdminTenantPackageDO> {

    default PageResult<AdminTenantPackageDO> selectPage(TenantPackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminTenantPackageDO>()
                .likeIfPresent(AdminTenantPackageDO::getName, reqVO.getName())
                .eqIfPresent(AdminTenantPackageDO::getStatus, reqVO.getStatus())
                .likeIfPresent(AdminTenantPackageDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(AdminTenantPackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AdminTenantPackageDO::getId));
    }

    default List<AdminTenantPackageDO> selectListByStatus(Integer status) {
        return selectList(AdminTenantPackageDO::getStatus, status);
    }
}
