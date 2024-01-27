package cn.iocoder.yudao.module.trade.controller.app.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.*;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.ShelveOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.service.order.ShelveOrderUpdateService;
import cn.iocoder.yudao.module.trade.service.order.ShelveOrderUpdateServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 上架订单")
@RestController
@RequestMapping("/shelve/order")
@Validated
@Slf4j
public class AppShelveOrderController {

    @Resource
    private ShelveOrderUpdateService shelveOrderUpdateService;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @PreAuthenticated
    public CommonResult<AppTradeOrderCreateRespVO> createOrder(@Valid @RequestBody AppShelveOrderCreateReqVO createReqVO) {
        // 生成订单
        TradeOrderDO order = shelveOrderUpdateService.createOrder(getLoginUserId(), getClientIP(), createReqVO, 1);
        return success(new AppTradeOrderCreateRespVO().setId(order.getId()).setPayOrderId(order.getPayOrderId()));
    }

    @PutMapping("close")
    @Operation(summary = "下架订单")
    @PreAuthenticated
    public CommonResult<Boolean> closeOrder(@RequestParam("id") Long id) {
        shelveOrderUpdateService.closeOrder(getLoginUserId(), id);
        return success(true);
    }
}
