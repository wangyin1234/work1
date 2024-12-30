package com.wison.purchase.packages.module.tbe.controller;

import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.module.tbe.service.InquiryTechnicalDocumentsTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 技术评审询价文件
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiryTechnicalDocuments")
@Tag(name = "技术评审询价文件接口")
@Api(tags = "技术评审询价文件接口")
public class InquiryTechnicalDocumentsTController extends BaseController {
    private final InquiryTechnicalDocumentsTService inquiryTechnicalDocumentsTService;

    /**
     * 删除技术评审询价文件
     */
    @Operation(summary = "删除技术评审询价文件")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(inquiryTechnicalDocumentsTService.deleteOne(id), "删除");
    }
}