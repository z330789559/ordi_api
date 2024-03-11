package cn.iocoder.yudao.module.project.controller.admin.whitelist;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.StatisticsDo;
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

import cn.iocoder.yudao.module.project.controller.admin.whitelist.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.WhiteListDO;
import cn.iocoder.yudao.module.project.service.whitelist.WhiteListService;

@Tag(name = "管理后台 - 白名单")
@RestController
@RequestMapping("/project/white-list")
@Validated
public class WhiteListController {

    @Resource
    private WhiteListService whiteListService;

    @PostMapping("/create")
    @Operation(summary = "创建白名单")
    @PreAuthorize("@ss.hasPermission('project:white-list:create')")
    public CommonResult<Integer> createWhiteList(@Valid @RequestBody WhiteListSaveReqVO createReqVO) {
        return success(whiteListService.createWhiteList(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新白名单")
    @PreAuthorize("@ss.hasPermission('project:white-list:update')")
    public CommonResult<Boolean> updateWhiteList(@Valid @RequestBody WhiteListSaveReqVO updateReqVO) {
        whiteListService.updateWhiteList(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除白名单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('project:white-list:delete')")
    public CommonResult<Boolean> deleteWhiteList(@RequestParam("id") Integer id) {
        whiteListService.deleteWhiteList(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得白名单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('project:white-list:query')")
    public CommonResult<WhiteListRespVO> getWhiteList(@RequestParam("id") Integer id) {
        WhiteListDO whiteList = whiteListService.getWhiteList(id);
        return success(BeanUtils.toBean(whiteList, WhiteListRespVO.class));
    }


    @GetMapping("/ordinal-static")
    @Operation(summary = "获得统计列表")
    public CommonResult<StatisticsRepVo> getWhiteListList() {
        StatisticsDo sdo = whiteListService.getStatisticData();
        return success(BeanUtils.toBean(sdo, StatisticsRepVo.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得白名单分页")
    @PreAuthorize("@ss.hasPermission('project:white-list:query')")
    public CommonResult<PageResult<WhiteListRespVO>> getWhiteListPage(@Valid WhiteListPageReqVO pageReqVO) {
        PageResult<WhiteListDO> pageResult = whiteListService.getWhiteListPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WhiteListRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出白名单 Excel")
    @PreAuthorize("@ss.hasPermission('project:white-list:export')")
    @OperateLog(type = EXPORT)
    public void exportWhiteListExcel(@Valid WhiteListPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WhiteListDO> list = whiteListService.getWhiteListPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "白名单.xls", "数据", WhiteListRespVO.class,
                        BeanUtils.toBean(list, WhiteListRespVO.class));
    }

}