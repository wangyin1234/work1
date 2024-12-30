package com.wison.purchase.packages.module.main.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.comm.domain.BaseMapperPlus;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 公司级采购包清单(company_pur_package_t)数据Mapper
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Mapper
public interface CompanyPurPackageTMapper extends BaseMapperPlus<CompanyPurPackageT, CompanyPurPackageT> {
    Page<CompanyPurPackageT> queryList(@Param("pg") Page<CompanyPurPackageT> pg, @Param("bo") CompanyPurPackageTBo bo);
}
