package com.wison.purchase.packages.module.tbe.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierFileT;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierT;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

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
@AutoMapper(target = TbeSupplierT.class)
public class TbeSupplierTVo extends BaseEntity {


    private static final long serialVersionUID = 1L;

    /**
     * 供应商编码
     */
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

    /**
     * 文件列表
     */
    private List<TbeSupplierFileT> tbeSupplierFileList;
}