package com.wison.purchase.packages.module.tbe.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsT;
import com.wison.purchase.packages.module.tbe.mapper.InquiryRevealingDocumentsTMapper;
import com.wison.purchase.packages.module.tbe.service.InquiryRevealingDocumentsTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 询价商务文件表服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-14 10:15:01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryRevealingDocumentsTServiceImpl implements InquiryRevealingDocumentsTService {
    private final InquiryRevealingDocumentsTMapper inquiryRevealingDocumentsTMapper;

    @Override
    public Page<InquiryTechnicalDocumentsT> list(InquiryTechnicalDocumentsT bo) {
        return null;
    }

    @Override
    public InquiryTechnicalDocumentsT selectOne(InquiryTechnicalDocumentsT bo) {
        return null;
    }

    @Override
    public int updateOne(InquiryTechnicalDocumentsT bo) {
        return 0;
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
        return inquiryRevealingDocumentsTMapper.deleteById(id);
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