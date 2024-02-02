package cn.iocoder.yudao.module.project.controller.admin.wallettransaction;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionAddressDO;
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

import cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionDO;
import cn.iocoder.yudao.module.project.service.wallettransaction.WalletTransactionService;

@Tag(name = "管理后台 - 钱包流水")
@RestController
@RequestMapping("/project/wallet-transaction")
@Validated
public class WalletTransactionController {

    @Resource
    private WalletTransactionService walletTransactionService;

    @PostMapping("/create")
    @Operation(summary = "创建钱包流水")
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:create')")
    public CommonResult<Long> createWalletTransaction(@Valid @RequestBody WalletTransactionSaveReqVO createReqVO) {
        return success(walletTransactionService.createWalletTransaction(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新钱包流水")
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:update')")
    public CommonResult<Boolean> updateWalletTransaction(@Valid @RequestBody WalletTransactionSaveReqVO updateReqVO) {
        walletTransactionService.updateWalletTransaction(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除钱包流水")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:delete')")
    public CommonResult<Boolean> deleteWalletTransaction(@RequestParam("id") Long id) {
        walletTransactionService.deleteWalletTransaction(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得钱包流水")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:query')")
    public CommonResult<WalletTransactionRespVO> getWalletTransaction(@RequestParam("id") Long id) {
        WalletTransactionDO walletTransaction = walletTransactionService.getWalletTransaction(id);
        return success(BeanUtils.toBean(walletTransaction, WalletTransactionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得钱包流水分页")
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:query')")
    public CommonResult<PageResult<WalletTransactionAddressRespVO>> getWalletTransactionPage(@Valid WalletTransactionPageReqVO pageReqVO) {
        PageResult<WalletTransactionAddressDO> pageResult = walletTransactionService.getWalletTransactionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WalletTransactionAddressRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出钱包流水 Excel")
    @PreAuthorize("@ss.hasPermission('project:wallet-transaction:export')")
    @OperateLog(type = EXPORT)
    public void exportWalletTransactionExcel(@Valid WalletTransactionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WalletTransactionAddressDO> list = walletTransactionService.getWalletTransactionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "钱包流水.xls", "数据", WalletTransactionRespVO.class,
                        BeanUtils.toBean(list, WalletTransactionRespVO.class));
    }

}