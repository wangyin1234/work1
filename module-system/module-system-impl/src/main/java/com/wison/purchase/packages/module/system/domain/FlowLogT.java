package com.wison.purchase.packages.module.system.domain;

import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 流程日志(flow_log_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-11-15 17:14:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FlowLogT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * requestId
     */
    private String requestId;
    /**
     * 备注
     */
    private String memo;
}