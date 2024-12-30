package com.wison.purchase.packages.module.main.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.message.email.api.domain.dto.SendEmailDTO;
import com.wison.message.email.api.service.EmailDubboService;
import com.wison.purchase.packages.comm.constants.ModuleConst;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.comm.domain.FwoaAutoComplete;
import com.wison.purchase.packages.comm.domain.FwoaRes;
import com.wison.purchase.packages.comm.domain.MailInfo;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.ProjectPurPackageT;
import com.wison.purchase.packages.module.main.domain.PurPackageFlowT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.domain.request.PurPackageOaInfo;
import com.wison.purchase.packages.module.main.service.CompanyPurPackageTService;
import com.wison.purchase.packages.module.main.service.ProjectPurPackageTService;
import com.wison.purchase.packages.module.main.service.PurPackageFlowTService;
import com.wison.purchase.packages.module.system.api.service.FwoaService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目级采购包
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projectPurPackage")
@Tag(name = "项目级采购包")
@Api(tags = "项目级采购包")
public class ProjectPurPackageTController extends BaseController {

    private final ProjectPurPackageTService projectPurPackageTService;
    private final CompanyPurPackageTService companyPurPackageTService;
    private final PurPackageFlowTService purPackageFlowTService;
    private final FwoaService fwoaService;
    @DubboReference
    private EmailDubboService emailDubboService;

    @Value("${workFlowId.purPackage}")
    private String purPackageWorkFlowId;

    /**
     * 查询所有项目级采购包数据
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<CompanyPurPackageT>> list(@RequestBody CompanyPurPackageTBo bo) {
//        if (ObjectUtil.isNotNull(bo.getProjectCode())) {
//            PurPackageFlowT flowBo = new PurPackageFlowT();
//            flowBo.setModule(ModuleConst.PUR_PACKAGE);
//            PurPackageFlowT flow = purPackageFlowTService.selectOne(flowBo);
//            if (null == flow || !flow.getStatus().equals(ProcessStatusEnum.COMPLETE.status)) {
//                return Result.success(new PageDTO<>());
//            }
//        }
        Page<CompanyPurPackageT> companyPurPackageTPage = companyPurPackageTService.queryList(bo);
        Result<PageInfo<CompanyPurPackageT>> result = Result.success("查询成功");
        result.setBeans(companyPurPackageTPage.getRecords());
        result.setTotalCount((int) companyPurPackageTPage.getTotal());
        return result;
    }

    /**
     * 根据项目号查询项目级采购包数据
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<ProjectPurPackageT> selectOne(@RequestBody @Valid ProjectPurPackageT bo) {
        ProjectPurPackageT one = projectPurPackageTService.selectOne(bo);
        return Result.success(one);
    }

    /**
     * 批量插入项目级采购包数据
     */
    @Operation(summary = "批量插入数据")
    @PostMapping("/insertAll")
    public Result<Void> insertAll(@RequestBody List<ProjectPurPackageT> boList) {
        return toAjax(projectPurPackageTService.insertAll(boList), "导入");
    }

    /**
     * 插入一条项目级采购包数据
     */
    @Operation(summary = "插入一条数据")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody ProjectPurPackageT bo) {
        return toAjax(projectPurPackageTService.insertOne(bo), "导入");
    }

    /**
     * 批量更新项目级采购包数据
     */
    @Operation(summary = "更新所有数据")
    @PostMapping("/updateAll")
    public Result<Void> updateAll(@RequestBody List<ProjectPurPackageT> boList) {
        return toAjax(projectPurPackageTService.updateAll(boList), "更新");
    }

    /**
     * 根据id更新项目级采购包数据
     */
    @Operation(summary = "更新一条数据")
    @PostMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody ProjectPurPackageT bo) {
        return toAjax(projectPurPackageTService.updateOne(bo), "更新");
    }

    /**
     * 根据条件更新项目级采购包数据
     */
    @Operation(summary = "更新数据")
    @PostMapping("/updateByBo")
    public Result<Void> updateByBo(@RequestBody @Valid ProjectPurPackageT bo) {
        return toAjax(projectPurPackageTService.updateByBo(bo), "更新");
    }

    /**
     * 根据id集合删除所有项目级采购包数据
     */
    @Operation(summary = "删除所有数据")
    @PostMapping("/deleteAll")
    public Result<Void> deleteAll(@RequestBody List<String> idList) {
        return toAjax(projectPurPackageTService.deleteAll(idList), "删除");
    }

    /**
     * 根据id删除一条项目级采购包数据
     */
    @Operation(summary = "删除一条数据")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(projectPurPackageTService.deleteOne(id), "删除");
    }

    /**
     * 根据条件删除项目级采购包数据
     */
    @Operation(summary = "删除数据")
    @PostMapping("/deleteByBo")
    public Result<Void> deleteByBo(@RequestBody @Valid ProjectPurPackageT bo) {
        return toAjax(projectPurPackageTService.deleteByBo(bo), "删除");
    }

    /**
     * 更新或者插入项目级采购包数据
     */
    @Operation(summary = "更新或者插入数据")
    @PostMapping("/insertOrUpdate")
    public Result<Void> insertOrUpdate(@RequestBody List<ProjectPurPackageT> boList) {
        return toAjax(projectPurPackageTService.insertOrUpdate(boList), "导入");
    }

    /**
     * 导出公司级采购包数据
     */
    @Operation(summary = "导出excel数据")
    @PostMapping("/exportCompanyExcel")
    public void exportCompanyExcel(@RequestBody CompanyPurPackageTBo bo, HttpServletResponse response) throws IOException {
        bo.setPageSize(-1);
        List<CompanyPurPackageT> list = companyPurPackageTService.queryList(bo).getRecords();
        log.info(String.valueOf(list.size()));
        companyPurPackageTService.exportExcel(list, response);
    }

    /**
     * 导出项目级采购包数据
     */
    @Operation(summary = "导出excel数据")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody @Valid ProjectPurPackageT bo, HttpServletResponse response) throws IOException {
//        PurPackageFlowT flowBo = new PurPackageFlowT();
//        flowBo.setProjectCode(HeadInfoHoldeResult.getProjectCode());
//        flowBo.setModule(ModuleConst.PUR_PACKAGE);
//        PurPackageFlowT flow = purPackageFlowTService.selectOne(flowBo);
//        if (null == flow || !flow.getStatus().equals(ProcessStatusEnum.COMPLETE.status)) {
//            companyPurPackageTService.exportExcel(new ArrayList<>(), response);
//            return;
//        }
        bo.setPageSize(-1);
        List<ProjectPurPackageT> list = projectPurPackageTService.list(bo).getRecords();
        projectPurPackageTService.exportExcel(list, response);
    }

    /**
     * 导入项目级采购包数据
     */
    @Operation(summary = "导入excel数据")
    @PostMapping(value = "/importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Void> importExcel(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        if (ObjectUtil.isEmpty(multipartFile)) {
            return Result.error("文件不存在");
        }
        return toAjax(projectPurPackageTService.importExcel(multipartFile.getInputStream()), "导入");
    }

    /**
     * 插入多条数据
     */
    @Operation(summary = "插入多条采购包数据")
    @PostMapping(value = "/insertBatch")
    public Result<Void> importExcel(@RequestBody List<CompanyPurPackageTBo> list) throws IOException {
        return toAjax(projectPurPackageTService.insertBatch(list), "导入");
    }

    /**
     * 邮件通知多人
     */
    @Operation(summary = "邮件通知多人")
    @Parameter(name = "zoneCode", in = ParameterIn.HEADER)
    @PostMapping("/send/list")
    public Result<Void> sendList(@RequestBody @Valid List<MailInfo> mailInfos) {
        for (MailInfo mailInfo : mailInfos) {
            Map<String, Object> sendData = new HashMap<>();
            sendData.put("userName", mailInfo.getToName());
            emailDubboService.sendEmail(SendEmailDTO.builder()
                    .nikeName("一体化平台")
                    .subject("采购包初始化完成通知").templateCode("pur_package").sendData(sendData).toUser(ListUtil.toList(mailInfo.getToEmail()).toArray(new String[0])).build());
        }
        return Result.success("发送成功");
    }

    /**
     * 发起流程
     */
    @Operation(summary = "发起流程")
    @Parameter(name = "zoneCode", in = ParameterIn.HEADER)
    @PostMapping("/oa/create")
    public Result<String> request(@RequestBody @Valid PurPackageOaInfo purPackageOaInfo) throws IllegalAccessException {
        PurPackageFlowT bo = new PurPackageFlowT();
        bo.setProjectCode(purPackageOaInfo.getProjectCode());
        bo.setModule(ModuleConst.PUR_PACKAGE);
        PurPackageFlowT entity = purPackageFlowTService.selectOne(bo);
        if (null != entity && ProcessStatusEnum.REVIEWING.status.equals(entity.getStatus())) {
            return Result.error("流程在审批中");
        }
        String userId = fwoaService.getUserId();
        if (!StringUtils.startWithNumber(userId)) {
            return Result.error("创建人不存在");
        }
        purPackageOaInfo.setCreatorId(userId);
        purPackageOaInfo.setWorkFlowId(purPackageWorkFlowId);
        String requestId = fwoaService.create(purPackageOaInfo, PurPackageOaInfo.class);
        if (ObjectUtil.isEmpty(requestId) || StringUtils.startWithPattern("-\\d+", requestId)) {
            return Result.error("流程创建失败");
        }
        bo.setRequestId(requestId);
        bo.setStatus(ProcessStatusEnum.REVIEWING.status);
        if (null == entity) {
            purPackageFlowTService.insertOne(bo);
        } else {
            bo.setId(entity.getId());
            purPackageFlowTService.updateOne(bo);
        }
        return Result.success("流程创建成功", requestId);
    }

    /**
     * 流程结束通知
     */
    @Operation(summary = "流程结束通知")
    @PostMapping("/oa/response")
    public String response(@RequestBody FwoaAutoComplete fwoaAutoComplete) {
        log.info("流程结束");
        JSONObject json = JSONUtil.parseObj(fwoaAutoComplete.getJsonStr());
        String requestId = String.valueOf(json.get("requestId"));
        if (ObjectUtil.isEmpty(requestId)) {
            return FwoaRes.error("流程ID不能为空");
        }
        PurPackageFlowT bo = new PurPackageFlowT();
        bo.setRequestId(requestId);
        PurPackageFlowT entity = purPackageFlowTService.selectOne(bo);
        if (null == entity) {
            return FwoaRes.error("流程不存在");
        }
        entity.setStatus(ProcessStatusEnum.COMPLETE.status);
        purPackageFlowTService.updateOne(entity);
        return FwoaRes.ok();
    }
}