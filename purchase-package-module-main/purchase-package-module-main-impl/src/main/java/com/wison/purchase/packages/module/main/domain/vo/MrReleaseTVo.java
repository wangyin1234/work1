package com.wison.purchase.packages.module.main.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import com.wison.purchase.packages.module.main.domain.MrReleaseFileT;
import com.wison.purchase.packages.module.main.domain.MrReleaseT;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

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
@AutoMapper(target = MrReleaseT.class)
public class MrReleaseTVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableField(exist = false)
    private String no;

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
    @NotBlank(message = "询价技术文件编号(WISON)不能为空")
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
    @NotBlank(message = "版本不能为空")
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
     * 文件列表
     */
    @Valid
    private List<MrReleaseFileT> mrReleaseFileTList;

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
    /**
     * 设计经理
     */
    @TableField(exist = false)
    private String designManagerName;
}