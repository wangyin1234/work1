package com.wison.purchase.packages.comm.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author yinxin
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    private Long id;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date updateTime;

    /**
     * 当前页
     */
    @TableField(exist = false)
    @ExcelIgnore
    private long pageNum;

    /**
     * 显示条数
     */
    @TableField(exist = false)
    @ExcelIgnore
    private long pageSize;

    /**
     * 板块
     */
    @TableField(fill = FieldFill.INSERT)
    private String zoneCode;

    /**
     * 项目code
     */
    @TableField(fill = FieldFill.INSERT)
    private String projectCode;
}
