package com.wison.purchase.packages.module.tbe.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import com.wison.purchase.packages.comm.utils.DateUtils;
import com.wison.purchase.packages.module.tbe.domain.InquiryRevealingDocumentsReplyT;
import com.wison.purchase.packages.module.tbe.domain.InquiryRevealingDocumentsT;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsReplyT;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsT;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * (tbe_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TbeTMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 询价编号
     */
    private String priceCheckNumber;
    /**
     * versionNumber
     */
    private String versionNumber;
    /**
     * 报价截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date quotationDeadline;
    /**
     * 技术开标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date planRevealingTechnicalProposalDate;
    /**
     * 询价要求
     */
    private String requestForQuotation;
    /**
     * 要求评审反馈时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date requiredTbeDate;
    /**
     * 设计专业负责人
     */
    private String headOfDesign;
    /**
     * 编制意见
     */
    private String compilationAdvice;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件地址
     */
    private String fileLink;
    /**
     * 技术评审文件名
     */
    private String technicalReviewFileName;
    /**
     * 技术评审文件
     */
    private String technicalReviewFileLink;
    /**
     * 是否开标
     */
    private boolean isBid;
    /**
     * 实际技术开标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date actualRevealingTechnicalProposalDate;
    /**
     * 询价技术文件
     */
    private List<InquiryTechnicalDocumentsT> inquiryTechnicalDocuments;
    /**
     * 询价技术文件回复
     */
    private List<InquiryTechnicalDocumentsReplyT> inquiryTechnicalDocumentsReplies;
    /**
     * 询价商务文件
     */
    private List<InquiryRevealingDocumentsT> inquiryRevealingDocuments;
    /**
     * 询价商务文件回复
     */
    private List<InquiryRevealingDocumentsReplyT> inquiryRevealingDocumentReplies;
    /**
     * 供应商名单
     */
    private List<TbeSupplierTVo> tbeSuppliers;

    /**
     * 询价发布时间/标书发放时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date biddingPublishDate;

    /**
     * 发布标书人员Id
     */
    private String publishOfficerUserId;

    /**
     * 发布标书人员名称
     */
    private String publishOfficerUserName;
    /**
     * 设计人员邮箱
     */
    private String creatorEmail;
    /**
     * 设计人员名字
     */
    private String creatorName;
    /**
     * 采购人员
     */
    private String buyer;
    /**
     * 采购人员邮箱
     */
    private String buyerEmail;
    /**
     * 采购人员名字
     */
    private String buyerName;
}