package com.wison.purchase.packages.module.main.service;


import com.wison.purchase.packages.comm.domain.BaseService;
import com.wison.purchase.packages.module.main.domain.vo.MrReleaseTVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 设计询价文件发布服务接口
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
public interface MrReleaseTService extends BaseService<MrReleaseTVo, MrReleaseTVo> {
    MrReleaseTVo selectOneByBo(MrReleaseTVo bo);

    void exportExcel(List<MrReleaseTVo> list, HttpServletResponse response) throws IOException;
}
