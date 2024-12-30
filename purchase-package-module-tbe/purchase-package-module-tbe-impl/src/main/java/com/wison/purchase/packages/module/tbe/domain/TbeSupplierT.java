package com.wison.purchase.packages.module.tbe.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (tbe_supplier_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbe_supplier_t")
public class TbeSupplierT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商编码
     */
    @NotBlank(message = "供应商编码不能为空")
    private String vendorCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 技术评审意见
     */
    private boolean technicalReviewOpinion;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 询价编号
     */
    @NotBlank(message = "采购包号不能为空")
    private String priceCheckNumber;
    /**
     * 供应商打分
     */
    private Integer tbeScore;
    /**
     * 评审结果
     */
    private String tbeResult;

    /**
     * 供应商联系人
     */
    private String contactorName;

    /**
     * 供应商联系电话
     */
    private String contactorPhone;

    /**
     * 供应商联系邮箱
     */
    private String contactorEmail;
    /**
     * 技术澄清结束
     */
    private boolean clarificationFinishFlag;

    /**
     * 采购页面供应商不参与理由
     */
    private String buyerReason;
}