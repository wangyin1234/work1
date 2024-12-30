package com.wison.purchase.packages.module.tbe.domain.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * (tbe_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TbeListBo implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 板块
     */
    private String zoneCode;

    /**
     * 项目code
     */
    private String projectCode;

    /**
     * 询价编号
     */
    private List<String> priceCheckNumbers;
}