package cn.iocoder.yudao.module.member.api.user;

import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 会员用户的 API 接口
 *
 * @author 芋道源码
 */
public interface MemberUserApi {

    /**
     * 获得会员用户信息
     *
     * @param id 用户编号
     * @return 用户信息
     */
    MemberUserRespDTO getUser(Long id);

    /**
     * 获得会员用户信息们
     *
     * @param ids 用户编号的数组
     * @return 用户信息们
     */
    List<MemberUserRespDTO> getUserList(Collection<Long> ids);

    /**
     * 获得会员用户 Map
     *
     * @param ids 用户编号的数组
     * @return 会员用户 Map
     */
    default Map<Long, MemberUserRespDTO> getUserMap(Collection<Long> ids) {
        List<MemberUserRespDTO> list = getUserList(ids);
        return convertMap(list, MemberUserRespDTO::getId);
    }

    MemberUserRespDTO getUserByAddress(String address);
}
