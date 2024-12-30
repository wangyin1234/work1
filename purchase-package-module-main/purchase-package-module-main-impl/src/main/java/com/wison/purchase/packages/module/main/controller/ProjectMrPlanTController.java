package com.wison.purchase.packages.module.main.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.base.core.domain.vo.form.InitFormVO;
import com.wison.base.core.emums.FormTypeEnums;
import com.wison.message.email.api.domain.dto.SendEmailDTO;
import com.wison.message.email.api.service.EmailDubboService;
import com.wison.purchase.packages.comm.constants.ModuleConst;
import com.wison.purchase.packages.comm.domain.*;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.utils.FwoaUtils;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;
import com.wison.purchase.packages.module.main.domain.PurPackageFlowT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.domain.request.ProjectMrPlanOaInfo;
import com.wison.purchase.packages.module.main.service.CompanyPurPackageTService;
import com.wison.purchase.packages.module.main.service.ProjectMrPlanTService;
import com.wison.purchase.packages.module.main.service.PurPackageFlowTService;
import com.wison.purchase.packages.module.main.utils.FormUtil;
import com.wison.purchase.packages.module.system.api.domain.response.FwoaUserIdRes;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * 设计询价文件计划服务
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 09:58:04
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projectMrPlanT")
@Tag(name = "设计询价文件计划接口")
@Api(tags = "设计询价文件计划接口")
public class ProjectMrPlanTController extends BaseController {
    private final ProjectMrPlanTService projectMrPlanTService;
    private final PurPackageFlowTService purPackageFlowTService;
    private final FwoaService fwoaService;
    private final CompanyPurPackageTService companyPurPackageTService;
    @DubboReference
    private EmailDubboService emailDubboService;

    @Value("${workFlowId.projectMrPlanFlowId}")
    private String projectMrPlanFlowId;

    /**
     * 查询所有设计询价文件
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<ProjectMrPlanT>> list(@RequestBody ProjectMrPlanT bo) {
//        log.info(String.valueOf(bo.isView()));
//        if (!bo.isView()) {
//            PurPackageFlowT flowBo = new PurPackageFlowT();
//            flowBo.setModule(ModuleConst.PROJECT_MR_PLAN);
//            PurPackageFlowT flow = purPackageFlowTService.selectOne(flowBo);
//            if (null == flow || !flow.getStatus().equals(ProcessStatusEnum.COMPLETE.status)) {
//                return Result.success(new PageDTO<>());
//            }
//        }
        Page<ProjectMrPlanT> page = projectMrPlanTService.list(bo);
        Result<PageInfo<ProjectMrPlanT>> result = Result.success("查询成功");
        result.setBeans(page.getRecords());
        result.setTotalCount((int) page.getTotal());
        return result;
    }

    /**
     * 根据id查询数据
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<ProjectMrPlanT> selectOne(@RequestBody ProjectMrPlanT bo) {
        ProjectMrPlanT one = projectMrPlanTService.selectOne(bo);
        if (null == one) {
            return Result.error("数据不存在");
        }
        return Result.success(one);
    }

    /**
     * 插入一条项目级采购包数据
     */
    @Operation(summary = "插入一条数据")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody ProjectMrPlanT bo) {
        return toAjax(projectMrPlanTService.insertOne(bo), "插入");
    }

    /**
     * 根据id更新数据
     */
    @Operation(summary = "更新一条数据")
    @PostMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody ProjectMrPlanT bo) {
        return toAjax(projectMrPlanTService.updateOne(bo), "更新");
    }

    /**
     * 根据id删除一条项目级采购包数据
     */
    @Operation(summary = "删除一条数据")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(projectMrPlanTService.deleteOne(id), "删除");
    }

    /**
     * 导出数据
     */
    @Operation(summary = "导出excel数据")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody ProjectMrPlanT bo, HttpServletResponse response) throws IOException {
//        PurPackageFlowT flowBo = new PurPackageFlowT();
//        flowBo.setModule(ModuleConst.PROJECT_MR_PLAN);
//        PurPackageFlowT flow = purPackageFlowTService.selectOne(flowBo);
//        if (null == flow || !flow.getStatus().equals(ProcessStatusEnum.COMPLETE.status)) {
//            projectMrPlanTService.exportExcel(new ArrayList<>(), response);
//            return;
//        }
        bo.setPageSize(-1);
        List<ProjectMrPlanT> list = projectMrPlanTService.list(bo).getRecords();
        projectMrPlanTService.exportExcel(list, response);
    }

    /**
     * 导入数据
     */
    @Operation(summary = "导入excel数据")
    @PostMapping(value = "/importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Void> importExcel(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            Result.error("文件不存在");
        }
        return toAjax(projectMrPlanTService.importExcel(multipartFile.getInputStream()), "导入");
    }

    /**
     * 询价文件计划发起流程
     */
    @Operation(summary = "发起流程")
    @Parameter(name = "zoneCode", in = ParameterIn.HEADER)
    @PostMapping("/oa/create")
    public Result<String> oaCreate(@RequestBody @Valid ProjectMrPlanOaInfo projectMrPlanOaInfo) throws IllegalAccessException {
        PurPackageFlowT bo = new PurPackageFlowT();
        bo.setProjectCode(projectMrPlanOaInfo.getProjectCode());
        bo.setModule(ModuleConst.PROJECT_MR_PLAN);
        PurPackageFlowT entity = purPackageFlowTService.selectOne(bo);
        if (null != entity && ProcessStatusEnum.REVIEWING.status.equals(entity.getStatus())) {
            return Result.error("流程在审批中");
        }
        String creator = UserInfoHolder.getUserInfo().getLoginName();
        String approver = projectMrPlanOaInfo.getApprover().split("/")[1];
        List<FwoaUserIdRes> beforeList = new ArrayList<>();
        beforeList.add(FwoaUserIdRes.builder().loginName(creator).userName("创建人").build());
        beforeList.add(FwoaUserIdRes.builder().loginName(approver).userName("材料进度工程师").build());
        Map<String, FwoaUserIdRes> afterMap = fwoaService.getUserId(beforeList);
        for (String key : afterMap.keySet()) {
            FwoaUserIdRes res = afterMap.get(key);
            if (res.isError()) {
                return Result.error(res.getUserName() + "不存在");
            }
        }
        // 材料进度工程师
        projectMrPlanOaInfo.setApprover(afterMap.get(approver).getUserId());
        // 申请人
        projectMrPlanOaInfo.setCreatorId(afterMap.get(creator).getUserId());
        projectMrPlanOaInfo.setWorkFlowId(projectMrPlanFlowId);
        projectMrPlanOaInfo.setStatus("0");
        projectMrPlanOaInfo.setRequestName("设计询价文件计划流程");
        String requestId = fwoaService.create(projectMrPlanOaInfo, ProjectMrPlanOaInfo.class);
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
    public String oaResponse(@RequestBody FwoaAutoComplete fwoaAutoComplete) throws IOException {
        String requestId = FwoaUtils.commonValidate(fwoaAutoComplete);
        PurPackageFlowT bo = new PurPackageFlowT();
        bo.setRequestId(requestId);
        PurPackageFlowT entity = purPackageFlowTService.selectOne(bo);
        if (null == entity) {
            log.info("流程不存在");
            return FwoaRes.error("流程不存在");
        }
        entity.setStatus(ProcessStatusEnum.COMPLETE.status);
        int row = purPackageFlowTService.updateOne(entity);
        if (row == 0) {
            return FwoaRes.error();
        }
        log.info("状态更新成功");
        return FwoaRes.ok();
    }

    /**
     * 流程附带采数据
     */
    @Operation(summary = "流程附带采数据")
    @PostMapping("/flowAttachList")
    public Result<Page<ProjectMrPlanT>> flowAttachList(@RequestBody ProjectMrPlanT flowInfo) {
        return Result.success(projectMrPlanTService.list(flowInfo));
    }

    /**
     * 拆分数据
     */
    @Operation(summary = "拆分询价文件数据")
    @PostMapping("/splitMr")
    public Result<ProjectMrPlanT> splitMr(@RequestBody ProjectMrPlanT bo) {
        List<ProjectMrPlanT> list = projectMrPlanTService.list(bo).getRecords();
        if (list.isEmpty()) {
            return Result.error("原计划文件不存在");
        }
        list.sort(Comparator.comparing(ProjectMrPlanT::getTechnicalDocumentNoWison).reversed());
        ProjectMrPlanT entity = list.get(0);
        entity.setActualDate(null);
        entity.setPoPlanningDate(null);
        entity.setMrType("2");
        if (list.size() == 1) {
            entity.setTechnicalDocumentNoWison(entity.getTechnicalDocumentNoWison() + "-S1");
            return Result.success(entity);
        }
        String[] currentTechnicalDocumentNoWison = entity.getTechnicalDocumentNoWison().split("-");
        currentTechnicalDocumentNoWison[currentTechnicalDocumentNoWison.length - 1] = "S" + (Integer.parseInt(currentTechnicalDocumentNoWison[currentTechnicalDocumentNoWison.length - 1].substring(1)) + 1);
        entity.setTechnicalDocumentNoWison(ArrayUtil.join(currentTechnicalDocumentNoWison, "-"));
        return Result.success(entity);
    }

    /**
     * 表单查询
     */
    @Operation(summary = "表单查询")
    @GetMapping("/search/form")
    public Result<InitFormVO> searchForm() {
        List<InitFormVO> initFormVOList = new ArrayList<>();
        CompanyPurPackageTBo cbo = new CompanyPurPackageTBo();
        cbo.setPageSize(-1);
        List<CompanyPurPackageT> list = companyPurPackageTService.list(cbo).getRecords();
        initFormVOList.add(
                new InitFormVO.Builder("technicalDocumentNoWison", "询价技术文件编号(WISON)").setType(FormTypeEnums.INPUT).setData(null).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("technicalDocumentNoOthers", "询价技术文件编号(Others)").setType(FormTypeEnums.INPUT).setData(null).build()
        );
        // 专业
        initFormVOList.add(
                new InitFormVO.Builder("disciplineId", "设计专业").setType(FormTypeEnums.SELECTED).setData(FormUtil.getDisciplineSelect(list)).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("fileName", "询价技术文件名称").setType(FormTypeEnums.INPUT).setData(null).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("planDate", "计划发布时间").setType(FormTypeEnums.DATEPICKER).setData(null).build()
        );
        return Result.success("初始化成功", initFormVOList);
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
            sendData.put("projectCode", HeadInfoHolder.getProjectCode());
            emailDubboService.sendEmail(SendEmailDTO.builder()
                    .nikeName("一体化平台")
                    .subject("询价文件计划操作通知").templateCode("mr_plan").sendData(sendData).toUser(ListUtil.toList(mailInfo.getToEmail()).toArray(new String[0])).build());
        }
        return Result.success("发送成功");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledSend() {
        List<ProjectMrPlanT> mrList = projectMrPlanTService.selectAll();
        for (ProjectMrPlanT mr : mrList) {
            Date calDate = null == mr.getForecastDate() ? mr.getPlanDate() : mr.getForecastDate();
            if (ObjectUtil.isNotEmpty(mr.getActualDate()) || null == calDate) {
                continue;
            }
            int diff = (int) (new Date().getTime() - calDate.getTime()) / (1000 * 3600 * 24);
            if (diff >= -5) {
                Map<String, Object> sendData = new HashMap<>();
                sendData.put("userName", mr.getDisciplineName());
                sendData.put("projectCode", mr.getProjectCode());
                emailDubboService.sendEmail(SendEmailDTO.builder()
                        .nikeName("一体化平台")
                        .subject("询价文件计划操作通知").templateCode("mr_plan").sendData(sendData).toUser(ListUtil.toList(mr.getDisciplineName().split("/")[1] + "@wison.com").toArray(new String[0])).build());

            }
        }
    }
}