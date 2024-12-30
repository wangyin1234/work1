package com.wison.purchase.packages.module.tbe.service;


import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeSupplierTVo;

/**
 * 服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
public interface TbeSupplierTService extends BaseService<TbeSupplierTVo, TbeSupplierTVo> {
    TbeSupplierTVo selectById(String id);

    void sendToSupplier(TbeSupplierTVo bo);
}
