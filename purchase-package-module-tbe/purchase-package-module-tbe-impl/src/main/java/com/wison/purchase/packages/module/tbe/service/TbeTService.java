package com.wison.purchase.packages.module.tbe.service;


import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.tbe.domain.TbeT;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;

import java.util.List;

/**
 * 服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
public interface TbeTService extends BaseService<TbeTVo, TbeTBo> {
    void sendToSupplier(TbeTVo bo);

    boolean sendToPur(TbeTBo bo);

    TbeT selectOneByBo(TbeTBo bo);

    List<TbeTVo> allList(TbeTBo bo);
}
