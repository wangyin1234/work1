package com.wison.purchase.packages.module.tbe.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wison.purchase.packages.module.tbe.api.domain.bo.MainTbeTBo;
import com.wison.purchase.packages.module.tbe.api.service.MainTbeService;
import com.wison.purchase.packages.module.tbe.domain.TbeT;
import com.wison.purchase.packages.module.tbe.mapper.TbeTMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class MainTbeServiceImpl implements MainTbeService {
    private final TbeTMapper tbeTMapper;

    @Override
    public void createTbe(MainTbeTBo bo) {
        TbeT existEntity = tbeTMapper.selectOne(buildOneLqw(bo));
        if (null == existEntity) {
            TbeT entity = BeanUtil.toBean(bo, TbeT.class);
            tbeTMapper.insert(entity);
        }
    }

    private LambdaQueryWrapper<TbeT> buildOneLqw(MainTbeTBo bo) {
        LambdaQueryWrapper<TbeT> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TbeT::getZoneCode, bo.getZoneCode());
        lqw.eq(TbeT::getProjectCode, bo.getProjectCode());
        lqw.eq(TbeT::getPriceCheckNumber, bo.getPriceCheckNumber());
        return lqw;
    }
}