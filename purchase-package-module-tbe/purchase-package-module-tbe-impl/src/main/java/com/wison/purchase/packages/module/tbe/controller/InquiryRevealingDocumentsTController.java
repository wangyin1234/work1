package com.wison.purchase.packages.module.tbe.controller;


import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.module.tbe.service.InquiryRevealingDocumentsTService;
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
 * 询价商务文件表服务控制器
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-14 10:15:01
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiryRevealingDocumentsT")
@Tag(name = "询价商务文件接口")
@Api(tags = "询价商务文件接口")
public class InquiryRevealingDocumentsTController extends BaseController {
    private final InquiryRevealingDocumentsTService inquiryRevealingDocumentsTService;

    /**
     * 删除商务文件
     */
    @Operation(summary = "删除商务文件")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(inquiryRevealingDocumentsTService.deleteOne(id), "删除");
    }
}