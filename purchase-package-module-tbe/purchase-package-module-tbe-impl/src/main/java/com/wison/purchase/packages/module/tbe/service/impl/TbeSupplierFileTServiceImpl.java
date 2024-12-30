package com.wison.purchase.packages.module.tbe.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierFileT;
import com.wison.purchase.packages.module.tbe.mapper.TbeSupplierFileTMapper;
import com.wison.purchase.packages.module.tbe.service.TbeSupplierFileTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-21 09:26:54
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbeSupplierFileTServiceImpl implements TbeSupplierFileTService {
    private final TbeSupplierFileTMapper tbeSupplierFileTMapper;

    @Override
    public Page<TbeSupplierFileT> list(TbeSupplierFileT bo) {
        PageDTO<TbeSupplierFileT> page = new PageDTO<>(bo.getPageNum(), bo.getPageSize());
        return tbeSupplierFileTMapper.selectVoPage(page, buildListLqw(bo));
    }

    @Override
    public TbeSupplierFileT selectOne(TbeSupplierFileT bo) {
        return null;
    }

    @Override
    public int updateOne(TbeSupplierFileT bo) {
        return 0;
    }

    @Override
    public int updateByBo(TbeSupplierFileT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<TbeSupplierFileT> boList) {
        return false;
    }

    @Override
    public int insertOne(TbeSupplierFileT bo) {
        return tbeSupplierFileTMapper.insert(bo);
    }

    @Override
    public boolean insertAll(List<TbeSupplierFileT> boList) {
        return tbeSupplierFileTMapper.insertBatch(boList);
    }

    @Override
    public int deleteOne(String id) {
        return 0;
    }

    @Override
    public int deleteByBo(TbeSupplierFileT bo) {
        LambdaQueryWrapper<TbeSupplierFileT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeSupplierFileT::getZoneCode, bo.getZoneCode());
        lqw.eq(TbeSupplierFileT::getProjectCode, bo.getProjectCode());
        lqw.eq(TbeSupplierFileT::getPriceCheckNumber, bo.getPriceCheckNumber());
        lqw.eq(TbeSupplierFileT::getVendorCode, bo.getVendorCode());
        String[] fileTypes = new String[]{"2", "4", "6", "8", "9"};
        lqw.in(TbeSupplierFileT::getFileType, Arrays.asList(fileTypes));
        return tbeSupplierFileTMapper.delete(lqw);
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<TbeSupplierFileT> boList) {
        return tbeSupplierFileTMapper.insertOrUpdateBatch(boList);
    }

    private LambdaQueryWrapper<TbeSupplierFileT> buildListLqw(TbeSupplierFileT bo) {
        LambdaQueryWrapper<TbeSupplierFileT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeSupplierFileT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(TbeSupplierFileT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.like(StringUtils.isNotBlank(bo.getPriceCheckNumber()), TbeSupplierFileT::getPriceCheckNumber, bo.getPriceCheckNumber());
        lqw.like(StringUtils.isNotBlank(bo.getVendorCode()), TbeSupplierFileT::getVendorCode, bo.getVendorCode());
        lqw.like(StringUtils.isNotBlank(bo.getFileType()), TbeSupplierFileT::getFileType, bo.getFileType());
        lqw.orderByAsc(TbeSupplierFileT::getUpdateTime);
        return lqw;
    }
}