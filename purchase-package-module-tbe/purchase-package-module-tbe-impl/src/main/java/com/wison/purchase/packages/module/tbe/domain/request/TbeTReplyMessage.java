package com.wison.purchase.packages.module.tbe.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class TbeTReplyMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商编码
     */
    private String vendorCode;
    /**
     * 询价编号
     */
    private String priceCheckNumber;

    /**
     * 创建人
     */
    private String creatorName;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 板块
     */
    private String zoneCode;

    /**
     * 项目code
     */
    private String projectCode;
    /**
     * 回复类型
     */
    private String typeCode;
    /**
     * 文件列表
     */
    private List<TbeTReplyFile> fileDatas;
}