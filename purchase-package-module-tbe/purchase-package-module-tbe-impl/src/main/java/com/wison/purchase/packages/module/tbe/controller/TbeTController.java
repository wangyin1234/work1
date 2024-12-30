package com.wison.purchase.packages.module.tbe.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wison.base.core.domain.Result;
import com.wison.master.data.project.api.domain.dto.ProjectPostMembers;
import com.wison.master.data.project.api.service.ProjectPeopleDubboService;
import com.wison.message.email.api.domain.dto.SendEmailDTO;
import com.wison.message.email.api.service.EmailDubboService;
import com.wison.purchase.packages.comm.constants.RoleCodeConstants;
import com.wison.purchase.packages.comm.domain.*;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.interfaces.Insert;
import com.wison.purchase.packages.comm.interfaces.Update;
import com.wison.purchase.packages.comm.utils.DateUtils;
import com.wison.purchase.packages.comm.utils.FwoaUtils;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.system.api.domain.response.FwoaUserIdRes;
import com.wison.purchase.packages.module.system.api.service.FwoaService;
import com.wison.purchase.packages.module.tbe.component.AsyncService;
import com.wison.purchase.packages.module.tbe.domain.TbeT;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeListBo;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.request.SupplierOaInfo;
import com.wison.purchase.packages.module.tbe.domain.request.TbeOaInfo;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTPushMessage;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;
import com.wison.purchase.packages.module.tbe.service.TbeTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 技术评审
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tbe")
@Tag(name = "技术评审接口")
@Api(tags = "技术评审接口")
public class TbeTController extends BaseController {
    private final TbeTService tbeTService;
    private final FwoaService fwoaService;
    private final AsyncService asyncService;

    @DubboReference
    private EmailDubboService emailDubboService;
    @DubboReference
    private ProjectPeopleDubboService projectPeopleDubboService;

    @Value("${workFlowId.tbeDesignFlowId}")
    private String tbeDesignFlowId;
    @Value("${workFlowId.tbeBuyFlowId}")
    private String tbeBuyFlowId;
    @Value("${supplier.url.pushFinish}")
    private String pushFinish;

    /**
     * 技术评审列表
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/list")
    public Result<PageInfo<TbeTVo>> list(@RequestBody TbeTBo bo) {
        Page<TbeTVo> page = tbeTService.list(bo);
        Result<PageInfo<TbeTVo>> result = Result.success("查询成功");
        result.setTotalCount((int) page.getTotal());
        List<TbeTVo> list = page.getRecords();
        if ("104".equals(HeadInfoHolder.getZoneCode()) && "2".equals(bo.getRole())) {
            list = list.stream().filter(l -> !ProcessStatusEnum.REVIEWING.status.equals(l.getDesignReviewStatus())).collect(Collectors.toList());
        }
        result.setBeans(list);
        return result;
    }

    /**
     * 技术评审列表
     */
    @Operation(summary = "查询多条数据")
    @PostMapping("/allListForContract")
    public Result<List<TbeTVo>> allListForContract(@RequestBody TbeListBo bo) {
        List<TbeTVo> list = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(bo.getPriceCheckNumbers())) {
            TbeTBo tbeTBo =  new TbeTBo();
            tbeTBo.setZoneCode(bo.getZoneCode());
            tbeTBo.setProjectCode(bo.getProjectCode());
            for (String priceCheckNumber: bo.getPriceCheckNumbers()) {
                tbeTBo.setPriceCheckNumber(priceCheckNumber);
                TbeTVo vo = tbeTService.selectOne(tbeTBo);
                list.add(vo);
            }
        }
        return Result.success("查询成功", list);
    }

    /**
     * 单条技术评审
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<TbeTVo> selectOne(@RequestBody @Validated(Default.class) @Valid TbeTBo bo) {
        return Result.success(tbeTService.selectOne(bo));
    }

    /**
     * 新增技术评审
     */
    @Operation(summary = "新增技术评审")
    @PostMapping("/insertOne")
    public Result<Void> insertOne(@RequestBody @Validated(Insert.class) @Valid TbeTBo bo) {
        return toAjax(tbeTService.insertOne(bo), "创建");
    }

    /**
     * 更新技术评审
     */
    @Operation(summary = "更新技术评审")
    @PostMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody @Validated(Update.class) @Valid TbeTBo bo) {
        TbeT entity = tbeTService.selectOneByBo(bo);
        if (null != entity) {
            if ("1".equals(bo.getRole()) && ProcessStatusEnum.REVIEWING.status.equals(entity.getDesignReviewStatus())) {
                return Result.error("设计流程在审批中");
            } else if (ProcessStatusEnum.REVIEWING.status.equals(entity.getBuyReviewStatus())) {
                return Result.error("采购流程在审批中");
            }
            if (ObjectUtil.isNotEmpty(bo.getBuyerEmail()) && ((ObjectUtil.isNotEmpty(entity.getBuyerEmail()) && !entity.getBuyerEmail().equals(bo.getBuyerEmail()) || ObjectUtil.isEmpty(entity.getBuyerEmail())))) {
                Map<String, Object> sendData = new HashMap<>();
                sendData.put("userName", bo.getBuyerName());
                emailDubboService.sendEmail(SendEmailDTO.builder()
                        .nikeName("一体化平台")
                        .subject("技术评审操作通知").templateCode("tbe").sendData(sendData).toUser(ListUtil.toList(bo.getBuyerEmail()).toArray(new String[0])).build());
            }
        }
        return toAjax(tbeTService.updateOne(bo), "更新");
    }

    /**
     * 删除技术评审
     */
    @Operation(summary = "删除技术评审")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(tbeTService.deleteOne(id), "删除");
    }

    /**
     * 提交到供应商
     */
    @Operation(summary = "提交到供应商")
    @PostMapping("/sendToSupplier")
    public Result<Void> sendToSupplier(@RequestBody @Valid TbeTBo bo) {
        TbeTVo vo = tbeTService.selectOne(bo);
        if (ProcessStatusEnum.REVIEWING.status.equals(vo.getDesignReviewStatus())) {
            return Result.error("设计流程在审批中");
        }
        if (ProcessStatusEnum.REVIEWING.status.equals(vo.getBuyReviewStatus())) {
            return Result.error("采购流程在审批中");
        }
        List<TbeSupplierTVo> tbeSuppliers = vo.getTbeSuppliers();
        if (ObjectUtil.isEmpty(tbeSuppliers)) {
            return Result.error("供应商不存在");
        }
        if (null != bo.getId()) {
            tbeTService.updateOne(bo);
        }
        tbeTService.sendToSupplier(vo);
        return Result.success("发送成功");
    }

    /**
     * 供应商打分
     */
    @Operation(summary = "供应商打分")
    @PostMapping("/sendToPur")
    public Result<Void> sendToPur(@RequestBody TbeTBo bo) {
        Date updateTime = new Date();
        bo.setUpdateTime(updateTime);
        boolean result = tbeTService.sendToPur(bo);
        if (result) {
            TbeTPushMessage data = new TbeTPushMessage();
            data.setPriceCheckNumber(bo.getPriceCheckNumber());
            data.setProjectCode(HeadInfoHolder.getProjectCode());
            data.setZoneCode(HeadInfoHolder.getZoneCode());
            data.setFinishDate(updateTime);
            asyncService.pushTechnicalBid(pushFinish, data);
        }
        return toAjax(result);
    }

    /**
     * 发起流程
     */
    @Operation(summary = "技术评审发起流程")
    @Parameter(name = "zoneCode", in = ParameterIn.HEADER)
    @PostMapping("/oa/create")
    public Result<String> oaCreate(@RequestBody @Valid TbeTBo tbeTBo) throws IllegalAccessException {
        TbeTVo entity = tbeTService.selectOne(tbeTBo);
        if (null != entity) {
            if (ProcessStatusEnum.REVIEWING.status.equals(entity.getDesignReviewStatus())) {
                return Result.error("设计流程在审批中");
            }
            if (ProcessStatusEnum.REVIEWING.status.equals(entity.getBuyReviewStatus())) {
                return Result.error("采购流程在审批中");
            }
            if (StringUtils.isEmpty(entity.getBuyer())) {
                return Result.error("采购员未分配");
            }
        }
        TbeOaInfo tbeOaInfo = new TbeOaInfo();
        tbeOaInfo.setRequestName("技术评审流程");
        String userId;
        String creator = UserInfoHolder.getUserInfo().getLoginName();
        List<FwoaUserIdRes> beforeList = new ArrayList<>();
        beforeList.add(FwoaUserIdRes.builder().loginName(creator).userName("创建人").build());
        if ("1".equals(tbeTBo.getRole())) {
            String approver = tbeTBo.getHeadOfDesign().split("/")[1];
            beforeList.add(FwoaUserIdRes.builder().loginName(approver).userName("设计专业负责人").build());
            Map<String, FwoaUserIdRes> afterMap = fwoaService.getUserId(beforeList);
            for (String key : afterMap.keySet()) {
                FwoaUserIdRes res = afterMap.get(key);
                if (res.isError()) {
                    return Result.error(res.getUserName() + "不存在");
                }
            }
            userId = afterMap.get(creator).getUserId();
            tbeOaInfo.setApprover(afterMap.get(approver).getUserId());
            tbeOaInfo.setWorkFlowId(tbeDesignFlowId);
            tbeOaInfo.setRequestName("设计询价申请流程");
            tbeOaInfo.setApplicant(userId);
        } else {
            if (null == entity) {
                return Result.error("技术评审不存在");
            }
            String buyer = entity.getBuyer();
            beforeList.add(FwoaUserIdRes.builder().loginName(buyer).userName("采购工程师").build());
            Map<String, FwoaUserIdRes> afterMap = fwoaService.getUserId(beforeList);
            for (String key : afterMap.keySet()) {
                FwoaUserIdRes res = afterMap.get(key);
                if (res.isError()) {
                    return Result.error(res.getUserName() + "不存在");
                }
            }
            userId = afterMap.get(creator).getUserId();
            tbeOaInfo.setRequestName("采购招标请申请流程");
            tbeOaInfo.setWorkFlowId(tbeBuyFlowId);
            Object[] burFiles = tbeTBo.getInquiryRevealingDocuments().stream().map(file -> "<a  href=" + file.getFileLink() + "  target=\"_blank\" >" + file.getFileName() + "</a>").toArray();
            tbeOaInfo.setInquiryFileUrl(ArrayUtil.join(burFiles, "<br/>"));
            List<SupplierOaInfo> suppliers = tbeTBo.getTbeSuppliers().stream().map(tbeSupplier -> SupplierOaInfo.builder().SupplierCode(tbeSupplier.getVendorCode()).SupplierName(tbeSupplier.getSupplierName()).contacts(tbeSupplier.getContactorName()).email(tbeSupplier.getContactorEmail()).build()).collect(Collectors.toList());
            tbeOaInfo.setSuppliers(suppliers);
            tbeOaInfo.setApplicant(afterMap.get(buyer).getUserId());
        }
        tbeOaInfo.setProjectCode("2002." + HeadInfoHolder.getProjectCode());
        tbeOaInfo.setPacketNumber(tbeTBo.getPriceCheckNumber());
        tbeOaInfo.setStatus("0");
        tbeOaInfo.setCreatorId(userId);
        if (ObjectUtil.isNotEmpty(tbeTBo.getQuotationDeadline())) {
            tbeOaInfo.setClarificationDeadline(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, tbeTBo.getQuotationDeadline()));
        }
        if (ObjectUtil.isNotEmpty(tbeTBo.getActualRevealingTechnicalProposalDate())) {
            tbeOaInfo.setTechnicalReviewTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, tbeTBo.getActualRevealingTechnicalProposalDate()));
        }
        tbeOaInfo.setInquiryRequirements(tbeTBo.getRequestForQuotation());
        Object[] designFiles = tbeTBo.getInquiryTechnicalDocuments().stream().map(file -> "<a  href=" + file.getFileLink() + "  target=\"_blank\" >" + file.getFileName() + "</a>").toArray();
        tbeOaInfo.setFileUrl(ArrayUtil.join(designFiles, "<br/>"));
        String requestId = fwoaService.create(tbeOaInfo, TbeOaInfo.class);
        if (ObjectUtil.isEmpty(requestId) || StringUtils.startWithPattern("-\\d+", requestId)) {
            log.info(requestId);
            return Result.error("流程创建失败");
        }
        int row;
        if ("1".equals(tbeTBo.getRole())) {
            tbeTBo.setDesignRequestId(requestId);
            tbeTBo.setDesignReviewStatus(ProcessStatusEnum.REVIEWING.status);
        } else {
            tbeTBo.setBuyRequestId(requestId);
            tbeTBo.setBuyReviewStatus(ProcessStatusEnum.REVIEWING.status);
        }
        if (null == entity) {
            row = tbeTService.insertOne(tbeTBo);
        } else {
            tbeTBo.setId(entity.getId());
            row = tbeTService.updateOne(tbeTBo);
        }
        if (row == 0) {
            return Result.error("创建失败");
        }
        return Result.success("流程创建成功", requestId);
    }

    /**
     * 设计流程结束通知
     */
    @Operation(summary = "设计流程结束通知")
    @PostMapping("/design/oa/response")
    public String designOaResponse(@RequestBody FwoaAutoComplete fwoaAutoComplete) {
        String requestId = FwoaUtils.commonValidate(fwoaAutoComplete);
        TbeTBo bo = new TbeTBo();
        bo.setDesignRequestId(requestId);
        TbeT entity = tbeTService.selectOneByBo(bo);
        if (null == entity) {
            log.info("设计流程不存在");
            return FwoaRes.error("设计流程不存在");
        }
        entity.setDesignReviewStatus(ProcessStatusEnum.COMPLETE.status);
        int row = tbeTService.updateOne(BeanUtil.toBean(entity, TbeTBo.class));
        if (row == 0) {
            return FwoaRes.error();
        }
        List<ProjectPostMembers> projectPostMembersList =
                projectPeopleDubboService.getProjectPostMembers(
                        entity.getZoneCode(),
                        entity.getProjectCode(),
                        RoleCodeConstants.BUY_MANAGER,
                        null);
        if (ObjectUtil.isNotEmpty(projectPostMembersList)) {
            Map<String, Object> sendData = new HashMap<>();
            sendData.put("userName", projectPostMembersList.get(0).getUserName());
            sendData.put("priceCheckNumber", entity.getPriceCheckNumber());
            emailDubboService.sendEmail(SendEmailDTO.builder()
                    .nikeName("一体化平台")
                    .subject("技术评审操作通知").templateCode("tbe_buyer").sendData(sendData).toUser(ListUtil.toList(projectPostMembersList.get(0).getEmail()).toArray(new String[0])).build());
        }
        log.info("设计状态更新成功");
        return FwoaRes.ok();
    }

    /**
     * 采购流程结束通知
     */
    @Operation(summary = "采购流程结束通知")
    @PostMapping("/buy/oa/response")
    public String buyOaResponse(@RequestBody FwoaAutoComplete fwoaAutoComplete) {
        String requestId = FwoaUtils.commonValidate(fwoaAutoComplete);
        TbeTBo bo = new TbeTBo();
        bo.setBuyRequestId(requestId);
        TbeT entity = tbeTService.selectOneByBo(bo);
        if (null == entity) {
            log.info("采购流程不存在");
            return FwoaRes.error("采购流程不存在");
        }
        entity.setBuyReviewStatus(ProcessStatusEnum.COMPLETE.status);
        int row = tbeTService.updateOne(BeanUtil.toBean(entity, TbeTBo.class));
        if (row == 0) {
            return FwoaRes.error();
        }
        bo = new TbeTBo();
        bo.setPriceCheckNumber(entity.getPriceCheckNumber());
        sendToSupplier(bo);
        log.info("采购状态更新成功");
        return FwoaRes.ok();
    }

    /**
     * 技术评审列表
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/flowAttachList")
    public Result<TbeTVo> flowAttachList(@RequestBody @Validated(Default.class) @Valid TbeTBo bo) {
        return Result.success(tbeTService.selectOne(bo));
    }

    /**
     * 技术评审列表
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/allList")
    public Result<TbeTVo> allList(@RequestBody TbeTBo bo) {
        return Result.success("查询成功", tbeTService.allList(bo));
    }
}