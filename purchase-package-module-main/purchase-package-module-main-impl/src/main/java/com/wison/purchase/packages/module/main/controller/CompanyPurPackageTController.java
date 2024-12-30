package com.wison.purchase.packages.module.main.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.comm.domain.FlowAttachInfo;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.service.CompanyPurPackageTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公司级采购包
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/companyPurPackage")
@Tag(name = "公司级采购包")
@Api(tags = "公司级采购包")
public class CompanyPurPackageTController extends BaseController {
    private final CompanyPurPackageTService companyPurPackageTService;

    /**
     * 查询所有公司级采购包数据
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<CompanyPurPackageT>> list(@RequestBody CompanyPurPackageTBo bo) {
        Page<CompanyPurPackageT> companyPurPackageTPage = companyPurPackageTService.list(bo);
        Result<PageInfo<CompanyPurPackageT>> result = Result.success("查询成功");
        result.setBeans(companyPurPackageTPage.getRecords());
        result.setTotalCount((int) companyPurPackageTPage.getTotal());
        return result;
    }

    /**
     * 根据项目号查询公司级采购包数据
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<CompanyPurPackageT> selectOne(@RequestBody CompanyPurPackageTBo bo) {
        CompanyPurPackageT one = companyPurPackageTService.selectOne(bo);
        return Result.success(one);
    }

    /**
     * 批量插入公司级采购包数据
     */
    @Operation(summary = "批量插入数据")
    @PostMapping("/insertAll")
    public Result<Void> insertAll(@RequestBody List<CompanyPurPackageTBo> boList) {
        return toAjax(companyPurPackageTService.insertAll(boList), "导入");
    }

    /**
     * 插入一条公司级采购包数据
     */
    @Operation(summary = "插入一条数据")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody CompanyPurPackageTBo bo) {
        return toAjax(companyPurPackageTService.insertOne(bo), "导入");
    }

    /**
     * 批量更新公司级采购包数据
     */
    @Operation(summary = "更新所有数据")
    @PostMapping("/updateAll")
    public Result<Void> updateAll(@RequestBody List<CompanyPurPackageTBo> boList) {
        return toAjax(companyPurPackageTService.updateAll(boList), "更新");
    }

    /**
     * 根据id更新公司级采购包数据
     */
    @Operation(summary = "更新一条数据")
    @PostMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody CompanyPurPackageTBo bo) {
        return toAjax(companyPurPackageTService.updateOne(bo), "更新");
    }

    /**
     * 根据条件更新公司级采购包数据
     */
    @Operation(summary = "更新数据")
    @PostMapping("/updateByBo")
    public Result<Void> updateByBo(@RequestBody CompanyPurPackageTBo bo) {
        return toAjax(companyPurPackageTService.updateByBo(bo), "更新");
    }

    /**
     * 根据id集合删除所有公司级采购包数据
     */
    @Operation(summary = "删除所有数据")
    @PostMapping("/deleteAll")
    public Result<Void> deleteAll(@RequestBody List<String> idList) {
        return toAjax(companyPurPackageTService.deleteAll(idList), "删除");
    }

    /**
     * 根据id删除一条公司级采购包数据
     */
    @Operation(summary = "删除一条数据")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(companyPurPackageTService.deleteOne(id), "删除");
    }

    /**
     * 根据条件删除公司级采购包数据
     */
    @Operation(summary = "删除数据")
    @PostMapping("/deleteByBo")
    public Result<Void> deleteByBo(@RequestBody CompanyPurPackageTBo bo) {
        return toAjax(companyPurPackageTService.deleteByBo(bo), "删除");
    }

    /**
     * 更新或者插入公司级采购包数据
     */
    @Operation(summary = "更新或者插入数据")
    @PostMapping("/insertOrUpdate")
    public Result<Void> insertOrUpdate(@RequestBody List<CompanyPurPackageTBo> boList) {
        return toAjax(companyPurPackageTService.insertOrUpdate(boList), "导入");
    }

    /**
     * 导出公司级采购包数据
     */
    @Operation(summary = "导出excel数据")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody CompanyPurPackageTBo bo, HttpServletResponse response) throws IOException {
        bo.setPageSize(-1);
        List<CompanyPurPackageT> list = companyPurPackageTService.list(bo).getRecords();
        companyPurPackageTService.exportExcel(list, response);
    }

    /**
     * 同步公司级采购包数据
     */
    @Operation(summary = "同步数据")
    @GetMapping("/syncCompanyPurPackage")
    public Result<Void> syncCompanyPurPackage() {
        return toAjax(companyPurPackageTService.syncCompanyPurPackage(), "同步");
    }

    /**
     * 流程附带采数据
     */
    @Operation(summary = "流程附带采数据")
    @PostMapping("/flowAttachList")
    public Result<List<CompanyPurPackageT>> flowAttachList(@RequestBody FlowAttachInfo flowInfo) {
        CompanyPurPackageTBo bo = new CompanyPurPackageTBo();
        bo.setZoneCode(flowInfo.getZoneCode());
        bo.setProjectCode(flowInfo.getProjectCode());
        bo.setPageSize(-1);
        List<CompanyPurPackageT> list = companyPurPackageTService.queryList(bo).getRecords();
        return Result.success(list);
    }
}