package cn.iocoder.yudao.module.project.service.member;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.member.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 会员用户 Service 接口
 *
 * @author 芋道源码
 */
public interface MemberService {

    /**
     * 创建会员用户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMember(@Valid MemberSaveReqVO createReqVO);

    /**
     * 更新会员用户
     *
     * @param updateReqVO 更新信息
     */
    void updateMember(@Valid MemberSaveReqVO updateReqVO);

    /**
     * 删除会员用户
     *
     * @param id 编号
     */
    void deleteMember(Long id);

    /**
     * 获得会员用户
     *
     * @param id 编号
     * @return 会员用户
     */
    MemberDO getMember(Long id);

    /**
     * 获得会员用户分页
     *
     * @param pageReqVO 分页查询
     * @return 会员用户分页
     */
    PageResult<MemberDO> getMemberPage(MemberPageReqVO pageReqVO);

}