package cn.iocoder.yudao.module.admin.service.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.admin.controller.admin.tenant.vo.packages.TenantPackagePageReqVO;
import cn.iocoder.yudao.module.admin.controller.admin.tenant.vo.packages.TenantPackageSaveReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.tenant.AdminTenantDO;
import cn.iocoder.yudao.module.admin.dal.dataobject.tenant.AdminTenantPackageDO;
import cn.iocoder.yudao.module.admin.dal.mysql.tenant.AdminTenantPackageMapper;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.admin.enums.ErrorCodeConstants.*;

/**
 * 租户套餐 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TenantAdminPackageServiceImpl implements TenantPackageService {

    @Resource
    private AdminTenantPackageMapper adminTenantPackageMapper;

    @Resource
    @Lazy // 避免循环依赖的报错
    private TenantService tenantService;

    @Override
    public Long createTenantPackage(TenantPackageSaveReqVO createReqVO) {
        // 插入
        AdminTenantPackageDO tenantPackage = BeanUtils.toBean(createReqVO, AdminTenantPackageDO.class);
        adminTenantPackageMapper.insert(tenantPackage);
        // 返回
        return tenantPackage.getId();
    }

    @Override
    @DSTransactional // 多数据源，使用 @DSTransactional 保证本地事务，以及数据源的切换
    public void updateTenantPackage(TenantPackageSaveReqVO updateReqVO) {
        // 校验存在
        AdminTenantPackageDO tenantPackage = validateTenantPackageExists(updateReqVO.getId());
        // 更新
        AdminTenantPackageDO updateObj = BeanUtils.toBean(updateReqVO, AdminTenantPackageDO.class);
        adminTenantPackageMapper.updateById(updateObj);
        // 如果菜单发生变化，则修改每个租户的菜单
        if (!CollUtil.isEqualList(tenantPackage.getMenuIds(), updateReqVO.getMenuIds())) {
            List<AdminTenantDO> tenants = tenantService.getTenantListByPackageId(tenantPackage.getId());
            tenants.forEach(tenant -> tenantService.updateTenantRoleMenu(tenant.getId(), updateReqVO.getMenuIds()));
        }
    }

    @Override
    public void deleteTenantPackage(Long id) {
        // 校验存在
        validateTenantPackageExists(id);
        // 校验正在使用
        validateTenantUsed(id);
        // 删除
        adminTenantPackageMapper.deleteById(id);
    }

    private AdminTenantPackageDO validateTenantPackageExists(Long id) {
        AdminTenantPackageDO tenantPackage = adminTenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        return tenantPackage;
    }

    private void validateTenantUsed(Long id) {
        if (tenantService.getTenantCountByPackageId(id) > 0) {
            throw exception(TENANT_PACKAGE_USED);
        }
    }

    @Override
    public AdminTenantPackageDO getTenantPackage(Long id) {
        return adminTenantPackageMapper.selectById(id);
    }

    @Override
    public PageResult<AdminTenantPackageDO> getTenantPackagePage(TenantPackagePageReqVO pageReqVO) {
        return adminTenantPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public AdminTenantPackageDO validTenantPackage(Long id) {
        AdminTenantPackageDO tenantPackage = adminTenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        if (tenantPackage.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_PACKAGE_DISABLE, tenantPackage.getName());
        }
        return tenantPackage;
    }

    @Override
    public List<AdminTenantPackageDO> getTenantPackageListByStatus(Integer status) {
        return adminTenantPackageMapper.selectListByStatus(status);
    }

}
