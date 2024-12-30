package com.wison.purchase.packages.module.tbe.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * (inquiry_technical_documents_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("inquiry_technical_documents_reply_t")
public class InquiryTechnicalDocumentsReplyT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件版次
     */
    private Long documentVersion = 1L;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 询价编号
     */
    private String priceCheckNumber;
    /**
     * 文件地址
     */
    private String fileLink;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 是否已发送到供应商
     */
    private boolean isSend;
}