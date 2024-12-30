package com.wison.purchase.packages.module.main.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公司级采购包清单服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
public interface CompanyPurPackageTService extends BaseService<CompanyPurPackageT, CompanyPurPackageTBo> {
    Page<CompanyPurPackageT> queryList(CompanyPurPackageTBo bo);

    void exportExcel(List<CompanyPurPackageT> list, HttpServletResponse response) throws IOException;

    boolean syncCompanyPurPackage();
}
