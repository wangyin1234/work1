package com.wison.purchase.packages.module.main.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (pur_package_flow_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-27 16:09:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("pur_package_flow_t")
public class PurPackageFlowT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * requestId
     */
    private String requestId;
    /**
     * status
     */
    private String status;
    /**
     * 流程模块
     */
    private String module;
}