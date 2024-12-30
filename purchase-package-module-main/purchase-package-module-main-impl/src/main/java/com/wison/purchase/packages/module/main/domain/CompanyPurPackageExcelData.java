package com.wison.purchase.packages.module.main.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公司级采购包清单(company_pur_package_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Data
public class CompanyPurPackageExcelData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty("序号")
    private Long mdbsn;
    /**
     * 设计专业
     */
    @ExcelProperty("设计专业名称")
    private String disciplineName;
    /**
     * 专业代码
     */
    @ExcelProperty("设计专业代码")
    private String disciplineId;
    /**
     * 大类名称
     */
    @ExcelProperty("大类名称")
    private String mdblargeName;
    /**
     * 大类code
     */
    @ExcelProperty("大类code")
    private String mdblargeId;
    /**
     * 中类名称
     */
    @ExcelProperty("中类名称")
    private String mdbmediumName;
    /**
     * 中类code
     */
    @ExcelProperty("中类code")
    private String mdbmediumId;
    /**
     * 小类名称
     */
    @ExcelProperty("小类名称")
    private String mdbsmallName;
    /**
     * 小类code
     */
    @ExcelProperty("小类code")
    private String mdbsmallId;
    /**
     * 采购包编号
     */
    @ExcelProperty("采购包编号")
    private String mdbPurchasePackageNo;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String mdbComments;
    /**
     * 是否存在项目包
     */
    @ExcelProperty("是否勾选Y:选择")
    private String isExist;
    /**
     * 分项目号
     */
    @ExcelProperty("分项目号")
    private String subPackageNo;
}