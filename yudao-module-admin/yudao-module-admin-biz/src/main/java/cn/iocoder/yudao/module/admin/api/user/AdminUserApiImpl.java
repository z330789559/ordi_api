package cn.iocoder.yudao.module.admin.api.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.admin.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.admin.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.admin.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.admin.service.dept.DeptService;
import cn.iocoder.yudao.module.admin.service.user.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * Admin 用户 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @Override
    public AdminUserRespDTO getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return BeanUtils.toBean(user, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListBySubordinate(Long userId) {
        // 1.1 获取用户负责的部门
        AdminUserDO user = userService.getUser(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        ArrayList<Long> deptIds = new ArrayList<>();
        DeptDO dept = deptService.getDept(user.getDeptId());
        if (dept == null) {
            return Collections.emptyList();
        }
        if (ObjUtil.notEqual(dept.getLeaderUserId(), userId)) { // 校验为负责人
            return Collections.emptyList();
        }
        deptIds.add(dept.getId());
        // 1.2 获取所有子部门
        List<DeptDO> childDeptList = deptService.getChildDeptList(dept.getId());
        if (CollUtil.isNotEmpty(childDeptList)) {
            deptIds.addAll(convertSet(childDeptList, DeptDO::getId));
        }

        // 2. 获取部门对应的用户信息
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        users.removeIf(item -> ObjUtil.equal(item.getId(), userId)); // 排除自己
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserList(Collection<Long> ids) {
        List<AdminUserDO> users = userService.getUserList(ids);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        userService.validateUserList(ids);
    }

}
