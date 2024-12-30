package com.wison.purchase.packages.module.main.domain;

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
 * 项目级采购包(project_pur_package_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_pur_package_t")
public class ProjectPurPackageT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司级采购包id
     */
    private String purPckId;
    /**
     * 状态
     */
    private String status;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 专业code
     */
    private String disciplineId;
    /**
     * 设计专业
     */
    private String disciplineName;
    /**
     * 采购包编号
     */
    private String mdbPurchasePackageNo;
    /**
     * 分项目号
     */
    private String subPackageNo;
}