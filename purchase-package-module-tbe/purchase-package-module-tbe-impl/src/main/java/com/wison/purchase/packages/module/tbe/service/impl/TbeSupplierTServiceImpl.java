package com.wison.purchase.packages.module.tbe.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.utils.HttpUtils;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierFileT;
import com.wison.purchase.packages.module.tbe.domain.TbeSupplierT;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTMessage;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import com.wison.purchase.packages.module.tbe.mapper.TbeSupplierFileTMapper;
import com.wison.purchase.packages.module.tbe.mapper.TbeSupplierTMapper;
import com.wison.purchase.packages.module.tbe.service.TbeSupplierTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbeSupplierTServiceImpl implements TbeSupplierTService {
    private final TbeSupplierTMapper tbeSupplierTMapper;
    private final TbeSupplierFileTMapper tbeSupplierFileTMapper;

    @Value("${supplier.url.send}")
    private String supplierSendUrl;

    @Override
    public Page<TbeSupplierTVo> list(TbeSupplierTVo bo) {
        return null;
    }

    @Override
    public TbeSupplierTVo selectOne(TbeSupplierTVo bo) {
        TbeSupplierTVo vo = tbeSupplierTMapper.selectVoOne(buildOneLqw(bo));
        List<TbeSupplierFileT> fileTList = tbeSupplierFileTMapper.selectList(buildFileLqw(bo));
        vo.setTbeSupplierFileList(fileTList);
        return vo;
    }

    @Override
    public TbeSupplierTVo selectById(String id) {
        return tbeSupplierTMapper.selectVoById(id);
    }

    @Override
    @Transactional
    public int updateOne(TbeSupplierTVo bo) {
        TbeSupplierT entity = BeanUtil.toBean(bo, TbeSupplierT.class);
        int row = tbeSupplierTMapper.updateById(entity);
        if (row > 0) {
            tbeSupplierFileTMapper.insertOrUpdateBatch(bo.getTbeSupplierFileList());
        }
        return row;
    }

    @Override
    public int updateByBo(TbeSupplierTVo bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<TbeSupplierTVo> boList) {
        return false;
    }

    @Override
    public int insertOne(TbeSupplierTVo bo) {
        return 0;
    }

    @Override
    public boolean insertAll(List<TbeSupplierTVo> boList) {
        return false;
    }

    @Override
    @Transactional
    public int deleteOne(String id) {
        TbeSupplierTVo vo = selectById(id);
        if (null == vo) {
            throw new BusinessException("供应商不存在");
        }
        LambdaQueryWrapper<TbeSupplierFileT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeSupplierFileT::getZoneCode, vo.getZoneCode());
        lqw.eq(TbeSupplierFileT::getProjectCode, vo.getProjectCode());
        lqw.eq(TbeSupplierFileT::getPriceCheckNumber, vo.getPriceCheckNumber());
        lqw.eq(TbeSupplierFileT::getVendorCode, vo.getVendorCode());
        tbeSupplierFileTMapper.delete(lqw);
        return tbeSupplierTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(TbeSupplierTVo bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<TbeSupplierTVo> boList) {
        return false;
    }

    private LambdaQueryWrapper<TbeSupplierT> buildOneLqw(TbeSupplierTVo bo) {
        LambdaQueryWrapper<TbeSupplierT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeSupplierT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(TbeSupplierT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.eq(StringUtils.isNotBlank(bo.getPriceCheckNumber()), TbeSupplierT::getPriceCheckNumber, bo.getPriceCheckNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getVendorCode()), TbeSupplierT::getVendorCode, bo.getVendorCode());
        return lqw;
    }

    private LambdaQueryWrapper<TbeSupplierFileT> buildFileLqw(TbeSupplierTVo bo) {
        LambdaQueryWrapper<TbeSupplierFileT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeSupplierFileT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(TbeSupplierFileT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.eq(StringUtils.isNotBlank(bo.getPriceCheckNumber()), TbeSupplierFileT::getPriceCheckNumber, bo.getPriceCheckNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getVendorCode()), TbeSupplierFileT::getVendorCode, bo.getVendorCode());
        return lqw;
    }

    @Override
    @Async("taskExecutor")
    public void sendToSupplier(TbeSupplierTVo bo) {
        log.info("发送到供应商");
        TbeTMessage data = new TbeTMessage();
        List<TbeSupplierTVo> tbeSuppliers = new ArrayList<>();
        tbeSuppliers.add(bo);
        data.setTbeSuppliers(tbeSuppliers);
        data.setPriceCheckNumber(bo.getPriceCheckNumber());
        data.setProjectCode(HeadInfoHolder.getProjectCode());
        data.setZoneCode(HeadInfoHolder.getZoneCode());
        Object result = HttpUtils.postJson(supplierSendUrl, bo);
        if (null == result) {
            throw new BusinessException("发送失败");
        }
        log.info("更新文件");
        List<TbeSupplierFileT> tbeSupplierFileList = bo.getTbeSupplierFileList();
        tbeSupplierFileList.forEach(file -> file.setSend(true));
        tbeSupplierFileTMapper.insertOrUpdateBatch(tbeSupplierFileList);
    }

}