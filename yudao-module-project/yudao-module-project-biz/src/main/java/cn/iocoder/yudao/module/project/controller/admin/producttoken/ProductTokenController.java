package cn.iocoder.yudao.module.project.controller.admin.producttoken;

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

import cn.iocoder.yudao.module.project.controller.admin.producttoken.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.producttoken.ProductTokenDO;
import cn.iocoder.yudao.module.project.service.producttoken.ProductTokenService;

@Tag(name = "管理后台 - 产品列")
@RestController
@RequestMapping("/project/product-token")
@Validated
public class ProductTokenController {

    @Resource
    private ProductTokenService productTokenService;

    @PostMapping("/create")
    @Operation(summary = "创建产品列")
    @PreAuthorize("@ss.hasPermission('project:product-token:create')")
    public CommonResult<Long> createProductToken(@Valid @RequestBody ProductTokenSaveReqVO createReqVO) {
        return success(productTokenService.createProductToken(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品列")
    @PreAuthorize("@ss.hasPermission('project:product-token:update')")
    public CommonResult<Boolean> updateProductToken(@Valid @RequestBody ProductTokenSaveReqVO updateReqVO) {
        productTokenService.updateProductToken(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:product-token:delete')")
    public CommonResult<Boolean> deleteProductToken(@RequestParam("id") Long id) {
        productTokenService.deleteProductToken(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:product-token:query')")
    public CommonResult<ProductTokenRespVO> getProductToken(@RequestParam("id") Long id) {
        ProductTokenDO productToken = productTokenService.getProductToken(id);
        return success(BeanUtils.toBean(productToken, ProductTokenRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品列分页")
    @PreAuthorize("@ss.hasPermission('project:product-token:query')")
    public CommonResult<PageResult<ProductTokenRespVO>> getProductTokenPage(@Valid ProductTokenPageReqVO pageReqVO) {
        PageResult<ProductTokenDO> pageResult = productTokenService.getProductTokenPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductTokenRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品列 Excel")
    @PreAuthorize("@ss.hasPermission('project:product-token:export')")
    @OperateLog(type = EXPORT)
    public void exportProductTokenExcel(@Valid ProductTokenPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductTokenDO> list = productTokenService.getProductTokenPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品列.xls", "数据", ProductTokenRespVO.class,
                        BeanUtils.toBean(list, ProductTokenRespVO.class));
    }

}