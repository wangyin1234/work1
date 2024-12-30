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

/**
 * (tbe_result_file_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-22 13:21:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbe_result_file_t")
public class TbeResultFileT extends BaseEntity {

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
     * 采购包号
     */
    private String priceCheckNumber;

}