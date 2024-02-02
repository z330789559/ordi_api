package cn.iocoder.yudao.module.project.controller.admin.wallet;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletMemberDO;
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

import cn.iocoder.yudao.module.project.controller.admin.wallet.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletDO;
import cn.iocoder.yudao.module.project.service.wallet.WalletService;

@Tag(name = "管理后台 - 钱包")
@RestController
@RequestMapping("/project/wallet")
@Validated
public class WalletController {

    @Resource
    private WalletService walletService;

    @PostMapping("/create")
    @Operation(summary = "创建钱包")
    @PreAuthorize("@ss.hasPermission('project:wallet:create')")
    public CommonResult<Long> createWallet(@Valid @RequestBody WalletSaveReqVO createReqVO) {
        return success(walletService.createWallet(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新钱包")
    @PreAuthorize("@ss.hasPermission('project:wallet:update')")
    public CommonResult<Boolean> updateWallet(@Valid @RequestBody WalletSaveReqVO updateReqVO) {
        walletService.updateWallet(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除钱包")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:wallet:delete')")
    public CommonResult<Boolean> deleteWallet(@RequestParam("id") Long id) {
        walletService.deleteWallet(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得钱包")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:wallet:query')")
    public CommonResult<WalletRespVO> getWallet(@RequestParam("id") Long id) {
        WalletDO wallet = walletService.getWallet(id);
        return success(BeanUtils.toBean(wallet, WalletRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得钱包分页")
    @PreAuthorize("@ss.hasPermission('project:wallet:query')")
    public CommonResult<PageResult<WalletRespVO>> getWalletPage(@Valid WalletPageReqVO pageReqVO) {
        PageResult<WalletMemberDO> pageResult = walletService.getWalletPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WalletRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出钱包 Excel")
    @PreAuthorize("@ss.hasPermission('project:wallet:export')")
    @OperateLog(type = EXPORT)
    public void exportWalletExcel(@Valid WalletPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WalletMemberDO> list = walletService.getWalletPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "钱包.xls", "数据", WalletRespVO.class,
                        BeanUtils.toBean(list, WalletRespVO.class));
    }

}