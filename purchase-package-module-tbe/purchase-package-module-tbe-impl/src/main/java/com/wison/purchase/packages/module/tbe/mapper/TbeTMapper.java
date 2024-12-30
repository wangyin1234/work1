package com.wison.purchase.packages.module.tbe.mapper;

import com.wison.purchase.packages.comm.domain.BaseMapperPlus;
import com.wison.purchase.packages.module.tbe.domain.TbeT;
import com.wison.purchase.packages.module.tbe.domain.bo.TbeTBo;
import com.wison.purchase.packages.module.tbe.domain.vo.TbeTVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * (tbe_t)数据Mapper
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Mapper
public interface TbeTMapper extends BaseMapperPlus<TbeT, TbeTVo> {
    TbeTVo queryList(TbeTBo bo);
}
