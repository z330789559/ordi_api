package cn.iocoder.yudao.module.system.api.tenant;

import java.util.List;

import javax.annotation.Resource;

import cn.iocoder.yudao.module.admin.api.tenant.TenantApi;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;

import org.springframework.stereotype.Service;

/**
 * 多租户的 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class TenantApiImpl implements TenantApi {

    @Resource
    private TenantService tenantService;

    @Override
    public List<Long> getTenantIdList() {
        return tenantService.getTenantIdList();
    }

    @Override
    public void validateTenant(Long id) {
        tenantService.validTenant(id);
    }

}
