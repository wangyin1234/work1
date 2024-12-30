package com.wison.purchase.packages.module.main.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.wison.purchase.packages.comm.convert.ExcelDateConvert;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 设计询价文件计划(project_mr_plan_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 09:51:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("project_mr_plan_t")
public class ProjectMrPlanT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableField(exist = false)
    private String no;
    /**
     * 公司级采购包id
     */
    private String purPckId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 专业
     */
    private String disciplineName;
    /**
     * 专业code
     */
    private String disciplineId;
    /**
     * 采购包编号
     */
    private String mdbPurchasePackageNo;
    /**
     * 询价技术文件编号(WISON)
     */
    @ExcelProperty("*询价技术文件编号(WISON)")
    private String technicalDocumentNoWison;
    /**
     * 询价技术文件编号(Others)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String technicalDocumentNoOthers;
    /**
     * 文件名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fileName;
    /**
     * 主项代码
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String principalItemCode;
    /**
     * 计划发布时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @ExcelProperty(index = 7, converter = ExcelDateConvert.class)
    @DateTimeFormat("yyyy-MM-dd")
    @JsonSerialize(using = DateSerializer.class)
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date planDate;
    /**
     * 预计发布时间
     */
    @ExcelProperty(index = 8, converter = ExcelDateConvert.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat("yyyy-MM-dd")
    @JsonSerialize(using = DateSerializer.class)
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date forecastDate;
    /**
     * 实际发布时间
     */
    @ExcelProperty(index = 9, converter = ExcelDateConvert.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date actualDate;
    /**
     * 超期(天)
     */
    private String overdue;
    /**
     * 合同签订计划日期
     */
    @ExcelProperty(index = 17, converter = ExcelDateConvert.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date poPlanningDate;
    /**
     * 要求到货时间
     */
    @ExcelProperty(index = 12, converter = ExcelDateConvert.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date requestedDeliveryTime;
    /**
     * 设计专业负责人
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String designDisciplineLeader;
    /**
     * 采购专业工程师
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String buyer;
    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remarks;
    /**
     * 采购包名称
     */
    private String mdbPurchasePackageName;
    /**
     * 流程ID
     */
    private String requestId;
    /**
     * 是否查看
     */
    @TableField(exist = false)
    private boolean isView;

    /**
     * 文件类别(主包：1，子包：2)
     */
    private String mrType;
    /**
     * 离岸时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ExcelProperty(index = 11, converter = ExcelDateConvert.class)
    private Date fobDate;
}