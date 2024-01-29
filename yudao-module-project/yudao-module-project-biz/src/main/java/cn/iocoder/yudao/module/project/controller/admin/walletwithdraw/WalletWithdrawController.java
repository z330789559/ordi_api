package cn.iocoder.yudao.module.project.controller.admin.walletwithdraw;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.iocoder.yudao.module.pay.api.withdraw.WithDrawApi;
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

import cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.walletwithdraw.WalletWithdrawDO;
import cn.iocoder.yudao.module.project.service.walletwithdraw.WalletWithdrawService;

@Tag(name = "管理后台 - 提现")
@RestController
@RequestMapping("/project/wallet-withdraw")
@Validated
public class WalletWithdrawController {

    @Resource
    private WalletWithdrawService walletWithdrawService;

    @Resource
    private WithDrawApi withDrawApi;

    @PostMapping("/create")
    @Operation(summary = "创建提现")
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:create')")
    public CommonResult<Long> createWalletWithdraw(@Valid @RequestBody WalletWithdrawSaveReqVO createReqVO) {
        return success(walletWithdrawService.createWalletWithdraw(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现")
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:update')")
    public CommonResult<Boolean> updateWalletWithdraw(@Valid @RequestBody WalletWithdrawSaveReqVO updateReqVO) {
        walletWithdrawService.updateWalletWithdraw(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:delete')")
    public CommonResult<Boolean> deleteWalletWithdraw(@RequestParam("id") Long id) {
        walletWithdrawService.deleteWalletWithdraw(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:query')")
    public CommonResult<WalletWithdrawRespVO> getWalletWithdraw(@RequestParam("id") Long id) {
        WalletWithdrawDO walletWithdraw = walletWithdrawService.getWalletWithdraw(id);
        return success(BeanUtils.toBean(walletWithdraw, WalletWithdrawRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现分页")
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:query')")
    public CommonResult<PageResult<WalletWithdrawRespVO>> getWalletWithdrawPage(@Valid WalletWithdrawPageReqVO pageReqVO) {
        PageResult<WalletWithdrawDO> pageResult = walletWithdrawService.getWalletWithdrawPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WalletWithdrawRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出提现 Excel")
    @PreAuthorize("@ss.hasPermission('project:wallet-withdraw:export')")
    @OperateLog(type = EXPORT)
    public void exportWalletWithdrawExcel(@Valid WalletWithdrawPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WalletWithdrawDO> list = walletWithdrawService.getWalletWithdrawPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "提现.xls", "数据", WalletWithdrawRespVO.class,
                        BeanUtils.toBean(list, WalletWithdrawRespVO.class));
    }


    @PostMapping("/audit")
    @Operation(summary = "提现审核")
    public CommonResult<String> withdrawAudit(@Valid @RequestBody WithDrawAuditReqVO auditReqVO) {
        withDrawApi.aduitWithdraw(auditReqVO.getId(), auditReqVO.getStatus(), auditReqVO.getRemark());
        return success("审核成功");
    }


}