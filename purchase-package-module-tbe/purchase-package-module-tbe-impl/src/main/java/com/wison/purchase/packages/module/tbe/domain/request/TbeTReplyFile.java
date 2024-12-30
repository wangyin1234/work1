package com.wison.purchase.packages.module.tbe.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wison.purchase.packages.comm.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class TbeTReplyFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    /**
     * 更新者
     */
    private String createBy;
    /**
     * 文件地址
     */
    private String fileLink;
}