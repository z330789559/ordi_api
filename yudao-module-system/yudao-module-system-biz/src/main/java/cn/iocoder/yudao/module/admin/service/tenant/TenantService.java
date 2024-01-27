package cn.iocoder.yudao.module.admin.service.tenant;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.admin.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.admin.service.tenant.handler.TenantInfoHandler;

import java.util.List;

/**
 * 租户 Service 接口
 *
 * @author 芋道源码
 */
public interface TenantService {

    /**
     * 获得租户
     *
     * @param id 编号
     * @return 租户
     */
    TenantDO getTenant(Long id);
    /**
     * 获得使用指定套餐的租户数量
     *
     * @param packageId 租户套餐编号
     * @return 租户数量
     */
    Long getTenantCountByPackageId(Long packageId);

    /**
     * 进行租户的信息处理逻辑
     * 其中，租户编号从 {@link TenantContextHolder} 上下文中获取
     *
     * @param handler 处理器
     */
    void handleTenantInfo(TenantInfoHandler handler);


    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIdList();

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validTenant(Long id);

}
