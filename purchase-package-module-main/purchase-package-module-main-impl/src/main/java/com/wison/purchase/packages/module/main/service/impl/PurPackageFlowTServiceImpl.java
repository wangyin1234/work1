package com.wison.purchase.packages.module.main.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.PurPackageFlowT;
import com.wison.purchase.packages.module.main.mapper.PurPackageFlowTMapper;
import com.wison.purchase.packages.module.main.service.PurPackageFlowTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-27 16:09:38
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PurPackageFlowTServiceImpl implements PurPackageFlowTService {
    private final PurPackageFlowTMapper purPackageFlowTMapper;

    @Override
    public Page<PurPackageFlowT> list(PurPackageFlowT bo) {
        return null;
    }

    @Override
    public PurPackageFlowT selectOne(PurPackageFlowT bo) {
        return purPackageFlowTMapper.selectOne(buildOneLqw(bo));
    }

    @Override
    public int updateOne(PurPackageFlowT bo) {
        return purPackageFlowTMapper.updateById(bo);
    }

    @Override
    public int updateByBo(PurPackageFlowT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<PurPackageFlowT> boList) {
        return false;
    }

    @Override
    public int insertOne(PurPackageFlowT bo) {
        return purPackageFlowTMapper.insert(bo);
    }

    @Override
    public boolean insertAll(List<PurPackageFlowT> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        return 0;
    }

    @Override
    public int deleteByBo(PurPackageFlowT bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<PurPackageFlowT> boList) {
        return false;
    }

    private LambdaQueryWrapper<PurPackageFlowT> buildOneLqw(PurPackageFlowT bo) {
        LambdaQueryWrapper<PurPackageFlowT> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getRequestId())) {
            lqw.eq(PurPackageFlowT::getRequestId, bo.getRequestId());
        } else {
            lqw.eq(PurPackageFlowT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
            lqw.eq(PurPackageFlowT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
            lqw.eq(PurPackageFlowT::getModule, bo.getModule());
        }
        return lqw;
    }
}