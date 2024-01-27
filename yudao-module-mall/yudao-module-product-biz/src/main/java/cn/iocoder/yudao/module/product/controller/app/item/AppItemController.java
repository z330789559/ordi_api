package cn.iocoder.yudao.module.product.controller.app.item;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.ItemUpdateStatusReqVO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.AppItemDetailRespVO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.AppItemPageReqVO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.AppItemPageRespVO;
import cn.iocoder.yudao.module.product.convert.item.ItemConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import cn.iocoder.yudao.module.product.service.item.ItemService;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

@Tag(name = "用户 APP - 商品 ITEM")
@RestController
@RequestMapping("/product/item")
@Validated
public class AppItemController {

    @Resource
    private ItemService itemService;
    @Resource
    private TokenService tokenService;
    @Resource
    private MemberUserApi memberUserApi;

    @PostMapping("/update-status")
    @Operation(summary = "更新状态")
    @PreAuthenticated
    public CommonResult<Boolean> updateStatus(@Valid @RequestBody ItemUpdateStatusReqVO updateReqVO) {
        ItemDO item = itemService.getItem(updateReqVO.getId());
        if (item == null) {
            throw exception(ITEM_NOT_EXISTS);
        }
        if (!item.getUserId().equals(getLoginUserId())) {
            throw exception(ITEM_NOT_BELONG_YOUR);
        }

        // 只有上架的订单才允许下架
        if (updateReqVO.getStatus().equals(ItemStatusEnum.DISABLE.getStatus())) {
            if (!item.getStatus().equals(ItemStatusEnum.ENABLE.getStatus())) {
                throw exception(ITEM_STATUS_ERROR);
            }
        }
        // 购买和下架状态的订单允许上架
        else if (updateReqVO.getStatus().equals(ItemStatusEnum.ENABLE.getStatus())) {
            if (!item.getStatus().equals(ItemStatusEnum.DISABLE.getStatus())
                    && !item.getStatus().equals(ItemStatusEnum.BUY.getStatus())) {
                throw exception(ITEM_STATUS_ERROR);
            }
        } else {
            throw exception(ITEM_STATUS_ERROR);
        }

        itemService.updateItemStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品 ITEM 分页")
    public CommonResult<PageResult<AppItemPageRespVO>> getItemPage(@Valid AppItemPageReqVO pageVO) {
        // 上架状态
        pageVO.setStatus(ItemStatusEnum.ENABLE.getStatus());
        PageResult<ItemDO> pageResult = itemService.getItemPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        TokenDO token = tokenService.getToken(2L);

        PageResult<AppItemPageRespVO> voPageResult = ItemConvert.INSTANCE.convertPageForGetItemPage(pageResult);

        voPageResult.getList().forEach(vo -> {
            BigDecimal btcPrice = vo.getPrice().multiply(token.getUsdPrice())
                    .setScale(8, RoundingMode.UP); // USDT转BTC
            vo.setTotalPrice(btcPrice.multiply(BigDecimal.valueOf(vo.getStock())))
                    .setUsdtTotalPrice(vo.getPrice().multiply(BigDecimal.valueOf(vo.getStock())));
        });

        return success(voPageResult);
    }

    @GetMapping("/my-item")
    @Operation(summary = "获得我的商品")
    @PreAuthenticated
    public CommonResult<PageResult<AppItemPageRespVO>> getMyItemPage(@Valid AppItemPageReqVO pageVO) {
        pageVO.setUserId(getLoginUserId());
        if (pageVO.getStatus().equals(-1)) {
            pageVO.setStatus(null);
        }
        PageResult<ItemDO> pageResult = itemService.getItemPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        PageResult<AppItemPageRespVO> voPageResult = ItemConvert.INSTANCE.convertPageForGetItemPage(pageResult);

        voPageResult.getList().forEach(vo -> {
            BigDecimal btcPrice = vo.getPrice().divide(BigDecimal.valueOf(43000), RoundingMode.UP);
            vo.setTotalPrice(btcPrice.multiply(BigDecimal.valueOf(vo.getStock())));
        });

        return success(voPageResult);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 ITEM 明细")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<AppItemDetailRespVO> getItemDetail(@RequestParam("id") Long id) {
        // 获得商品 ITEM
        ItemDO spu = itemService.getItem(id);
        if (spu == null) {
            throw exception(ITEM_NOT_EXISTS);
        }
        if (!ItemStatusEnum.isEnable(spu.getStatus())) {
            throw exception(ITEM_NOT_ENABLE);
        }

        AppItemDetailRespVO detailVO = ItemConvert.INSTANCE.convertForGetItemDetail(spu);
        return success(detailVO);
    }
}
