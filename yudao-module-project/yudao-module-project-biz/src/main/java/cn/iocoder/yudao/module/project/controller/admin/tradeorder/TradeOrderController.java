package cn.iocoder.yudao.module.project.controller.admin.tradeorder;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderItemDO;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderPageDO;
import cn.iocoder.yudao.module.trade.api.order.TradeOrderApi;
import cn.iocoder.yudao.module.trade.enums.OrderOperatorTerminal;
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

import cn.iocoder.yudao.module.project.controller.admin.tradeorder.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.tradeorder.AdminTradeOrderDO;
import cn.iocoder.yudao.module.project.service.tradeorder.TradeOrderService;

@Tag(name = "管理后台 - 交易订单")
@RestController
@RequestMapping("/project/trade-order")
@Validated
public class TradeOrderController {

    @Resource
    private TradeOrderService tradeOrderService;


    @Resource
    private TradeOrderApi tradeOrderApi;

    @PostMapping("/create")
    @Operation(summary = "创建交易订单")
    @PreAuthorize("@ss.hasPermission('project:trade-order:create')")
    public CommonResult<Long> createTradeOrder(@Valid @RequestBody TradeOrderSaveReqVO createReqVO) {
        return success(tradeOrderService.createTradeOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新交易订单")
    @PreAuthorize("@ss.hasPermission('project:trade-order:update')")
    public CommonResult<Boolean> updateTradeOrder(@Valid @RequestBody TradeOrderSaveReqVO updateReqVO) {
        tradeOrderService.updateTradeOrder(updateReqVO);
        return success(true);
    }

    @PostMapping("/close")
    @Operation(summary = "关闭交易订单")
    @PreAuthorize("@ss.hasPermission('project:trade-order:close')")
    public CommonResult<Boolean> closeTradeOrder(@RequestParam("userId") Long userId,@RequestParam("id") Long id) {
        tradeOrderApi.closeOrder(userId,id, OrderOperatorTerminal.ADMIN);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除交易订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:trade-order:delete')")
    public CommonResult<Boolean> deleteTradeOrder(@RequestParam("id") Long id) {
        tradeOrderService.deleteTradeOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得交易订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:trade-order:query')")
    public CommonResult<TradeOrderRespVO> getTradeOrder(@RequestParam("id") Long id) {
        AdminTradeOrderDO tradeOrder = tradeOrderService.getTradeOrder(id);
        return success(BeanUtils.toBean(tradeOrder, TradeOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得交易订单分页")
    @PreAuthorize("@ss.hasPermission('project:trade-order:query')")
    public CommonResult<PageResult<TradeOrderRespVO>> getTradeOrderPage(@Valid TradeOrderPageReqVO pageReqVO) {
        PageResult<AdminTradeOrderPageDO> pageResult = tradeOrderService.getTradeOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TradeOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出交易订单 Excel")
    @PreAuthorize("@ss.hasPermission('project:trade-order:export')")
    @OperateLog(type = EXPORT)
    public void exportTradeOrderExcel(@Valid TradeOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AdminTradeOrderPageDO> list = tradeOrderService.getTradeOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "交易订单.xls", "数据", TradeOrderRespVO.class,
                        BeanUtils.toBean(list, TradeOrderRespVO.class));
    }

    // ==================== 子表（交易订单明细） ====================

    @GetMapping("/trade-order-item/list-by-order-id")
    @Operation(summary = "获得交易订单明细列表")
    @Parameter(name = "orderId", description = "订单id")
    @PreAuthorize("@ss.hasPermission('project:trade-order:query')")
    public CommonResult<List<AdminTradeOrderItemDO>> getTradeOrderItemListByOrderId(@RequestParam("orderId") Long orderId) {
        return success(tradeOrderService.getTradeOrderItemListByOrderId(orderId));
    }

}