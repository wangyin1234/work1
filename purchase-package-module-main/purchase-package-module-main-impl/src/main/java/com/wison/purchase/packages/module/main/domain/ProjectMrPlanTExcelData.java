package com.wison.purchase.packages.module.main.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设计询价文件计划(project_mr_plan_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 09:51:38
 */
@Data
public class ProjectMrPlanTExcelData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业名称
     */
    @ExcelProperty(index = 1)
    private String disciplineName;
    /**
     * 专业代码
     */
    @ExcelProperty(index = 2)
    private String disciplineId;
    /**
     * 询价技术文件编号(WISON)
     */
    @ExcelProperty(index = 5)
    private String technicalDocumentNoWison;
    /**
     * 询价技术文件编号(Others)
     */
    @ExcelProperty(index = 4)
    private String technicalDocumentNoOthers;
    /**
     * 文件名称
     */
    @ExcelProperty(index = 6)
    private String fileName;
    /**
     * 主项代码
     */
    @ExcelProperty(index = 3)
    private String principalItemCode;
    /**
     * 计划发布时间
     */
    @ExcelProperty(index = 7)
    private String planDate;
    /**
     * 预计发布时间
     */
    @ExcelProperty(index = 8)
    private String forecastDate;
    /**
     * 实际发布时间
     */
    @ExcelProperty(index = 9)
    private String actualDate;
    /**
     * 合同签订计划日期
     */
    @ExcelProperty(index = 17)
    private String poPlanningDate;
    /**
     * 要求到货时间
     */
    @ExcelProperty(index = 12)
    private Date requestedDeliveryTime;
    /**
     * 设计专业负责人
     */
    @ExcelProperty(index = 13)
    private String designDisciplineLeader;
    /**
     * 采购专业工程师
     */
    @ExcelProperty(index = 14)
    private String buyer;
    /**
     * 备注
     */
    @ExcelProperty(index = 19)
    private String remarks;
    /**
     * 离岸时间
     */
    @ExcelProperty(index = 11)
    private Date fobDate;
}