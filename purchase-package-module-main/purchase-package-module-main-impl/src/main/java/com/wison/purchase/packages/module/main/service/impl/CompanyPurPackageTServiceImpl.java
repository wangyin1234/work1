package com.wison.purchase.packages.module.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.ExcelUtil;
import com.wison.purchase.packages.comm.utils.HttpUtils;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import com.wison.purchase.packages.module.main.domain.bo.CompanyPurPackageTBo;
import com.wison.purchase.packages.module.main.mapper.CompanyPurPackageTMapper;
import com.wison.purchase.packages.module.main.service.CompanyPurPackageTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 公司级采购包清单服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyPurPackageTServiceImpl implements CompanyPurPackageTService {
    private static final String ECOSYS_URL = "https://ecosys.wison.com/ecosys/api/restjson/company-pwp-list/?_username=admin&_password=EcosysEPP";
    private final CompanyPurPackageTMapper companyPurPackageTMapper;

    @Override
    public Page<CompanyPurPackageT> list(CompanyPurPackageTBo bo) {
        PageDTO<CompanyPurPackageT> page = new PageDTO<>(bo.getPageNum(), bo.getPageSize());
        return companyPurPackageTMapper.selectPage(page, buildListLqw(bo));
    }

    @Override
    public CompanyPurPackageT selectOne(CompanyPurPackageTBo bo) {
        return companyPurPackageTMapper.selectOne(buildOneLqw(bo));
    }

    @Override
    public int updateOne(CompanyPurPackageTBo bo) {
        CompanyPurPackageT entity = BeanUtil.toBean(bo, CompanyPurPackageT.class);
        return companyPurPackageTMapper.updateById(entity);
    }

    @Override
    public int updateByBo(CompanyPurPackageTBo bo) {
        CompanyPurPackageT entity = BeanUtil.toBean(bo, CompanyPurPackageT.class);
        return companyPurPackageTMapper.update(entity, buildOneLqw(bo));
    }

    @Override
    public boolean updateAll(List<CompanyPurPackageTBo> boList) {
        List<CompanyPurPackageT> entityList = BeanUtil.copyToList(boList, CompanyPurPackageT.class);
        return companyPurPackageTMapper.updateBatchById(entityList);
    }

    @Override
    public int insertOne(CompanyPurPackageTBo bo) {
        CompanyPurPackageT entity = BeanUtil.toBean(bo, CompanyPurPackageT.class);
        return companyPurPackageTMapper.insert(entity);
    }

    @Override
    public boolean insertAll(List<CompanyPurPackageTBo> boList) {
        List<CompanyPurPackageT> entityList = BeanUtil.copyToList(boList, CompanyPurPackageT.class);
        return companyPurPackageTMapper.insertBatch(entityList);
    }

    @Override
    public int deleteOne(String id) {
        return companyPurPackageTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(CompanyPurPackageTBo bo) {
        return companyPurPackageTMapper.delete(buildOneLqw(bo));
    }

    @Override
    public int deleteAll(List<String> idList) {
        return companyPurPackageTMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean insertOrUpdate(List<CompanyPurPackageTBo> boList) {
        List<CompanyPurPackageT> entityList = BeanUtil.copyToList(boList, CompanyPurPackageT.class);
        return companyPurPackageTMapper.insertOrUpdateBatch(entityList);
    }

    private LambdaQueryWrapper<CompanyPurPackageT> buildListLqw(CompanyPurPackageTBo bo) {
        LambdaQueryWrapper<CompanyPurPackageT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CompanyPurPackageT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(StringUtils.isNotEmpty(bo.getMdbPurchasePackageNo()), CompanyPurPackageT::getMdbPurchasePackageNo, bo.getMdbPurchasePackageNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDisciplineId()), CompanyPurPackageT::getDisciplineId, bo.getDisciplineId());
        lqw.like(StringUtils.isNotBlank(bo.getDisciplineName()), CompanyPurPackageT::getDisciplineName, bo.getDisciplineName());
        lqw.like(StringUtils.isNotBlank(bo.getMdblargeName()), CompanyPurPackageT::getMdblargeName, bo.getMdblargeName());
        lqw.like(StringUtils.isNotBlank(bo.getMdblargeId()), CompanyPurPackageT::getMdblargeId, bo.getMdblargeId());
        lqw.like(StringUtils.isNotBlank(bo.getMdbmediumName()), CompanyPurPackageT::getMdbmediumName, bo.getMdbmediumName());
        lqw.like(StringUtils.isNotBlank(bo.getMdbmediumId()), CompanyPurPackageT::getMdbmediumId, bo.getMdbmediumId());
        lqw.like(StringUtils.isNotBlank(bo.getMdbsmallName()), CompanyPurPackageT::getMdbsmallName, bo.getMdbsmallName());
        lqw.like(StringUtils.isNotBlank(bo.getMdbsmallId()), CompanyPurPackageT::getMdbsmallId, bo.getMdbsmallId());
        lqw.orderByAsc(CompanyPurPackageT::getMdbsn);
        return lqw;
    }

    private LambdaQueryWrapper<CompanyPurPackageT> buildOneLqw(CompanyPurPackageTBo bo) {
        LambdaQueryWrapper<CompanyPurPackageT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CompanyPurPackageT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(StringUtils.isNotBlank(bo.getMdbPurchasePackageNo()), CompanyPurPackageT::getMdbPurchasePackageNo, bo.getMdbPurchasePackageNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDisciplineId()), CompanyPurPackageT::getDisciplineId, bo.getDisciplineId());
        return lqw;
    }

    @Override
    public Page<CompanyPurPackageT> queryList(CompanyPurPackageTBo bo) {
        PageDTO<CompanyPurPackageT> page = new PageDTO<>(bo.getPageNum(), bo.getPageSize());
        page.addOrder(bo.getOrderItem());
        if (ObjectUtil.isNull(bo.getZoneCode())) {
            bo.setZoneCode(HeadInfoHolder.getZoneCode());
        }
        Page<CompanyPurPackageT> pageResult = companyPurPackageTMapper.queryList(page, bo);
        pageResult.getRecords().forEach(l -> {
            if (ObjectUtil.isNotEmpty(l.getProjectCode()) && !l.getProjectCode().equals(HeadInfoHolder.getProjectCode())) {
                l.setIsExist("");
                l.setSubPackageNo("");
            }
        });
        pageResult.getRecords().sort(Comparator.comparing(CompanyPurPackageT::getIsExist).reversed());
        return pageResult;
    }

    @Override
    public void exportExcel(List<CompanyPurPackageT> list, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        String templatePath = "templates/company_pur_package.xlsx";
        Map<String, Object> sheet1 = new HashMap<>();
        sheet1.put("list", list);
        data.add(sheet1);
        OutputStream os = response.getOutputStream();
        ExcelUtil.exportTemplateMultiSheet(data, templatePath, os);
    }

    @Override
    public boolean syncCompanyPurPackage() {
        CompletableFuture<Map<String, List<Map<String, String>>>> ecosysFuture = CompletableFuture.supplyAsync(() -> HttpUtils.getJson(ECOSYS_URL));
        CompletableFuture<List<CompanyPurPackageT>> existsDataFuture = CompletableFuture.supplyAsync(() -> {
            CompanyPurPackageTBo bo = new CompanyPurPackageTBo();
            bo.setPageSize(-1);
            return list(bo).getRecords();
        });
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(ecosysFuture, existsDataFuture);
        AtomicReference<List<CompanyPurPackageTBo>> inserOrUpdatetList = new AtomicReference<>(new ArrayList<>());
        allFutures.thenRun(() -> {
            try {
                List<Map<String, String>> mapList = ecosysFuture.get().get("WorkingForecastTransactionList");
                List<CompanyPurPackageT> existsList = existsDataFuture.get();
                List<CompanyPurPackageTBo> list = mapList.stream().map(CompanyPurPackageTBo::new).collect(Collectors.toList());
                String zoneCode = HeadInfoHolder.getZoneCode();
                list.forEach(u -> {
                    existsList.stream().filter(i -> zoneCode.equals(i.getZoneCode()) && i.getDisciplineId().equals(u.getDisciplineId()) && i.getMdbPurchasePackageNo().equals(u.getMdbPurchasePackageNo())).findFirst().ifPresent(t ->
                            u.setId(t.getId())
                    );
                });
                inserOrUpdatetList.set(list);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).join();
        return insertOrUpdate(inserOrUpdatetList.get());
    }
}