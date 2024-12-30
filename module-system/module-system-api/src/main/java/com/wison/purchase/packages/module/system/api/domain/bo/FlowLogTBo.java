package com.wison.purchase.packages.module.system.api.domain.bo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 流程日志(flow_log_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-11-15 17:14:09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FlowLogTBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * requestId
     */
    @NotBlank(message = "流程ID不能为空")
    private String requestId;
    /**
     * 备注
     */
    @NotBlank(message = "删除理由不能为空")
    private String memo;
}