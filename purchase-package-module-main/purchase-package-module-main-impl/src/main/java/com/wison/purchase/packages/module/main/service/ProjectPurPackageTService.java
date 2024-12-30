package com.wison.purchase.packages.module.main.service;


import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.main.domain.ProjectPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 项目级采购包服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
public interface ProjectPurPackageTService extends BaseService<ProjectPurPackageT, ProjectPurPackageT> {
    void exportExcel(List<ProjectPurPackageT> list, HttpServletResponse response) throws IOException;

    boolean importExcel(InputStream is);

    boolean insertBatch(List<CompanyPurPackageTBo> boList);
}
