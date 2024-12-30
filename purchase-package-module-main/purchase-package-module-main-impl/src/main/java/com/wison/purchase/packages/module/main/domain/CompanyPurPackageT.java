package com.wison.purchase.packages.module.main.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 公司级采购包清单(company_pur_package_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("company_pur_package_t")
public class CompanyPurPackageT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Long mdbsn;
    /**
     * 设计专业
     */
    private String disciplineName;
    /**
     * 专业代码
     */
    private String disciplineId;
    /**
     * 大类名称
     */
    private String mdblargeName;
    /**
     * 大类code
     */
    private String mdblargeId;
    /**
     * 中类名称
     */
    private String mdbmediumName;
    /**
     * 中类code
     */
    private String mdbmediumId;
    /**
     * 小类名称
     */
    private String mdbsmallName;
    /**
     * 小类code
     */
    private String mdbsmallId;
    /**
     * 采购包编号
     */
    private String mdbPurchasePackageNo;
    /**
     * 备注
     */
    private String mdbComments;
    /**
     * 状态
     */
    private String status;
    /**
     * 同步唯一标识
     */
    private Long internalId;
    /**
     * 是否存在项目包
     */
    @TableField(exist = false)
    private String isExist;
    /**
     * 分项目号
     */
    @TableField(exist = false)
    private String subPackageNo;
}