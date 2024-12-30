package com.wison.purchase.packages.module.main.service;

import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 设计询价文件计划服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 09:58:04
 */
public interface ProjectMrPlanTService extends BaseService<ProjectMrPlanT, ProjectMrPlanT> {

    ProjectMrPlanT selectOneByBo(ProjectMrPlanT bo);

    void exportExcel(List<ProjectMrPlanT> list, HttpServletResponse response) throws IOException;

    boolean importExcel(InputStream is);

    List<ProjectMrPlanT> selectAll();
}
