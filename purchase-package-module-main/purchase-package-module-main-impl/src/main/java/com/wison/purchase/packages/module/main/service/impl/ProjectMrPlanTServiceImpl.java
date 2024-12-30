package com.wison.purchase.packages.module.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.DateUtils;
import com.wison.purchase.packages.comm.utils.ExcelUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.MrReleaseT;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanTExcelData;
import com.wison.purchase.packages.module.main.mapper.MrReleaseTMapper;
import com.wison.purchase.packages.module.main.mapper.ProjectMrPlanTMapper;
import com.wison.purchase.packages.module.main.service.ProjectMrPlanTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 设计询价文件计划服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 09:58:04
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectMrPlanTServiceImpl implements ProjectMrPlanTService {
    private final ProjectMrPlanTMapper projectMrPlanTMapper;
    private final MrReleaseTMapper mrReleaseTMapper;

    private static ProjectMrPlanT getProjectMrPlanT(ProjectMrPlanT c, ProjectMrPlanT updateData) {
        updateData.setTechnicalDocumentNoOthers(c.getTechnicalDocumentNoOthers());
        updateData.setPrincipalItemCode(c.getPrincipalItemCode());
        updateData.setFileName(c.getFileName());
        updateData.setPlanDate(c.getPlanDate());
        updateData.setForecastDate(c.getForecastDate());
        updateData.setPoPlanningDate(c.getPoPlanningDate());
        updateData.setRequestedDeliveryTime(c.getRequestedDeliveryTime());
        updateData.setDesignDisciplineLeader(c.getDesignDisciplineLeader());
        updateData.setBuyer(c.getBuyer());
        updateData.setRemarks(c.getRemarks());
        updateData.setFobDate(c.getFobDate());
        return updateData;
    }

    @Override
    public Page<ProjectMrPlanT> list(ProjectMrPlanT bo) {
        Page<ProjectMrPlanT> page = projectMrPlanTMapper.selectPage(new PageDTO<>(bo.getPageNum(), bo.getPageSize()), buildListLqw(bo));
        page.getRecords().forEach(p ->
                p.setOverdue(getOverdue(p))
        );
        return page;
    }

    @Override
    public ProjectMrPlanT selectOne(ProjectMrPlanT bo) {
        ProjectMrPlanT entity = projectMrPlanTMapper.selectById(bo.getId());
        entity.setOverdue(getOverdue(bo));
        return projectMrPlanTMapper.selectById(bo.getId());
    }

    @Override
    public int updateOne(ProjectMrPlanT bo) {
        return projectMrPlanTMapper.updateById(bo);
    }

    @Override
    public int updateByBo(ProjectMrPlanT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<ProjectMrPlanT> boList) {
        return false;
    }

    @Override
    public int insertOne(ProjectMrPlanT bo) {
        return projectMrPlanTMapper.insert(bo);
    }

    @Override
    public boolean insertAll(List<ProjectMrPlanT> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        ProjectMrPlanT entity = projectMrPlanTMapper.selectById(id);
        LambdaQueryWrapper<MrReleaseT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MrReleaseT::getZoneCode, entity.getZoneCode());
        lqw.eq(MrReleaseT::getProjectCode,entity.getProjectCode());
        lqw.eq(MrReleaseT::getTechnicalDocumentNoWison, entity.getTechnicalDocumentNoWison());
        List<MrReleaseT> mrList = mrReleaseTMapper.selectList(lqw);
        if (ObjectUtil.isNotEmpty(mrList)) {
            throw new BusinessException("该询价文件编号已发布，不允许删除");
        }
        return projectMrPlanTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(ProjectMrPlanT bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return projectMrPlanTMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean insertOrUpdate(List<ProjectMrPlanT> boList) {
        return false;
    }

    private LambdaQueryWrapper<ProjectMrPlanT> buildListLqw(ProjectMrPlanT bo) {
        LambdaQueryWrapper<ProjectMrPlanT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectMrPlanT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(ProjectMrPlanT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.like(StringUtils.isNotBlank(bo.getTechnicalDocumentNoWison()), ProjectMrPlanT::getTechnicalDocumentNoWison, bo.getTechnicalDocumentNoWison());
        lqw.like(StringUtils.isNotBlank(bo.getTechnicalDocumentNoOthers()), ProjectMrPlanT::getTechnicalDocumentNoOthers, bo.getTechnicalDocumentNoOthers());
        lqw.like(StringUtils.isNotBlank(bo.getDisciplineName()), ProjectMrPlanT::getDisciplineName, bo.getDisciplineName());
        lqw.eq(StringUtils.isNotBlank(bo.getDisciplineId()), ProjectMrPlanT::getDisciplineId, bo.getDisciplineId());
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), ProjectMrPlanT::getFileName, bo.getFileName());
        lqw.eq(null != bo.getPlanDate(), ProjectMrPlanT::getPlanDate, bo.getPlanDate());
        return lqw;
    }

    private String getOverdue(ProjectMrPlanT entity) {
        Date calDate = null == entity.getForecastDate() ? entity.getPlanDate() : entity.getForecastDate();
        return null != calDate && null == entity.getActualDate() && new Date().after(calDate) ? String.valueOf(DateUtils.differentDaysByMillisecond(new Date(), calDate)) : "0";
    }

    @Override
    public ProjectMrPlanT selectOneByBo(ProjectMrPlanT bo) {
        return projectMrPlanTMapper.selectOne(buildOneLqw(bo));
    }

    @Override
    public void exportExcel(List<ProjectMrPlanT> list, HttpServletResponse response) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNo(String.valueOf(i + 1));
        }
        Map<String, String> params = new HashMap<>();
        params.put("projectCode", HeadInfoHolder.getProjectCode());
        ExcelUtil.exportListExcel("templates/project_mr_plan.xlsx", list, response, params);
    }

    @Override
    public boolean importExcel(InputStream is) {
        List<ProjectMrPlanTExcelData> imports = ExcelUtil.importExcel(is, ProjectMrPlanTExcelData.class, 2);
        ProjectMrPlanT existBo = new ProjectMrPlanT();
        existBo.setPageSize(-1);
        String projectCode = HeadInfoHolder.getProjectCode();
        String zoneCode = HeadInfoHolder.getZoneCode();
        existBo.setProjectCode(projectCode);
        existBo.setZoneCode(zoneCode);
        List<ProjectMrPlanT> list = list(existBo).getRecords();
        List<ProjectMrPlanT> importList = BeanUtil.copyToList(imports, ProjectMrPlanT.class);
        List<ProjectMrPlanT> handlerList = new ArrayList<>();
        importList.forEach(c -> {
            if (ObjectUtil.isEmpty(c.getDisciplineId())) {
                throw new BusinessException("专业代码不能为空");
            }
            if (ObjectUtil.isEmpty(c.getDisciplineName())) {
                throw new BusinessException("专业名称不能为空");
            }
            if (ObjectUtil.isEmpty(c.getTechnicalDocumentNoWison())) {
                throw new BusinessException("询价技术文件编号(WISON)不能为空");
            }
            if (ObjectUtil.isEmpty(c.getFileName())) {
                throw new BusinessException("设计(或业主)询价技术文件名称不能为空");
            }
            if (ObjectUtil.isEmpty(c.getDesignDisciplineLeader())) {
                throw new BusinessException("设计专业负责人不能为空");
            }
            Optional<ProjectMrPlanT> optional = list.stream().filter(e -> c.getTechnicalDocumentNoWison().equals(e.getTechnicalDocumentNoWison())).findFirst();
            if (optional.isPresent()) {
                ProjectMrPlanT updateData = getProjectMrPlanT(c, optional.get());
                handlerList.add(updateData);
            } else {
                String[] wisons = c.getTechnicalDocumentNoWison().split("-");
                if (Pattern.matches("^S[0-9]+$", wisons[wisons.length - 1])) {
                    c.setMrType("2");
                }
                handlerList.add(c);
            }
        });
        return projectMrPlanTMapper.insertOrUpdateBatch(handlerList);
    }

    private LambdaQueryWrapper<ProjectMrPlanT> buildOneLqw(ProjectMrPlanT bo) {
        LambdaQueryWrapper<ProjectMrPlanT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectMrPlanT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(ProjectMrPlanT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTechnicalDocumentNoWison()), ProjectMrPlanT::getTechnicalDocumentNoWison, bo.getTechnicalDocumentNoWison());
        return lqw;
    }

    @Override
    public List<ProjectMrPlanT> selectAll() {
        List<ProjectMrPlanT> list = projectMrPlanTMapper.selectList(new LambdaQueryWrapper<>());
        list.forEach(p ->
                p.setOverdue(getOverdue(p))
        );
        return list;
    }
}