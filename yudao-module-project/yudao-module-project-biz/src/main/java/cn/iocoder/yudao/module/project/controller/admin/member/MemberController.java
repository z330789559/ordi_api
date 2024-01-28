package cn.iocoder.yudao.module.project.controller.admin.member;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.project.controller.admin.member.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.member.MemberDO;
import cn.iocoder.yudao.module.project.service.member.MemberService;

@Tag(name = "管理后台 - 会员用户")
@RestController
@RequestMapping("/project/member")
@Validated
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/create")
    @Operation(summary = "创建会员用户")
    @PreAuthorize("@ss.hasPermission('project:member:create')")
    public CommonResult<Long> createMember(@Valid @RequestBody MemberSaveReqVO createReqVO) {
        return success(memberService.createMember(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员用户")
    @PreAuthorize("@ss.hasPermission('project:member:update')")
    public CommonResult<Boolean> updateMember(@Valid @RequestBody MemberSaveReqVO updateReqVO) {
        memberService.updateMember(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员用户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:member:delete')")
    public CommonResult<Boolean> deleteMember(@RequestParam("id") Long id) {
        memberService.deleteMember(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:member:query')")
    public CommonResult<MemberRespVO> getMember(@RequestParam("id") Long id) {
        MemberDO member = memberService.getMember(id);
        return success(BeanUtils.toBean(member, MemberRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员用户分页")
    @PreAuthorize("@ss.hasPermission('project:member:query')")
    public CommonResult<PageResult<MemberRespVO>> getMemberPage(@Valid MemberPageReqVO pageReqVO) {
        PageResult<MemberDO> pageResult = memberService.getMemberPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MemberRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员用户 Excel")
    @PreAuthorize("@ss.hasPermission('project:member:export')")
    @OperateLog(type = EXPORT)
    public void exportMemberExcel(@Valid MemberPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MemberDO> list = memberService.getMemberPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员用户.xls", "数据", MemberRespVO.class,
                        BeanUtils.toBean(list, MemberRespVO.class));
    }

}