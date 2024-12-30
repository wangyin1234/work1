package com.wison.purchase.packages.module.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.wison.base.core.exception.BusinessException;
import com.wison.cology.api.domain.vo.ProcessStaffVO;
import com.wison.cology.api.service.WorkflowCurrentOperatorDubboService;
import com.wison.master.data.project.api.domain.dto.ProjectPostMembers;
import com.wison.master.data.project.api.service.ProjectPeopleDubboService;
import com.wison.purchase.packages.comm.constants.RoleCodeConstants;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.enums.CategoryEnum;
import com.wison.purchase.packages.comm.enums.DoneStatusEnum;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.comm.utils.ExcelUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.main.domain.MrReleaseFileT;
import com.wison.purchase.packages.module.main.domain.MrReleaseT;
import com.wison.purchase.packages.module.main.domain.vo.MrReleaseTVo;
import com.wison.purchase.packages.module.main.mapper.MrReleaseFileTMapper;
import com.wison.purchase.packages.module.main.mapper.MrReleaseTMapper;
import com.wison.purchase.packages.module.main.service.MrReleaseTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设计询价文件发布服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MrReleaseTServiceImpl implements MrReleaseTService {
    private final MrReleaseTMapper mrReleaseTMapper;
    private final MrReleaseFileTMapper mrReleaseFileTMapper;
    @DubboReference
    private WorkflowCurrentOperatorDubboService workflowCurrentOperatorDubboService;
    @DubboReference
    private ProjectPeopleDubboService projectPeopleDubboService;

    @Override
    public Page<MrReleaseTVo> list(MrReleaseTVo bo) {
        Page<MrReleaseTVo> page = mrReleaseTMapper.selectVoPage(new PageDTO<>(bo.getPageNum(), bo.getPageSize()), buildListLqw(bo), MrReleaseTVo.class);
        Map<Long, ProcessStaffVO> userMap = workflowCurrentOperatorDubboService.getProcessStaffMap(page.getRecords().stream().map(user ->
                Long.valueOf(user.getRequestId())
        ).collect(Collectors.toSet()));
        List<ProjectPostMembers> projectPostMembersList =
                projectPeopleDubboService.getProjectPostMembers(
                        HeadInfoHolder.getZoneCode(),
                        HeadInfoHolder.getProjectCode(),
                        RoleCodeConstants.PROJECT_ACCOUNTING,
                        null);
        String designManagerName;
        if (ObjectUtil.isNotEmpty(projectPostMembersList)) {
            designManagerName = projectPostMembersList.get(0).getUserName();
        } else {
            designManagerName = null;
        }
        page.getRecords().forEach(mr -> {
            List<MrReleaseFileT> fileList = mrReleaseFileTMapper.selectList(buildFileLqw(mr));
            mr.setMrReleaseFileTList(fileList);
            ProcessStaffVO currentHandler = userMap.get(Long.valueOf(mr.getRequestId()));
            if (ProcessStatusEnum.REVIEWING.status.equals(mr.getReviewStatus())) {
                mr.setCurrentHandler(currentHandler.getUserName() + "/" + currentHandler.getLoginName());
            }
            if ("0".equals(mr.getDesignManager())) {
                mr.setDesignManagerName(designManagerName);
            }
        });
        return page;
    }

    @Override
    public MrReleaseTVo selectOne(MrReleaseTVo bo) {
        MrReleaseTVo vo = mrReleaseTMapper.selectVoById(bo.getId(), MrReleaseTVo.class);
        if (null == vo) {
            throw new BusinessException("数据不存在");
        }
        vo.setMrReleaseFileTList(mrReleaseFileTMapper.selectList(buildFileLqw(vo)));
        return vo;
    }

    @Override
    @Transactional
    public int updateOne(MrReleaseTVo bo) {
        MrReleaseT entity = BeanUtil.toBean(bo, MrReleaseT.class);
        int row = mrReleaseTMapper.updateById(entity);
        List<MrReleaseFileT> mrReleaseFileTList = bo.getMrReleaseFileTList();
        if (null != mrReleaseFileTList && !mrReleaseFileTList.isEmpty()) {
            mrReleaseFileTList.forEach(file -> {
                file.setTechnicalDocumentNoWison(bo.getTechnicalDocumentNoWison());
                file.setVersion(bo.getVersion());
                file.setCategory(bo.getCategory());
            });
            mrReleaseFileTMapper.insertBatch(mrReleaseFileTList);
        }
        return row;
    }

    @Override
    public int updateByBo(MrReleaseTVo bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<MrReleaseTVo> boList) {
        return false;
    }

    @Override
    @Transactional
    public int insertOne(MrReleaseTVo bo) {
        MrReleaseT entity = BeanUtil.toBean(bo, MrReleaseT.class);
        int row = mrReleaseTMapper.insert(entity);
        List<MrReleaseFileT> mrReleaseFileTList = bo.getMrReleaseFileTList();
        mrReleaseFileTList.forEach(file -> {
            file.setTechnicalDocumentNoWison(bo.getTechnicalDocumentNoWison());
            file.setVersion(bo.getVersion());
            file.setCategory(bo.getCategory());
        });
        mrReleaseFileTMapper.insertBatch(mrReleaseFileTList);
        return row;
    }

    @Override
    public boolean insertAll(List<MrReleaseTVo> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        return mrReleaseTMapper.deleteById(id);
    }

    @Override
    public int deleteByBo(MrReleaseTVo bo) {
        return mrReleaseTMapper.delete(buildOneLqw(bo));
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<MrReleaseTVo> boList) {
        return false;
    }

    private LambdaQueryWrapper<MrReleaseT> buildListLqw(MrReleaseTVo bo) {
        LambdaQueryWrapper<MrReleaseT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MrReleaseT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(MrReleaseT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.like(StringUtils.isNotBlank(bo.getTechnicalDocumentNoWison()), MrReleaseT::getTechnicalDocumentNoWison, bo.getTechnicalDocumentNoWison());
        lqw.like(StringUtils.isNotBlank(bo.getTechnicalDocumentNoOthers()), MrReleaseT::getTechnicalDocumentNoOthers, bo.getTechnicalDocumentNoOthers());
        lqw.eq(StringUtils.isNotBlank(bo.getDoneStatus()), MrReleaseT::getDoneStatus, bo.getDoneStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), MrReleaseT::getCategory, bo.getCategory());
        lqw.like(StringUtils.isNotBlank(bo.getCurrentHandler()), MrReleaseT::getCurrentHandler, bo.getCurrentHandler());
        lqw.eq(StringUtils.isNotBlank(bo.getDisciplineId()), MrReleaseT::getDisciplineId, bo.getDisciplineId());
        lqw.eq(StringUtils.isNotBlank(bo.getReviewStatus()), MrReleaseT::getReviewStatus, bo.getReviewStatus());
        lqw.like(StringUtils.isNotBlank(bo.getPreparer()), MrReleaseT::getPreparer, bo.getPreparer());
        return lqw;
    }

    private LambdaQueryWrapper<MrReleaseFileT> buildFileLqw(MrReleaseTVo bo) {
        LambdaQueryWrapper<MrReleaseFileT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MrReleaseFileT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
        lqw.eq(MrReleaseFileT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
        lqw.eq(MrReleaseFileT::getTechnicalDocumentNoWison, bo.getTechnicalDocumentNoWison());
        lqw.eq(MrReleaseFileT::getVersion, bo.getVersion());
        lqw.eq(MrReleaseFileT::getCategory, bo.getCategory());
        return lqw;
    }

    private LambdaQueryWrapper<MrReleaseT> buildOneLqw(MrReleaseTVo bo) {
        LambdaQueryWrapper<MrReleaseT> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getRequestId())) {
            lqw.eq(MrReleaseT::getRequestId, bo.getRequestId());
        } else {
            lqw.eq(MrReleaseT::getZoneCode, ObjectUtil.isNull(bo.getZoneCode()) ? HeadInfoHolder.getZoneCode() : bo.getZoneCode());
            lqw.eq(MrReleaseT::getProjectCode, ObjectUtil.isNull(bo.getProjectCode()) ? HeadInfoHolder.getProjectCode() : bo.getProjectCode());
            lqw.eq(MrReleaseT::getTechnicalDocumentNoWison, bo.getTechnicalDocumentNoWison());
            lqw.eq(MrReleaseT::getCategory, bo.getCategory());
            lqw.orderByDesc(MrReleaseT::getVersion);
        }
        return lqw;
    }

    @Override
    public MrReleaseTVo selectOneByBo(MrReleaseTVo bo) {
        List<MrReleaseTVo> list = mrReleaseTMapper.selectVoList(buildOneLqw(bo), MrReleaseTVo.class);
        return !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public void exportExcel(List<MrReleaseTVo> list, HttpServletResponse response) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNo(String.valueOf(i + 1));
            list.get(i).setDoneStatus(DoneStatusEnum.findByType(list.get(i).getDoneStatus()));
            list.get(i).setReviewStatus(ProcessStatusEnum.findByType(list.get(i).getReviewStatus()));
            list.get(i).setCategory(CategoryEnum.findByType(list.get(i).getCategory()));
        }
        ExcelUtil.exportListExcel("templates/mr_release.xlsx", list, response);
    }
}