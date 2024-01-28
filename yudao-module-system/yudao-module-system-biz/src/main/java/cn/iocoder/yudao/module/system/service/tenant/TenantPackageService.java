package cn.iocoder.yudao.module.system.service.tenant;

import java.util.List;

import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantPackageDO;

/**
 * 租户套餐 Service 接口
 *
 * @author 芋道源码
 */
public interface TenantPackageService {

    /**
     * 删除租户套餐
     *
     * @param id 编号
     */
    void deleteTenantPackage(Long id);

    /**
     * 获得租户套餐
     *
     * @param id 编号
     * @return 租户套餐
     */
    TenantPackageDO getTenantPackage(Long id);

    /**
     * 校验租户套餐
     *
     * @param id 编号
     * @return 租户套餐
     */
    TenantPackageDO validTenantPackage(Long id);

    /**
     * 获得指定状态的租户套餐列表
     *
     * @param status 状态
     * @return 租户套餐
     */
    List<TenantPackageDO> getTenantPackageListByStatus(Integer status);

}
