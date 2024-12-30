package com.wison.purchase.packages.module.system.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author yinxin
 */
public class BaseEntity implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 显示条数
     */
    private long pageSize;

    /**
     * 板块
     */
    private String zoneCode;

    /**
     * 项目code
     */
    private String projectCode;
}
