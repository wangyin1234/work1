package com.wison.purchase.packages.module.tbe.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.domain.UserInfo;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.utils.DateUtils;
import com.wison.purchase.packages.comm.utils.HttpUtils;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.tbe.domain.*;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.request.TbeTMessage;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;
import com.wison.purchase.packages.module.tbe.mapper.*;
import com.wison.purchase.packages.module.tbe.service.TbeTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
public class TbeTServiceImpl implements TbeTService {
    private final TbeTMapper tbeTMapper;
    private final InquiryTechnicalDocumentsTMapper inquiryTechnicalDocumentsTMapper;
    private final InquiryRevealingDocumentsTMapper inquiryRevealingDocumentsTMapper;
    private final InquiryTechnicalDocumentsReplyTMapper inquiryTechnicalDocumentsReplyTMapper;
    private final InquiryRevealingDocumentsReplyTMapper inquiryRevealingDocumentsReplyTMapper;
    private final TbeSupplierTMapper tbeSupplierTMapper;
    private final TbeSupplierFileTMapper tbeSupplierFileTMapper;
    private final TbeResultFileTMapper tbeResultFileTMapper;

    @Value("${supplier.url.send}")
    private String supplierSendUrl;

    @Override
    public Page<TbeTVo> list(TbeTBo bo) {
        PageDTO<TbeT> page = new PageDTO<>(bo.getPageNum(), bo.getPageSize());
        return tbeTMapper.selectVoPage(page, buildListLqw(bo));
    }

    @Override
    public TbeTVo selectOne(TbeTBo bo) {
        bo.setZoneCode(ObjectUtil.isNotEmpty(bo.getZoneCode()) ? bo.getZoneCode() : HeadInfoHolder.getZoneCode());
        bo.setProjectCode(ObjectUtil.isNotEmpty(bo.getProjectCode()) ? bo.getProjectCode() : HeadInfoHolder.getProjectCode());
        return tbeTMapper.queryList(bo);
    }

    @Override
    @Transactional
    public int updateOne(TbeTBo bo) {
        TbeT entity = BeanUtil.toBean(bo, TbeT.class);
        int row = tbeTMapper.updateById(entity);
        inquiryTechnicalDocumentsTMapper.insertOrUpdateBatch(bo.getInquiryTechnicalDocuments());
        inquiryRevealingDocumentsTMapper.insertOrUpdateBatch(bo.getInquiryRevealingDocuments());
        inquiryTechnicalDocumentsReplyTMapper.insertOrUpdateBatch(bo.getInquiryTechnicalDocumentsReplies());
        inquiryRevealingDocumentsReplyTMapper.insertOrUpdateBatch(bo.getInquiryRevealingDocumentReplies());
        if (ObjectUtil.isNotEmpty(bo.getTbeSuppliers())) {
            List<TbeSupplierT> tbeSupplierTList = BeanUtil.copyToList(bo.getTbeSuppliers(), TbeSupplierT.class);
            tbeSupplierTMapper.insertOrUpdateBatch(tbeSupplierTList);
            insertTbeSupplierFileTList(bo.getTbeSuppliers(), bo.getPriceCheckNumber());
        }
        return row;
    }

    @Override
    public int updateByBo(TbeTBo bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<TbeTBo> boList) {
        return false;
    }

    @Override
    @Transactional
    public int insertOne(TbeTBo bo) {
        TbeT entity = BeanUtil.toBean(bo, TbeT.class);
        UserInfo loginUser = UserInfoHolder.getUserInfo();
        // 设计
        entity.setCreatorEmail(loginUser.getEmail());
        entity.setCreatorName(loginUser.getUserName());
        int row = tbeTMapper.insert(entity);
        if (ObjectUtil.isNotEmpty(bo.getInquiryTechnicalDocuments())) {
            List<InquiryTechnicalDocumentsT> inquiryTechnicalDocuments = bo.getInquiryTechnicalDocuments();
            inquiryTechnicalDocuments.forEach(doc -> doc.setPriceCheckNumber(bo.getPriceCheckNumber()));
            inquiryTechnicalDocumentsTMapper.insertBatch(inquiryTechnicalDocuments);
        }
        if (ObjectUtil.isNotEmpty(bo.getInquiryTechnicalDocumentsReplies())) {
            List<InquiryTechnicalDocumentsReplyT> inquiryTechnicalDocumentsReplies = bo.getInquiryTechnicalDocumentsReplies();
            inquiryTechnicalDocumentsReplies.forEach(doc -> doc.setPriceCheckNumber(bo.getPriceCheckNumber()));
            inquiryTechnicalDocumentsReplyTMapper.insertBatch(inquiryTechnicalDocumentsReplies);
        }
        if (ObjectUtil.isNotEmpty(bo.getInquiryRevealingDocuments())) {
            List<InquiryRevealingDocumentsT> inquiryRevealingDocumentsT = bo.getInquiryRevealingDocuments();
            inquiryRevealingDocumentsT.forEach(doc -> doc.setPriceCheckNumber(bo.getPriceCheckNumber()));
            inquiryRevealingDocumentsTMapper.insertBatch(inquiryRevealingDocumentsT);
        }
        if (ObjectUtil.isNotEmpty(bo.getInquiryRevealingDocumentReplies())) {
            List<InquiryRevealingDocumentsReplyT> inquiryRevealingDocumentReplies = bo.getInquiryRevealingDocumentReplies();
            inquiryRevealingDocumentReplies.forEach(doc -> doc.setPriceCheckNumber(bo.getPriceCheckNumber()));
            inquiryRevealingDocumentsReplyTMapper.insertBatch(inquiryRevealingDocumentReplies);
        }
        if (ObjectUtil.isNotEmpty(bo.getTbeSuppliers())) {
            List<TbeSupplierTVo> tbeSupplierTVoList = bo.getTbeSuppliers();
            tbeSupplierTVoList.forEach(doc -> doc.setPriceCheckNumber(bo.getPriceCheckNumber()));
            List<TbeSupplierT> tbeSupplierTList = BeanUtil.copyToList(tbeSupplierTVoList, TbeSupplierT.class);
            tbeSupplierTMapper.insertBatch(tbeSupplierTList);
            insertTbeSupplierFileTList(tbeSupplierTVoList, bo.getPriceCheckNumber());
        }
        return row;
    }

    private void insertTbeSupplierFileTList(List<TbeSupplierTVo> list, String priceCheckNumber) {
        list.forEach(tbeSupplierTVo -> {
            if (ObjectUtil.isNotEmpty(tbeSupplierTVo.getTbeSupplierFileList())) {
                List<TbeSupplierFileT> TbeSupplierFileTList = tbeSupplierTVo.getTbeSupplierFileList().stream().filter(file -> ObjectUtil.isEmpty(file.getId())).collect(Collectors.toList());
                TbeSupplierFileTList.forEach(doc -> {
                    doc.setPriceCheckNumber(priceCheckNumber);
                    doc.setVendorCode(tbeSupplierTVo.getVendorCode());
                });
                tbeSupplierFileTMapper.insertBatch(TbeSupplierFileTList);
            }
        });
    }

    @Override
    public boolean insertAll(List<TbeTBo> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        TbeT entity = tbeTMapper.selectById(id);
        if (null == entity) {
            throw new BusinessException("技术评审不存在");
        }
        if (ProcessStatusEnum.REVIEWING.status.equals(entity.getDesignReviewStatus())) {
            throw new BusinessException("设计流程在审批中");
        } else if (ProcessStatusEnum.REVIEWING.status.equals(entity.getBuyReviewStatus())) {
            throw new BusinessException("采购流程在审批中");
        }
        return tbeTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(TbeTBo bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<TbeTBo> boList) {
        return false;
    }

    private LambdaQueryWrapper<TbeT> buildListLqw(TbeTBo bo) {
        LambdaQueryWrapper<TbeT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(TbeT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.like(StringUtils.isNotBlank(bo.getPriceCheckNumber()), TbeT::getPriceCheckNumber, bo.getPriceCheckNumber());
        lqw.eq(bo.isFinish(), TbeT::getIsFinish, true);
        lqw.orderByDesc(TbeT::getUpdateTime);
        return lqw;
    }

    @Override
    @Async
    public void sendToSupplier(TbeTVo vo) {
        log.info("发送到供应商");
        TbeTMessage data = BeanUtil.toBean(vo, TbeTMessage.class);
        data.setTbeSuppliers(data.getTbeSuppliers().stream().filter(TbeSupplierTVo::isTechnicalReviewOpinion).collect(Collectors.toList()));
        data.setBiddingPublishDate(new Date());
        UserInfo loginUser = UserInfoHolder.getUserInfo();
        data.setPublishOfficerUserId(loginUser.getLoginName());
        data.setPublishOfficerUserName(loginUser.getUserName());
        data.setProjectCode(HeadInfoHolder.getProjectCode());
        data.setZoneCode(HeadInfoHolder.getZoneCode());
        Object result = HttpUtils.postJson(supplierSendUrl, data);
        if (null == result) {
            throw new BusinessException("发送失败");
        }
        log.info("发送成功");
    }

    @Override
    @Transactional
    public boolean sendToPur(TbeTBo bo) {
        TbeT entity = tbeTMapper.selectById(bo.getId());
        if (null == entity) {
            throw new BusinessException("技术评审不存在");
        }
        if (ProcessStatusEnum.REVIEWING.status.equals(entity.getDesignReviewStatus())) {
            throw new BusinessException("设计流程在审批中");
        }
        if (ProcessStatusEnum.REVIEWING.status.equals(entity.getBuyReviewStatus())) {
            throw new BusinessException("采购流程在审批中");
        }
        if (ObjectUtil.isEmpty(bo.getTbeResultFiles())) {
            throw new BusinessException("技术评审结果文件必须上传");
        }
        if (ObjectUtil.isEmpty(bo.getTbeSuppliers())) {
            throw new BusinessException("供应商不存在");
        }
        entity.setIsFinish(true);
        int row = tbeTMapper.updateById(entity);
        if (row == 0) {
            throw new BusinessException("更新失败");
        }
        List<TbeResultFileT> tbeResultFiles = bo.getTbeResultFiles();
        tbeResultFiles.forEach(doc -> doc.setPriceCheckNumber(entity.getPriceCheckNumber()));
        boolean result = tbeResultFileTMapper.insertBatch(tbeResultFiles);
        List<TbeSupplierT> tbeSuppliers = BeanUtil.copyToList(bo.getTbeSuppliers(), TbeSupplierT.class);
        result = result && tbeSupplierTMapper.updateBatchById(tbeSuppliers);
        return result;
    }

    private LambdaQueryWrapper<TbeT> buildOneLqw(TbeTBo bo) {
        LambdaQueryWrapper<TbeT> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getDesignRequestId())) {
            lqw.eq(TbeT::getDesignRequestId, bo.getDesignRequestId());
        } else if (StringUtils.isNotBlank(bo.getBuyRequestId())) {
            lqw.eq(TbeT::getBuyRequestId, bo.getBuyRequestId());
        } else {
            lqw.eq(TbeT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
            lqw.eq(TbeT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
            lqw.eq(TbeT::getPriceCheckNumber, bo.getPriceCheckNumber());
        }
        return lqw;
    }

    @Override
    public TbeT selectOneByBo(TbeTBo bo) {
        return tbeTMapper.selectOne(buildOneLqw(bo));
    }

    @Override
    public List<TbeTVo> allList(TbeTBo bo) {
        return tbeTMapper.selectVoList(buildListLqw(bo));
    }
}