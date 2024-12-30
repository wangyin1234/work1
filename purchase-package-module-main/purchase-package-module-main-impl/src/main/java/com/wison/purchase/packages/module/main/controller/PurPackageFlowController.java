package com.wison.purchase.packages.module.main.controller;

import com.wison.base.core.domain.Result;
import com.wison.purchase.packages.comm.domain.BaseController;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import com.wison.purchase.packages.module.main.domain.PurPackageFlowT;
import com.wison.purchase.packages.module.main.service.PurPackageFlowTService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 项目级采购包流程
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purPackageFlow")
@Tag(name = "流程接口")
@Api(tags = "流程接口")
public class PurPackageFlowController extends BaseController {

    private final PurPackageFlowTService purPackageFlowTService;

    /**
     * 根据项目号查询流程
     */
    @Operation(summary = "查询一条数据")
    @PostMapping("/one")
    public Result<PurPackageFlowT> selectOne(@RequestBody @Valid PurPackageFlowT bo) {
        PurPackageFlowT entity = purPackageFlowTService.selectOne(bo);
        if (null == entity) {
            PurPackageFlowT vo = new PurPackageFlowT();
            vo.setStatus(ProcessStatusEnum.Not_COMMIT.status);
            return Result.success(vo);
        }
        return Result.success(entity);
    }
}