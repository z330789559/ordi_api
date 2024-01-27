package cn.iocoder.yudao.module.member.controller.app.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.controller.app.user.vo.*;
import cn.iocoder.yudao.module.member.convert.user.MemberUserConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 用户个人中心")
@RestController
@RequestMapping("/member/user")
@Validated
@Slf4j
public class AppMemberUserController {

    @Resource
    private MemberUserService userService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    @PreAuthenticated
    public CommonResult<AppMemberUserInfoRespVO> getUserInfo() {
        MemberUserDO user = userService.getUser(getLoginUserId());

        return success(MemberUserConvert.INSTANCE.convert(user));
    }
    @GetMapping("/get-invitation")
    @Operation(summary = "获得邀请人列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppMemberUserInfoRespVO>> getInvitationUser(@Valid AppMemberUserPageReqVO pageVO) {
        pageVO.setParentId(getLoginUserId());
        PageResult<MemberUserDO> pageResult = userService.getUserPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        PageResult<AppMemberUserInfoRespVO> voPageResult = MemberUserConvert.INSTANCE.convertPage02(pageResult);

        return success(voPageResult);
    }

    @GetMapping("/get-team")
    @Operation(summary = "获得网体列表")
    @PreAuthenticated
    public CommonResult<List<AppMemberUserInfoRespVO>> getTeamUser() {
        AppMemberUserPageReqVO pageVO = new AppMemberUserPageReqVO().setParentId(getLoginUserId());
        List<MemberUserDO> pageResult = userService.getUserPageRecursion(pageVO);

        return success(MemberUserConvert.INSTANCE.convertList(pageResult));
    }

    @PutMapping("/bind")
    @Operation(summary = "绑定用户")
    @PreAuthenticated
    public CommonResult<Boolean> bindUser(@RequestBody @Valid AppMemberUserBindReqVO reqVO) {
        userService.updateBind(getLoginUserId(), reqVO.getInvitationCode());
        return success(true);
    }

}
