package cn.iocoder.yudao.module.admin.dal.mysql.permission;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.admin.dal.dataobject.permission.AdminUserRoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapperX<AdminUserRoleDO> {

    default List<AdminUserRoleDO> selectListByUserId(Long userId) {
        return selectList(AdminUserRoleDO::getUserId, userId);
    }

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<AdminUserRoleDO>()
                .eq(AdminUserRoleDO::getUserId, userId)
                .in(AdminUserRoleDO::getRoleId, roleIds));
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<AdminUserRoleDO>().eq(AdminUserRoleDO::getUserId, userId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<AdminUserRoleDO>().eq(AdminUserRoleDO::getRoleId, roleId));
    }

    default List<AdminUserRoleDO> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(AdminUserRoleDO::getRoleId, roleIds);
    }

}
