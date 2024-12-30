package com.wison.purchase.packages.module.tbe.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

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
@TableName("tbe_t")
public class TbeT extends BaseEntity {

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
    private Date quotationDeadline;
    /**
     * 技术开标时间
     */
    private Date planRevealingTechnicalProposalDate;
    /**
     * 询价要求
     */
    private String requestForQuotation;
    /**
     * 要求评审反馈时间
     */
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
    private Boolean isFinish;
    /**
     * 实际技术开标时间
     */
    private Date actualRevealingTechnicalProposalDate;
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
}