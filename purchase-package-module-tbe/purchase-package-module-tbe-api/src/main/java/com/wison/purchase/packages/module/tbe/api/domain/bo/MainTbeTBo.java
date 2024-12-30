package com.wison.purchase.packages.module.tbe.api.domain.bo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (tbe_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Data
@NoArgsConstructor
public class MainTbeTBo implements Serializable {


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
     * 设计专业负责人
     */
    private String headOfDesign;
    /**
     * 询价编号
     */
    private String priceCheckNumber;
    /**
     * 采购人员
     */
    private String buyer;
    /**
     * 采购人员邮箱
     */
    private String buyerEmail;
    /**
     * 采购人员名字
     */
    private String buyerName;
    /**
     * 设计人员邮箱
     */
    private String creatorEmail;
    /**
     * 设计人员名字
     */
    private String creatorName;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
}