package com.wison.purchase.packages.module.tbe.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.module.tbe.domain.InquiryTechnicalDocumentsReplyT;
import com.wison.purchase.packages.module.tbe.mapper.InquiryTechnicalDocumentsReplyTMapper;
import com.wison.purchase.packages.module.tbe.service.InquiryTechnicalDocumentsReplyTService;
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
public class InquiryTechnicalDocumentsReplyTServiceImpl implements InquiryTechnicalDocumentsReplyTService {

    private final InquiryTechnicalDocumentsReplyTMapper inquiryTechnicalDocumentsReplyTMapper;


    @Override
    public Page<InquiryTechnicalDocumentsReplyT> list(InquiryTechnicalDocumentsReplyT bo) {
        return null;
    }

    @Override
    public InquiryTechnicalDocumentsReplyT selectOne(InquiryTechnicalDocumentsReplyT bo) {
        return null;
    }

    @Override
    public int updateOne(InquiryTechnicalDocumentsReplyT bo) {
        return 0;
    }

    @Override
    public int updateByBo(InquiryTechnicalDocumentsReplyT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<InquiryTechnicalDocumentsReplyT> boList) {
        return false;
    }

    @Override
    public int insertOne(InquiryTechnicalDocumentsReplyT bo) {
        return 0;
    }

    @Override
    public boolean insertAll(List<InquiryTechnicalDocumentsReplyT> boList) {
        return inquiryTechnicalDocumentsReplyTMapper.insertBatch(boList);
    }

    @Override
    public int deleteOne(String id) {
        return 0;
    }

    @Override
    public int deleteByBo(InquiryTechnicalDocumentsReplyT bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<InquiryTechnicalDocumentsReplyT> boList) {
        return false;
    }
}