package com.wison.purchase.packages.module.main.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 设计询价文件发布(mr_release_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mr_release_t")
public class MrReleaseT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 专业code
     */
    private String disciplineId;
    /**
     * 专业
     */
    private String disciplineName;
    /**
     * 采购包编号
     */
    private String mdbPurchasePackageNo;
    /**
     * 询价技术文件编号(WISON)
     */
    private String technicalDocumentNoWison;
    /**
     * 询价技术文件编号(Others)
     */
    private String technicalDocumentNoOthers;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 设计经理
     */
    private String designManager;
    /**
     * 采购专业工程师
     */
    private String buyer;
    /**
     * 采购包名称
     */
    private String mdbPurchasePackageName;
    /**
     * 审核状态
     */
    private String reviewStatus;
    /**
     * 分配状态
     */
    private String doneStatus;
    /**
     * 版次
     */
    private String version;
    /**
     * 类别
     */
    private String category;
    /**
     * 当前处理人
     */
    private String currentHandler;
    /**
     * 编制人
     */
    private String preparer;
    /**
     * 编写发布日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date issueDate;
    /**
     * 说明
     */
    private String comment;
    /**
     * 材料控制工程师
     */
    private String materialControlEngineer;
    /**
     * 项目采购经理
     */
    private String procurementManager;
    /**
     * 流程ID
     */
    private String requestId;
    /**
     * 是否需要技术评审
     */
    private boolean needTechnical;
    /**
     * 设计专业审核人
     */
    private String designDisciplineReviewer;
    /**
     * 采购工程师邮箱
     */
    private String buyerEmail;
    /**
     * 材料控制邮箱
     */
    private String materialControlEngineerEmail;
    /**
     * 设计专业审核人邮箱
     */
    private String designDisciplineReviewerEmail;
}