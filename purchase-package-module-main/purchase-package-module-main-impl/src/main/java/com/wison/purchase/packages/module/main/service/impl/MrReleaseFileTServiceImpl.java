package com.wison.purchase.packages.module.main.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wison.purchase.packages.module.main.domain.MrReleaseFileT;
import com.wison.purchase.packages.module.main.mapper.MrReleaseFileTMapper;
import com.wison.purchase.packages.module.main.service.MrReleaseFileTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务接口实现
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MrReleaseFileTServiceImpl implements MrReleaseFileTService {
    private final MrReleaseFileTMapper mrReleaseFileTMapper;

    @Override
    public Page<MrReleaseFileT> list(MrReleaseFileT bo) {
        return null;
    }

    @Override
    public MrReleaseFileT selectOne(MrReleaseFileT bo) {
        return null;
    }

    @Override
    public int updateOne(MrReleaseFileT bo) {
        return 0;
    }

    @Override
    public int updateByBo(MrReleaseFileT bo) {
        return 0;
    }

    @Override
    public boolean updateAll(List<MrReleaseFileT> boList) {
        return false;
    }

    @Override
    public int insertOne(MrReleaseFileT bo) {
        return 0;
    }

    @Override
    public boolean insertAll(List<MrReleaseFileT> boList) {
        return false;
    }

    @Override
    public int deleteOne(String id) {
        return 0;
    }

    @Override
    public int deleteByBo(MrReleaseFileT bo) {
        return 0;
    }

    @Override
    public int deleteAll(List<String> idList) {
        return 0;
    }

    @Override
    public boolean insertOrUpdate(List<MrReleaseFileT> boList) {
        return false;
    }
}