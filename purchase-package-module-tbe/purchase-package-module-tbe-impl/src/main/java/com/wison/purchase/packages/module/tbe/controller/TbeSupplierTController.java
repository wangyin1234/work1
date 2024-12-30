package com.wison.purchase.packages.module.tbe.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wison.base.core.domain.Result;
import com.wison.message.email.api.domain.dto.SendEmailDTO;
import com.wison.message.email.api.service.EmailDubboService;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import com.wison.purchase.packages.module.tbe.component.AsyncService;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierFileT;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.request.TbeSendMessage;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTPushMessage;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTReplyFile;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTReplyMessage;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;
import com.wison.purchase.packages.module.tbe.enums.TbeSupplierFileEnum;
import com.wison.purchase.packages.module.tbe.service.TbeSupplierFileTService;
import com.wison.purchase.packages.module.tbe.service.TbeSupplierTService;
import com.wison.purchase.packages.module.tbe.service.TbeTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 技术评审供应商
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tbeSupplier")
@Tag(name = "技术评审供应商接口")
@Api(tags = "技术评审供应商接口")
public class TbeSupplierTController extends BaseController {
    private final TbeTService tbeTService;
    private final TbeSupplierTService tbeSupplierTService;
    private final TbeSupplierFileTService tbeSupplierFileTService;
    private final AsyncService asyncService;
    @DubboReference
    private EmailDubboService emailDubboService;

    @Value("${supplier.url.pushClarification}")
    private String pushClarification;
    @Value("${supplier.url.pushSupplierParticipateFlag}")
    private String pushSupplierParticipateFlag;

    /**
     * 单条技术评审
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<TbeSupplierTVo> selectOne(@RequestBody @Valid TbeSupplierTVo bo) {
        return Result.success(tbeSupplierTService.selectOne(bo));
    }

    /**
     * 单条技术评审
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/deleteByBo")
    public Result<TbeSupplierTVo> deleteByBo(@RequestBody @Valid TbeSupplierTVo bo) {
        TbeSupplierFileT fileBo = BeanUtil.toBean(bo, TbeSupplierFileT.class);
        tbeSupplierFileTService.deleteByBo(fileBo);
        return Result.success("");
    }

    /**
     * 删除技术评审询价文件
     */
    @Operation(summary = "删除供应商")
    @DeleteMapping("/deleteOne/{id}")
    public Result<Void> deleteOne(@PathVariable(name = "id") String id) {
        return toAjax(tbeSupplierTService.deleteOne(id), "删除");
    }

    /**
     * 提交到供应商
     */
    @Operation(summary = "提交到供应商")
    @PostMapping("/sendToSupplier")
    public Result<Void> sendToSupplier(@RequestBody @Valid TbeSupplierTVo bo) {
        for (TbeSupplierFileT file : bo.getTbeSupplierFileList()) {
            if (file.isSend()) {
                return Result.error(file.getFileName() + "文件已发送过");
            }
            file.setVendorCode(bo.getVendorCode());
            file.setPriceCheckNumber(bo.getPriceCheckNumber());
            file.setSend(true);
            file.setCreateTime(new Date());
            file.setUpdateTime(new Date());
            log.info(file.getCreateTime().toString());
        }
        boolean result = tbeSupplierFileTService.insertAll(bo.getTbeSupplierFileList());
        if (result) {
            TbeSupplierTVo entity = tbeSupplierTService.selectOne(bo);
            if (ObjectUtil.isEmpty(entity)) {
                return Result.error("供应商不存在");
            }
            if (!entity.isTechnicalReviewOpinion()) {
                return Result.error("该供应商已不再参与");
            }
            TbeTReplyMessage message = new TbeTReplyMessage();
            message.setProjectCode(entity.getProjectCode());
            message.setZoneCode(entity.getZoneCode());
            message.setVendorCode(entity.getVendorCode());
            message.setCreateBy(UserInfoHolder.getUserInfo().getLoginName());
            message.setCreatorName(UserInfoHolder.getUserInfo().getUserName());
            message.setPriceCheckNumber(entity.getPriceCheckNumber());
            message.setFileDatas(BeanUtil.copyToList(bo.getTbeSupplierFileList(), TbeTReplyFile.class));
            message.setTypeCode(bo.getTbeSupplierFileList().get(0).getFileType());
            asyncService.replyToSupplier(message);
        }
        return Result.success("发送成功");
    }

    /**
     * 更新供应商数据
     */
    @Operation(summary = "更新供应商数据")
    @PostMapping("/uploadFile")
    public Result<Void> uploadFile(@RequestBody @Valid TbeSupplierTVo bo) {
        List<TbeSupplierFileT> tbeSupplierFileList = bo.getTbeSupplierFileList();
        if (ObjectUtil.isEmpty(tbeSupplierFileList)) {
            return Result.error("必须上传文件");
        }
        TbeTBo tbeBo = new TbeTBo();
        tbeBo.setPriceCheckNumber(bo.getPriceCheckNumber());
        log.info(bo.getPriceCheckNumber());
        TbeTVo vo = tbeTService.selectOne(tbeBo);
        List<TbeSupplierTVo> suppliers = vo.getTbeSuppliers();
        if (ObjectUtil.isEmpty(suppliers)) {
            return Result.error("评审供应商不存在");
        }
        Optional<TbeSupplierTVo> optional = suppliers.stream().filter(s -> s.getVendorCode().equals(bo.getVendorCode())).findFirst();
        if (!optional.isPresent()) {
            return Result.error("该供应商不存在");
        }
        TbeSupplierTVo entity = optional.get();
        Map<String, String> fileTypes = new HashMap<>();
        tbeSupplierFileList.forEach(tbeSupplierFile -> {
            tbeSupplierFile.setZoneCode(bo.getZoneCode());
            tbeSupplierFile.setProjectCode(bo.getProjectCode());
            tbeSupplierFile.setPriceCheckNumber(bo.getPriceCheckNumber());
            tbeSupplierFile.setVendorCode(bo.getVendorCode());
            tbeSupplierFile.setUpdateTime(tbeSupplierFile.getUpdateTime());
            tbeSupplierFile.setCreateTime(tbeSupplierFile.getCreateTime());
            tbeSupplierFile.setCreateBy(tbeSupplierFile.getCreateBy());
            tbeSupplierFile.setUpdateBy(tbeSupplierFile.getUpdateBy());
            log.info(tbeSupplierFile.getCreateTime().toString());
            fileTypes.put(tbeSupplierFile.getFileType(), TbeSupplierFileEnum.findByResult(tbeSupplierFile.getFileType()));
        });
        List<String> loads = new ArrayList<>();
        loads.add(entity.getSupplierName());
        loads.add(entity.getProjectCode());
        loads.add(entity.getPriceCheckNumber());
        List<String> values = new ArrayList<>();
        for (String key : fileTypes.keySet()) {
            values.add(fileTypes.get(key));
        }
        loads.add(ArrayUtil.join(values.toArray(), ","));
        emailDubboService.sendEmail(SendEmailDTO.builder()
                .nikeName("一体化平台")
                .subject("技术评审操作通知").templateCode("tbe_file").loads(loads.toArray(new String[0])).toUser(ListUtil.toList(vo.getCreatorEmail()).toArray(new String[0])).ccUser(ListUtil.toList(vo.getBuyerEmail()).toArray(new String[0])).build());
        return toAjax(tbeSupplierFileTService.insertAll(tbeSupplierFileList), "回复");
    }

    /**
     * 更新供应商数据
     */
    @Operation(summary = "技术澄清是否结束")
    @PostMapping("/updateStatus")
    public Result<TbeSupplierTVo> updateStatus(@RequestBody @Valid TbeSupplierTVo bo) {
        TbeSupplierTVo entity = tbeSupplierTService.selectOne(bo);
        if (null == entity) {
            return Result.error("供应商不存在");
        }
        Date updateTime = new Date();
        entity.setUpdateTime(updateTime);
        entity.setClarificationFinishFlag(bo.isClarificationFinishFlag());
        int row = tbeSupplierTService.updateOne(entity);
        if (row > 0) {
            TbeTPushMessage data = new TbeTPushMessage();
            data.setVendorCode(bo.getVendorCode());
            data.setPriceCheckNumber(bo.getPriceCheckNumber());
            data.setProjectCode(HeadInfoHolder.getProjectCode());
            data.setZoneCode(HeadInfoHolder.getZoneCode());
            data.setClarificationFinishDate(updateTime);
            data.setClarificationFinishFlag(bo.isClarificationFinishFlag());
            data.setTechnicalReviewOpinion(bo.isTechnicalReviewOpinion());
            asyncService.pushTechnicalBid(pushClarification, data);
        }
        String statusName = bo.isClarificationFinishFlag() ? "关闭" : "开启";
        return row > 0 ? Result.success(statusName + "成功", bo) : Result.error(statusName + "失败");
    }

    /**
     * 更新供应商数据
     */
    @Operation(summary = "供应商是否参与")
    @PostMapping("/updateTechnicalReviewOpinion")
    public Result<TbeSupplierTVo> technicalReviewOpinion(@RequestBody @Valid TbeSupplierTVo bo) {
        TbeSupplierTVo entity = tbeSupplierTService.selectOne(bo);
        if (null == entity) {
            return Result.error("供应商不存在");
        }
        Date updateTime = new Date();
        entity.setUpdateTime(updateTime);
        entity.setTechnicalReviewOpinion(bo.isTechnicalReviewOpinion());
        entity.setBuyerReason(bo.getBuyerReason());
        int row = tbeSupplierTService.updateOne(entity);
        if (row > 0) {
            TbeSendMessage data = new TbeSendMessage();
            data.setVendorCode(bo.getVendorCode());
            data.setPriceCheckNumber(bo.getPriceCheckNumber());
            data.setProjectCode(HeadInfoHolder.getProjectCode());
            data.setZoneCode(HeadInfoHolder.getZoneCode());
            data.setParticipateFlagUpdateDate(updateTime);
            data.setParticipateFlag(bo.isTechnicalReviewOpinion());
            asyncService.pushTechnicalBid(pushSupplierParticipateFlag, data);
        }
        String statusName = !bo.isTechnicalReviewOpinion() ? "关闭" : "开启";
        return row > 0 ? Result.success(statusName + "成功", bo) : Result.error(statusName + "失败");
    }
}