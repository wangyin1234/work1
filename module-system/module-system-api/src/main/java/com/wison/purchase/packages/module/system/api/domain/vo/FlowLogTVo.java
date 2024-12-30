package com.wison.purchase.packages.module.system.api.domain.vo;


import com.wison.purchase.packages.module.system.api.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 流程日志(flow_log_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-11-15 17:14:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowLogTVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * requestId
     */
    private String requestId;
    /**
     * 备注
     */
    private String memo;
}