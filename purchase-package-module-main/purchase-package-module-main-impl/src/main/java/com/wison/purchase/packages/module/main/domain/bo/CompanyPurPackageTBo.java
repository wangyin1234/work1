package com.wison.purchase.packages.module.main.domain.bo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 公司级采购包清单(company_pur_package_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CompanyPurPackageTBo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Long mdbsn;
    /**
     * 设计专业
     */
    private String disciplineName;
    /**
     * 专业代码
     */
    private String disciplineId;
    /**
     * 大类名称
     */
    private String mdblargeName;
    /**
     * 大类code
     */
    private String mdblargeId;
    /**
     * 中类名称
     */
    private String mdbmediumName;
    /**
     * 中类code
     */
    private String mdbmediumId;
    /**
     * 小类名称
     */
    private String mdbsmallName;
    /**
     * 小类code
     */
    private String mdbsmallId;
    /**
     * 采购包编号
     */
    private String mdbPurchasePackageNo;
    /**
     * 备注
     */
    private String mdbComments;
    /**
     * 状态
     */
    private String status;
    /**
     * 同步唯一标识
     */
    private Long internalId;
    /**
     * 是否存在项目包
     */
    private String isExist;
    /**
     * 分项目号
     */
    private String subPackageNo;

    /**
     * 是否存在项目包
     */
    private OrderItem orderItem;

    public CompanyPurPackageTBo(Map<String, String> map) {
        this.setMdbsn(Long.valueOf(map.get("mdbSn")));
        this.setDisciplineName(map.get("DisciplineName"));
        this.setDisciplineId(map.get("DisciplineId"));
        this.setMdblargeName(map.get("LargeName"));
        this.setMdblargeId(map.get("LargeId"));
        this.setMdbmediumName(map.get("MediumName"));
        this.setMdbmediumId(map.get("MediumId"));
        this.setMdbsmallName(map.get("SmallName"));
        this.setMdbsmallId(map.get("SmallId"));
        this.setMdbPurchasePackageNo(map.get("PwpNo"));
        this.setMdbComments(map.get("Comments"));
        this.setStatus(map.get("Status"));
        this.setInternalId(Long.valueOf(map.get("internalId")));
        this.setDisciplineName(map.get("DisciplineName"));
    }
}