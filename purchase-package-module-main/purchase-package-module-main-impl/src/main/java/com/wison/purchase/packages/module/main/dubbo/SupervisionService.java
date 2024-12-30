package com.wison.purchase.packages.module.main.dubbo;


import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.module.main.domain.ProjectMrPlanT;
import com.wison.purchase.packages.module.main.domain.vo.MrReleaseTVo;
import com.wison.purchase.packages.module.main.service.ProjectMrPlanTService;
import com.wison.scm.supervision.api.domain.MaterialRequisitionDTO;
import com.wison.scm.supervision.api.service.SupervisionDubboService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupervisionService {

    @DubboReference
    private SupervisionDubboService supervisionDubboService;
    private final ProjectMrPlanTService projectMrPlanTService;

    /**
     * 推送询价技术文件记录到监造服务
     *
     * @param mr
     */
    @Async("taskExecutor")
    public void pushMaterialRequisitionToSupervision(MrReleaseTVo mr) {
        log.info("推送询价技术文件");
        try {
            List<MaterialRequisitionDTO> materialRequisitionDTOS = new ArrayList<>();
            MaterialRequisitionDTO dto = new MaterialRequisitionDTO();
            dto.setProjId(mr.getProjectCode());
            dto.setDpCode(mr.getDisciplineId());
            dto.setMrNo(mr.getTechnicalDocumentNoWison());
            dto.setMrName(mr.getFileName());
            dto.setBuyer(mr.getBuyer());
            dto.setOwnerMrNo(mr.getTechnicalDocumentNoOthers());
            ProjectMrPlanT planBo = new ProjectMrPlanT();
            planBo.setZoneCode(mr.getZoneCode());
            planBo.setProjectCode(mr.getProjectCode());
            planBo.setTechnicalDocumentNoWison(mr.getTechnicalDocumentNoWison());
            ProjectMrPlanT plan = projectMrPlanTService.selectOneByBo(planBo);
            if (null != plan) {
                dto.setSubProjectCode(plan.getPrincipalItemCode());
            }
            materialRequisitionDTOS.add(dto);
            boolean res = supervisionDubboService.pushMaterialRequisitionToSupervision(materialRequisitionDTOS);
            if (!res) {
                throw new BusinessException("推送失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("推送成功");
    }
}