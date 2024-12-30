package com.wison.purchase.packages.module.tbe.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsT;
import com.wison.purchase.packages.module.tbe.mapper.InquiryTechnicalDocumentsTMapper;
import com.wison.purchase.packages.module.tbe.service.InquiryTechnicalDocumentsTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class InquiryTechnicalDocumentsTServiceImpl implements InquiryTechnicalDocumentsTService {

    private final InquiryTechnicalDocumentsTMapper inquiryTechnicalDocumentsTMapper;

    @Override
    public Page<InquiryTechnicalDocumentsT> list(InquiryTechnicalDocumentsT bo) {
        return null;
    }

    @Override
    public InquiryTechnicalDocumentsT selectOne(InquiryTechnicalDocumentsT bo) {
        return inquiryTechnicalDocumentsTMapper.selectById(bo.getId());
    }

    @Override
    public int updateOne(InquiryTechnicalDocumentsT entity) {
        return inquiryTechnicalDocumentsTMapper.updateById(entity);
    }

    @Override
    public int updateByBo(InquiryTechnicalDocumentsT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<InquiryTechnicalDocumentsT> boList) {
        return false;
    }

    @Override
    public int insertOne(InquiryTechnicalDocumentsT bo) {
        return 0;
    }

    @Override
    public boolean insertAll(List<InquiryTechnicalDocumentsT> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        return inquiryTechnicalDocumentsTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(InquiryTechnicalDocumentsT bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<InquiryTechnicalDocumentsT> boList) {
        return false;
    }
}