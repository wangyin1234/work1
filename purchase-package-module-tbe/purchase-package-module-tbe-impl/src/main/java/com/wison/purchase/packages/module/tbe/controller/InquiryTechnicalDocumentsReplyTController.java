package com.wison.purchase.packages.module.tbe.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.module.tbe.component.AsyncService;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsReplyT;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTReplyFile;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTReplyMessage;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;
import com.wison.purchase.packages.module.tbe.enums.TbeSupplierFileEnum;
import com.wison.purchase.packages.module.tbe.service.InquiryTechnicalDocumentsReplyTService;
import com.wison.purchase.packages.module.tbe.service.TbeTService;
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
import java.util.Date;
import java.util.List;

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
@RequestMapping("/api/inquiryTechnicalDocumentsReply")
@Tag(name = "技术评审询价文件回复接口")
@Api(tags = "技术评审询价文件回复接口")
public class InquiryTechnicalDocumentsReplyTController extends BaseController {
    private final InquiryTechnicalDocumentsReplyTService inquiryTechnicalDocumentsReplyTService;
    private final TbeTService tbeTService;
    private final AsyncService asyncService;

    /**
     * 提交到供应商
     */
    @Operation(summary = "提交到供应商")
    @PostMapping("/sendToSupplier")
    public Result<Void> sendToSupplier(@RequestBody @Valid TbeTBo bo) {
        for (InquiryTechnicalDocumentsReplyT file : bo.getInquiryTechnicalDocumentsReplies()) {
            if (file.isSend()) {
                return Result.error(file.getFileName() + "文件已发送过");
            }
            file.setPriceCheckNumber(bo.getPriceCheckNumber());
            file.setCreateTime(new Date());
            file.setUpdateTime(new Date());
            file.setFileType(TbeSupplierFileEnum.INQUIRY_TECHNICAL_DOC_REPLY.type);
            file.setCreateBy(UserInfoHolder.getUserInfo().getUserName());
            file.setUpdateBy(UserInfoHolder.getUserInfo().getUserName());
            file.setSend(true);
            log.info(file.getCreateTime().toString());
        }
        boolean result = inquiryTechnicalDocumentsReplyTService.insertAll(bo.getInquiryTechnicalDocumentsReplies());
        if (result) {
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
            TbeTReplyMessage message = new TbeTReplyMessage();
            message.setProjectCode(vo.getProjectCode());
            message.setZoneCode(vo.getZoneCode());
            message.setCreateBy(UserInfoHolder.getUserInfo().getLoginName());
            message.setCreatorName(UserInfoHolder.getUserInfo().getUserName());
            message.setPriceCheckNumber(vo.getPriceCheckNumber());
            message.setFileDatas(BeanUtil.copyToList(bo.getInquiryTechnicalDocumentsReplies(), TbeTReplyFile.class));
            message.setTypeCode(TbeSupplierFileEnum.INQUIRY_TECHNICAL_DOC_REPLY.type);
            asyncService.replyToSupplier(message);
        }
        return Result.success("发送成功");
    }
}