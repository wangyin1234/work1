package com.wison.purchase.packages.module.tbe.domain.bo;

import com.wison.purchase.packages.comm.domain.BaseEntity;
import com.wison.purchase.packages.comm.interfaces.Insert;
import com.wison.purchase.packages.comm.interfaces.Update;
import com.wison.purchase.packages.module.tbe.domain.*;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
public class TbeTBo extends BaseEntity {


    private static final long serialVersionUID = 1L;

    /**
     * 询价编号
     */
    @NotBlank(message = "采购包号不能为空")
    private String priceCheckNumber;
    /**
     * versionNumber
     */
    private String versionNumber;
    /**
     * 报价截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quotationDeadline;
    /**
     * 技术开标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planRevealingTechnicalProposalDate;
    /**
     * 询价要求
     */
    private String requestForQuotation;
    /**
     * 要求评审反馈时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 是否结束
     */
    private boolean isFinish;
    /**
     * 实际技术开标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 技术评审文件
     */
    private List<TbeResultFileT> tbeResultFiles;
    /**
     * 技术评审意见
     */
    private boolean technicalReviewOpinion;
    /**
     * 角色（1：设计 2：采购）
     */
    @NotBlank(message = "角色不能为空", groups = {Insert.class, Update.class})
    private String role;
    /**
     * 设计审批
     */
    private String designRequestId;
    /**
     * 设计审核状态
     */
    private String designReviewStatus;
    /**
     * 采购审批
     */
    private String buyRequestId;
    /**
     * 采购审核状态
     */
    private String buyReviewStatus;
    /**
     * 公司code
     */
    private String companyCode;
    /**
     * 项目code（4段）
     */
    private String code;
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