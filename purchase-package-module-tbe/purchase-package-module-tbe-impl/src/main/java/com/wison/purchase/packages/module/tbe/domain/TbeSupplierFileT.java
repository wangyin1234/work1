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
 * (tbe_supplier_file_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-21 09:26:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbe_supplier_file_t")
public class TbeSupplierFileT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * fileName
     */
    private String fileName;
    /**
     * fileLink
     */
    private String fileLink;
    /**
     * 供应商编码
     */
    private String vendorCode;
    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    /**
     * 采购包号
     */
    private String priceCheckNumber;
    /**
     * 是否已发送到供应商
     */
    private boolean isSend;
}