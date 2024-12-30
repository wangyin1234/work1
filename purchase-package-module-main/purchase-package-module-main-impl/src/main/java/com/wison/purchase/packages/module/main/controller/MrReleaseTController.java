package com.wison.purchase.packages.module.main.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.base.core.domain.vo.form.InitFormVO;
import com.wison.base.core.emums.FormTypeEnums;
import com.wison.message.email.api.domain.dto.SendEmailDTO;
import com.wison.message.email.api.service.EmailDubboService;
import com.wison.purchase.packages.comm.domain.*;
import com.wison.purchase.packages.comm.enums.CategoryEnum;
import com.wison.purchase.packages.comm.enums.DoneStatusEnum;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.utils.DateUtils;
import com.wison.purchase.packages.comm.utils.FormUtils;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.domain.request.MrReleaseOaInfo;
import com.wison.purchase.packages.module.main.domain.vo.MrReleaseTVo;
import com.wison.purchase.packages.module.main.dubbo.SupervisionService;
import com.wison.purchase.packages.module.main.service.CompanyPurPackageTService;
import com.wison.purchase.packages.module.main.service.MrReleaseTService;
import com.wison.purchase.packages.module.main.service.ProjectMrPlanTService;
import com.wison.purchase.packages.module.main.utils.FormUtil;
import com.wison.purchase.packages.module.system.api.domain.bo.FlowLogTBo;
import com.wison.purchase.packages.module.system.api.domain.response.FwoaUserIdRes;
import com.wison.purchase.packages.module.system.api.service.FwoaService;
import com.wison.purchase.packages.module.tbe.api.domain.bo.MainTbeTBo;
import com.wison.purchase.packages.module.tbe.api.service.MainTbeService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * 设计询价文件发布服务
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mrReleaseT")
@Tag(name = "设计询价文件发布接口")
@Api(tags = "设计询价文件发布接口")
public class MrReleaseTController extends BaseController {
    private final MrReleaseTService mrReleaseTService;
    private final ProjectMrPlanTService projectMrPlanTService;
    private final FwoaService fwoaService;
    private final CompanyPurPackageTService companyPurPackageTService;
    private final SupervisionService supervisionService;
    private final MainTbeService mainTbeService;
    @DubboReference
    private EmailDubboService emailDubboService;
    @Value("${workFlowId.mrReleaseFlowId}")
    private String mrReleaseFlowId;

    /**
     * 查询所有设计询价文件
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<MrReleaseTVo>> list(@RequestBody MrReleaseTVo bo) {
        Page<MrReleaseTVo> page = mrReleaseTService.list(bo);
        Result<PageInfo<MrReleaseTVo>> result = Result.success("查询成功");
        result.setBeans(page.getRecords());
        result.setTotalCount((int) page.getTotal());
        return result;
    }

    /**
     * 根据id查询数据
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<MrReleaseTVo> selectOne(@RequestBody MrReleaseTVo bo) {
        return Result.success(mrReleaseTService.selectOne(bo));
    }

    /**
     * 根据条件查询数据
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/oneByBo")
    public Result<MrReleaseTVo> selectOneByBo(@RequestBody MrReleaseTVo bo) {
        MrReleaseTVo entity = mrReleaseTService.selectOneByBo(bo);
        if (null != entity && ProcessStatusEnum.REVIEWING.status.equals(entity.getReviewStatus())) {
            Result.error("流程在审批中");
        }
        return Result.success(entity);
    }

    /**
     * 根据id更新数据
     */
    @Operation(summary = "更新一条数据")
    @PostMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody MrReleaseTVo bo) {
        MrReleaseTVo entity = mrReleaseTService.selectOne(bo);
        if (null == entity) {
            return Result.error("数据不存在");
        }
        if (ProcessStatusEnum.REVIEWING.status.equals(entity.getReviewStatus())) {
            return Result.error("流程审批中，无法更新");
        }
        entity.setComment(bo.getComment());
        entity.setBuyer(bo.getBuyer());
        return toAjax(mrReleaseTService.updateOne(bo), "更新");
    }

    /**
     * 插入设计询价文件数据
     */
    @Operation(summary = "插入一条数据")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody MrReleaseTVo bo) {
        ProjectMrPlanT projectMrPlanTBo = new ProjectMrPlanT();
        projectMrPlanTBo.setTechnicalDocumentNoWison(bo.getTechnicalDocumentNoWison());
        ProjectMrPlanT projectMrPlanT = projectMrPlanTService.selectOneByBo(projectMrPlanTBo);
        if (null == projectMrPlanT) {
            return Result.error("询价文件不存在");
        }
        if (null == bo.getMrReleaseFileTList()) {
            return Result.error("文件不能为空");
        }
        bo.setDisciplineName(projectMrPlanT.getDisciplineName());
        return toAjax(mrReleaseTService.insertOne(bo), "导入");
    }

    /**
     * 询价文件发布发起流程
     */
    @Operation(summary = "发起流程")
    @Parameter(name = "zoneCode", in = ParameterIn.HEADER)
    @PostMapping("/oa/create")
    public Result<String> oaCreate(@RequestBody @Valid MrReleaseTVo mrReleaseTVo) throws IllegalAccessException {
        MrReleaseTVo entity = mrReleaseTService.selectOneByBo(mrReleaseTVo);
        if (null != entity) {
            if (ProcessStatusEnum.REVIEWING.status.equals(entity.getReviewStatus())) {
                return Result.error("流程在审批中");
            }
        } else {
            ProjectMrPlanT projectMrPlanTBo = new ProjectMrPlanT();
            projectMrPlanTBo.setZoneCode(HeadInfoHolder.getZoneCode());
            projectMrPlanTBo.setProjectCode(HeadInfoHolder.getProjectCode());
            projectMrPlanTBo.setTechnicalDocumentNoWison(mrReleaseTVo.getTechnicalDocumentNoWison());
            ProjectMrPlanT projectMrPlanT = projectMrPlanTService.selectOneByBo(projectMrPlanTBo);
            if (null == projectMrPlanT) {
                log.info("询价文件不存在");
                return Result.error("询价文件不存在");
            }
            if (ObjectUtil.isEmpty(projectMrPlanT.getActualDate()) && CategoryEnum.FOR_PURCHASE.code.equals(mrReleaseTVo.getCategory())) {
                projectMrPlanT.setActualDate(new Date());
                projectMrPlanTService.updateOne(projectMrPlanT);
            }
        }
        String creator = UserInfoHolder.getUserInfo().getLoginName();
        String designer = mrReleaseTVo.getDesignDisciplineReviewer().split("/")[1];
        String buyer = mrReleaseTVo.getBuyer().split("/")[1];
        String materialControlEngineer = mrReleaseTVo.getMaterialControlEngineer();
        List<FwoaUserIdRes> beforeList = new ArrayList<>();
        beforeList.add(FwoaUserIdRes.builder().loginName(creator).userName("创建人").build());
        beforeList.add(FwoaUserIdRes.builder().loginName(designer).userName("设计专业审核人").build());
        beforeList.add(FwoaUserIdRes.builder().loginName(buyer).userName("采购工程师").build());
        if (null != materialControlEngineer) {
            materialControlEngineer = materialControlEngineer.split("/")[1];
            beforeList.add(FwoaUserIdRes.builder().loginName(materialControlEngineer).userName("材料控制工程师").build());
        }
        Map<String, FwoaUserIdRes> afterMap = fwoaService.getUserId(beforeList);
        for (String key : afterMap.keySet()) {
            FwoaUserIdRes res = afterMap.get(key);
            if (res.isError()) {
                return Result.error(res.getUserName() + "不存在");
            }
        }
        String creatorId = afterMap.get(creator).getUserId();
        MrReleaseOaInfo mrReleaseOaInfo = new MrReleaseOaInfo();
        mrReleaseOaInfo.setCreatorId(creatorId);
        mrReleaseOaInfo.setWorkFlowId(mrReleaseFlowId);
        mrReleaseOaInfo.setRequestName("设计询价文件发布流程");
        mrReleaseOaInfo.setProjectCode(HeadInfoHolder.getProjectCode());
        mrReleaseOaInfo.setFlag(mrReleaseTVo.getDesignManager());
        // 申请人
        mrReleaseOaInfo.setApplicant(creatorId);
        mrReleaseOaInfo.setWisonFileNum(mrReleaseTVo.getTechnicalDocumentNoWison());
        mrReleaseOaInfo.setOtherFileNum(mrReleaseTVo.getTechnicalDocumentNoOthers());
        mrReleaseOaInfo.setFileName(mrReleaseTVo.getFileName());
        mrReleaseOaInfo.setVersion(mrReleaseTVo.getVersion());
        // 类别
        mrReleaseOaInfo.setType(CategoryEnum.findByType(mrReleaseTVo.getCategory()));
        // 编写发布日期
        mrReleaseOaInfo.setWriteDate(DateUtils.getDate());
        mrReleaseOaInfo.setRemark(mrReleaseTVo.getComment());
        Object[] files = mrReleaseTVo.getMrReleaseFileTList().stream().map(file -> "<a  href=" + file.getFileLink() + "  target=\"_blank\" >" + file.getFileName() + "</a>").toArray();
        mrReleaseOaInfo.setFileUrl(ArrayUtil.join(files, ","));
        // 是否需要技术评审
        mrReleaseOaInfo.setIsReview(mrReleaseTVo.isNeedTechnical() ? "是" : "否");
        // 设计专业审核人
        mrReleaseOaInfo.setDesigner(afterMap.get(designer).getUserId());
        // 采购工程师
        String engineers = afterMap.get(buyer).getUserId();
        // 材料控制工程师
        if (null != materialControlEngineer) {
            String materialControlEngineerId = afterMap.get(materialControlEngineer).getUserId();
            engineers = engineers + "," + materialControlEngineerId;
        }
        mrReleaseOaInfo.setEngineers(engineers);
        mrReleaseOaInfo.setStatus("0");
        String requestId = fwoaService.create(mrReleaseOaInfo, MrReleaseOaInfo.class);
        if (ObjectUtil.isEmpty(requestId) || StringUtils.startWithPattern("-\\d+", requestId)) {
            log.info(requestId);
            return Result.error("流程创建失败");
        }
        int row;
        mrReleaseTVo.setRequestId(requestId);
        mrReleaseTVo.setIssueDate(DateUtils.parseDate(mrReleaseOaInfo.getWriteDate()));
        mrReleaseTVo.setReviewStatus(ProcessStatusEnum.REVIEWING.status);
        mrReleaseTVo.setPreparer(UserInfoHolder.getUserInfo().getLoginName());
        row = mrReleaseTService.insertOne(mrReleaseTVo);
        if (row == 0) {
            return Result.error("创建失败");
        }
        return Result.success("流程创建成功", requestId);
    }

    /**
     * 流程结束通知
     */
    @Operation(summary = "流程结束通知")
    @PostMapping("/oa/response")
    public String oaResponse(@RequestBody FwoaAutoComplete fwoaAutoComplete) {
        log.info("流程结束");
        JSONObject json = JSONUtil.parseObj(fwoaAutoComplete.getJsonStr());
        String requestId = String.valueOf(json.get("requestId"));
        if (ObjectUtil.isEmpty(requestId)) {
            log.info("流程ID不能为空");
            return FwoaRes.error("流程ID不能为空");
        }
        log.info(requestId);
        MrReleaseTVo bo = new MrReleaseTVo();
        bo.setRequestId(requestId);
        MrReleaseTVo entity = mrReleaseTService.selectOneByBo(bo);
        if (null == entity) {
            log.info("流程不存在");
            return FwoaRes.error("流程不存在");
        }
        entity.setReviewStatus(ProcessStatusEnum.COMPLETE.status);
        if (CategoryEnum.FOR_PURCHASE.code.equals(entity.getCategory())) {
            entity.setDoneStatus(DoneStatusEnum.NOT_ALLOCATED.code);
        }
        mrReleaseTService.updateOne(entity);
        if (CategoryEnum.FOR_PURCHASE.code.equals(entity.getCategory()) && entity.isNeedTechnical()) {
            entity.setDoneStatus(DoneStatusEnum.NOT_ALLOCATED.code);
            MainTbeTBo mainTbeTBo = getMainTbeTBo(entity);
            mainTbeService.createTbe(mainTbeTBo);
        }
        supervisionService.pushMaterialRequisitionToSupervision(entity);
        Map<String, String> mailMap = new HashMap<>();
        mailMap.put(entity.getBuyer(), entity.getBuyerEmail());
        mailMap.put(entity.getMaterialControlEngineer(), entity.getMaterialControlEngineerEmail());
        for (String key : mailMap.keySet()) {
            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("technicalDocumentNoWison", entity.getTechnicalDocumentNoWison());
            dataMap.put("category", CategoryEnum.findByType(entity.getCategory()));
            dataList.add(dataMap);
            Map<String, Object> sendData = new HashMap<>();
            sendData.put("userName", key.split("/")[0]);
            sendData.put("mrList", dataList);
            sendData.put("projectCode", entity.getProjectCode());
            emailDubboService.sendEmail(SendEmailDTO.builder()
                    .nikeName("一体化平台")
                    .subject("询价文件发布流程结束通知").templateCode("mr_release").sendData(sendData).toUser(ListUtil.toList(key.split("/")[1] + "@wison.com").toArray(new String[0])).build());
        }
        log.info("状态更新成功");
        return FwoaRes.ok();
    }

    private MainTbeTBo getMainTbeTBo(MrReleaseTVo entity) {
        MainTbeTBo mainTbeTBo = new MainTbeTBo();
        mainTbeTBo.setPriceCheckNumber(entity.getTechnicalDocumentNoWison());
        mainTbeTBo.setCreateBy(entity.getCreateBy());
        mainTbeTBo.setUpdateBy(entity.getCreateBy());
        mainTbeTBo.setZoneCode(entity.getZoneCode());
        mainTbeTBo.setProjectCode(entity.getProjectCode());
        mainTbeTBo.setCreatorEmail(entity.getCreateBy().split("/")[1] + "@wison.com");
        mainTbeTBo.setCreatorName(entity.getCreateBy().split("/")[0]);
        mainTbeTBo.setBuyer(entity.getBuyer().split("/")[1]);
        mainTbeTBo.setBuyerEmail(entity.getBuyerEmail());
        mainTbeTBo.setBuyerName(entity.getBuyer().split("/")[0]);
        mainTbeTBo.setHeadOfDesign(entity.getDesignDisciplineReviewer());
        return mainTbeTBo;
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
        initFormVOList.add(
                new InitFormVO.Builder("doneStatus", "分配状态").setType(FormTypeEnums.SELECTED).setData(FormUtils.getDoneStatusSelect()).build()
        );
        // 专业
        initFormVOList.add(
                new InitFormVO.Builder("disciplineId", "设计专业").setType(FormTypeEnums.SELECTED).setData(FormUtil.getDisciplineSelect(list)).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("category", "类别").setType(FormTypeEnums.SELECTED).setData(FormUtils.getCategorySelect()).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("reviewStatus", "审核状态").setType(FormTypeEnums.SELECTED).setData(FormUtils.getProcessStatusSelect()).build()
        );
        initFormVOList.add(
                new InitFormVO.Builder("preparer", "编制人").setType(FormTypeEnums.INPUT).setData(null).build()
        );
        return Result.success("初始化成功", initFormVOList);
    }

    /**
     * 流程删除
     */
    @Operation(summary = "流程删除")
    @PostMapping("/oa/delete")
    public Result<Void> oaDelete(@RequestBody @Valid FlowLogTBo bo) {
        log.info("流程删除");
        fwoaService.delete(bo);
        MrReleaseTVo mrReleaseTbo = new MrReleaseTVo();
        mrReleaseTbo.setRequestId(bo.getRequestId());
        mrReleaseTService.deleteByBo(mrReleaseTbo);
        return Result.success("删除成功");
    }

    /**
     * 导出数据
     */
    @Operation(summary = "导出excel数据")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody MrReleaseTVo bo, HttpServletResponse response) throws IOException {
        bo.setPageSize(-1);
        List<MrReleaseTVo> list = mrReleaseTService.list(bo).getRecords();
        mrReleaseTService.exportExcel(list, response);
    }
}