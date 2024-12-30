package com.wison.purchase.packages.module.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.ExcelUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageExcelData;
import com.wison.purchase.packages.module.main.domain.MrReleaseT;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;
import com.wison.purchase.packages.module.main.domain.ProjectPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.mapper.MrReleaseTMapper;
import com.wison.purchase.packages.module.main.mapper.ProjectMrPlanTMapper;
import com.wison.purchase.packages.module.main.mapper.ProjectPurPackageTMapper;
import com.wison.purchase.packages.module.main.service.ProjectPurPackageTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 项目级采购包服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectPurPackageTServiceImpl implements ProjectPurPackageTService {
    private final ProjectPurPackageTMapper projectPurPackageTMapper;
    private final ProjectMrPlanTMapper projectMrPlanTMapper;
    private final MrReleaseTMapper mrReleaseTMapper;

    @Override
    public Page<ProjectPurPackageT> list(ProjectPurPackageT bo) {
        PageDTO<ProjectPurPackageT> page = new PageDTO<>(bo.getPageNum(), bo.getPageSize());
        return projectPurPackageTMapper.selectPage(page, buildListLqw(bo));
    }

    @Override
    public ProjectPurPackageT selectOne(ProjectPurPackageT bo) {
        return projectPurPackageTMapper.selectOne(buildOneLqw(bo));
    }

    @Override
    public int updateOne(ProjectPurPackageT bo) {
        return projectPurPackageTMapper.updateById(bo);
    }

    @Override
    public int updateByBo(ProjectPurPackageT bo) {
        return projectPurPackageTMapper.update(bo, buildOneLqw(bo));
    }

    @Override
    public boolean updateAll(List<ProjectPurPackageT> boList) {
        return projectPurPackageTMapper.updateBatchById(boList);
    }

    @Override
    public int insertOne(ProjectPurPackageT bo) {
        return 0;
    }

    @Override
    public boolean insertAll(List<ProjectPurPackageT> boList) {
        return true;
    }

    @Override
    public boolean insertBatch(List<CompanyPurPackageTBo> boList) {
        return handlerList(boList, new ArrayList<>());
    }

    @Override
    @Transactional
    public int deleteOne(String id) {
        ProjectPurPackageT projectPurPackageT = projectPurPackageTMapper.selectById(id);
        for (String key : getKeyList(projectPurPackageT)) {
            deleteMr(projectPurPackageT.getZoneCode(), projectPurPackageT.getProjectCode(), key);
        }
        return projectPurPackageTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(ProjectPurPackageT bo) {
        return projectPurPackageTMapper.delete(buildOneLqw(bo));
    }

    @Override
    @Transactional
    public int deleteAll(List<String> idList) {
        return projectPurPackageTMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean insertOrUpdate(List<ProjectPurPackageT> boList) {
        return projectPurPackageTMapper.insertOrUpdateBatch(boList);
    }

    @Override
    public void exportExcel(List<ProjectPurPackageT> list, HttpServletResponse response) throws IOException {
        ExcelUtil.exportListExcel("templates/company_pur_package.xlsx", list, response);
    }

    @Override
    @Transactional
    public boolean importExcel(InputStream is) {
        List<CompanyPurPackageTBo> importList = BeanUtil.copyToList(ExcelUtil.importExcel(is, CompanyPurPackageExcelData.class), CompanyPurPackageTBo.class);
        return handlerList(importList.stream().filter(im -> "Y".equals(im.getIsExist())).collect(Collectors.toList()), importList.stream().filter(im -> "D".equals(im.getIsExist())).collect(Collectors.toList()));
    }

    private boolean handlerList(List<CompanyPurPackageTBo> insertAndUpdateList, List<CompanyPurPackageTBo> deleteList) {
        ProjectPurPackageT existBo = new ProjectPurPackageT();
        existBo.setPageSize(-1);
        existBo.setZoneCode(HeadInfoHolder.getZoneCode());
        existBo.setProjectCode(HeadInfoHolder.getProjectCode());
        List<ProjectPurPackageT> existList = list(existBo).getRecords();
        List<ProjectPurPackageT> insertList = new ArrayList<>();
        List<ProjectMrPlanT> projectMrPlanTList = new ArrayList<>();
        LambdaQueryWrapper<ProjectPurPackageT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectPurPackageT::getZoneCode, HeadInfoHolder.getZoneCode());
        lqw.eq(ProjectPurPackageT::getProjectCode, HeadInfoHolder.getProjectCode());
        deleteList.forEach(im -> {
            existList.stream().filter(ex -> ex.getDisciplineId().equals(im.getDisciplineId()) && ex.getMdbPurchasePackageNo().equals(im.getMdbPurchasePackageNo())).findFirst().ifPresent(i -> deleteOne(String.valueOf(i.getId())));
        });
        insertAndUpdateList.forEach(i -> {
            Optional<ProjectPurPackageT> optional = existList.stream().filter(ex -> ex.getDisciplineId().equals(i.getDisciplineId()) && ex.getMdbPurchasePackageNo().equals(i.getMdbPurchasePackageNo())).findFirst();
            if (!optional.isPresent()) {
                ProjectPurPackageT entity = new ProjectPurPackageT();
                entity.setStatus("0");
                entity.setDisciplineId(i.getDisciplineId());
                entity.setMdbPurchasePackageNo(i.getMdbPurchasePackageNo());
                entity.setSubPackageNo(i.getSubPackageNo());
                insertList.add(entity);
                if (ObjectUtil.isEmpty(i.getSubPackageNo())) {
                    projectMrPlanTList.add(getProjectMrPlanT(i, ""));
                } else {
                    String[] subs = i.getSubPackageNo().split(",");
                    for (String sub : subs) {
                        projectMrPlanTList.add(getProjectMrPlanT(i, "[" + sub + "]"));
                    }
                }
            } else {
                ProjectPurPackageT existProjectPurPackageT = optional.get();
                List<String> existSubNoList = new ArrayList<>();
                List<String> currentSubNoList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(existProjectPurPackageT.getSubPackageNo())) {
                    existSubNoList = Arrays.stream(existProjectPurPackageT.getSubPackageNo().split(",")).collect(Collectors.toList());
                }
                if (ObjectUtil.isNotEmpty(i.getSubPackageNo())) {
                    currentSubNoList = Arrays.stream(i.getSubPackageNo().split(",")).collect(Collectors.toList());
                }
                List<String> deleteMrList = CollectionUtil.subtractToList(existSubNoList, currentSubNoList);
                deleteMrList.forEach(delete -> {
                    int deleteRow = deleteMr(HeadInfoHolder.getZoneCode(), HeadInfoHolder.getProjectCode(), HeadInfoHolder.getProjectCode() + "[" + delete + "]-EM0701-E" + i.getDisciplineId() + "-" + i.getMdbPurchasePackageNo());
                    if (deleteRow > 0) {
                        existProjectPurPackageT.setSubPackageNo(i.getSubPackageNo());
                        projectPurPackageTMapper.updateById(existProjectPurPackageT);
                    }
                });
                List<String> insertMrList = CollectionUtil.subtractToList(currentSubNoList, existSubNoList);
                insertMrList.forEach(insert -> projectMrPlanTList.add(getProjectMrPlanT(i, "[" + insert + "]")));
            }
        });
        boolean result = projectPurPackageTMapper.insertBatch(insertList);
        if (result) {
            projectMrPlanTMapper.insertBatch(projectMrPlanTList);
        }
        return result;
    }

    private ProjectMrPlanT getProjectMrPlanT(CompanyPurPackageTBo bo, String sub) {
        ProjectMrPlanT projectMrPlanT = new ProjectMrPlanT();
        projectMrPlanT.setTechnicalDocumentNoWison(HeadInfoHolder.getProjectCode() + sub + "-EM0701-E" + bo.getDisciplineId() + "-" + bo.getMdbPurchasePackageNo());
        projectMrPlanT.setDisciplineName(bo.getDisciplineName());
        projectMrPlanT.setMdbPurchasePackageName(bo.getMdbsmallName());
        projectMrPlanT.setMdbPurchasePackageNo(bo.getMdbPurchasePackageNo());
        projectMrPlanT.setDisciplineId(bo.getDisciplineId());
        projectMrPlanT.setPrincipalItemCode(sub.startsWith("[") ? sub.substring(1, sub.length() - 1) : sub);
        projectMrPlanT.setMrType("1");
        return projectMrPlanT;
    }

    private LambdaQueryWrapper<ProjectPurPackageT> buildListLqw(ProjectPurPackageT bo) {
        LambdaQueryWrapper<ProjectPurPackageT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectPurPackageT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(ProjectPurPackageT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        return lqw;
    }

    private LambdaQueryWrapper<ProjectPurPackageT> buildOneLqw(ProjectPurPackageT bo) {
        LambdaQueryWrapper<ProjectPurPackageT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectPurPackageT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(ProjectPurPackageT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.eq(StringUtils.isNotBlank(bo.getMdbPurchasePackageNo()), ProjectPurPackageT::getMdbPurchasePackageNo, bo.getMdbPurchasePackageNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDisciplineId()), ProjectPurPackageT::getDisciplineId, bo.getDisciplineId());
        return lqw;
    }

    private int deleteMr(String zoneCode, String projectCode, String key) {
        LambdaQueryWrapper<ProjectMrPlanT> projectMrPlanTLqw = new LambdaQueryWrapper<>();
        projectMrPlanTLqw.eq(ProjectMrPlanT::getZoneCode, zoneCode);
        projectMrPlanTLqw.eq(ProjectMrPlanT::getProjectCode, projectCode);
        projectMrPlanTLqw.eq(ProjectMrPlanT::getTechnicalDocumentNoWison, key);
        ProjectMrPlanT pr = projectMrPlanTMapper.selectOne(projectMrPlanTLqw);
        if (null == pr) {
            throw new BusinessException("当前采购包关联询价文件不存在：" + key);
        }
        if (null != pr.getActualDate()) {
            throw new BusinessException("当前采购包关联询价文件已发布，不允许删除: " + key);
        } else {
            LambdaQueryWrapper<MrReleaseT> mrReleaseTLqw = new LambdaQueryWrapper<>();
            mrReleaseTLqw.eq(MrReleaseT::getZoneCode, pr.getZoneCode());
            mrReleaseTLqw.eq(MrReleaseT::getProjectCode, pr.getProjectCode());
            mrReleaseTLqw.eq(MrReleaseT::getTechnicalDocumentNoWison, pr.getTechnicalDocumentNoWison());
            List<MrReleaseT> mrReleaseTList = mrReleaseTMapper.selectList(mrReleaseTLqw);
            if (!mrReleaseTList.isEmpty()) {
                throw new BusinessException("当前采购包已有流程，不允许删除: " + key);
            }
        }
        return projectMrPlanTMapper.deleteById(pr.getId());
    }

    private List<String> getKeyList(ProjectPurPackageT bo) {
        List<String> keyList = new ArrayList<>();
        if (ObjectUtil.isEmpty(bo.getSubPackageNo())) {
            keyList.add(HeadInfoHolder.getProjectCode() + "-EM0701-E" + bo.getDisciplineId() + "-" + bo.getMdbPurchasePackageNo());
        } else {
            String[] subs = bo.getSubPackageNo().split(",");
            for (String sub : subs) {
                keyList.add(HeadInfoHolder.getProjectCode() + "[" + sub + "]-EM0701-E" + bo.getDisciplineId() + "-" + bo.getMdbPurchasePackageNo());
            }
        }
        return keyList;
    }
}