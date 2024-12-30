package com.wison.purchase.packages.module.tbe.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierFileT;
import com.wison.purchase.packages.module.tbe.service.TbeSupplierFileTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 服务控制器
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-21 09:26:54
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tbeSupplierFile")
@Tag(name = "供应商文件接口")
@Api(tags = "供应商文件接口")
public class TbeSupplierFileTController extends BaseController {
    private final TbeSupplierFileTService tbeSupplierFileTService;

    /**
     * 文件列表
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<TbeSupplierFileT>> list(@RequestBody @Valid TbeSupplierFileT bo) {
        Page<TbeSupplierFileT> page = tbeSupplierFileTService.list(bo);
        Result<PageInfo<TbeSupplierFileT>> result = Result.success("查询成功");
        result.setBeans(page.getRecords());
        result.setTotalCount((int) page.getTotal());
        return result;
    }

    /**
     * 新增文件
     */
    @Operation(summary = "新增文件")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody @Valid TbeSupplierFileT bo) {
        return toAjax(tbeSupplierFileTService.insertOne(bo), "创建");
    }
}