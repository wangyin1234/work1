package com.wison.purchase.packages.comm.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * Service接口
 */
public interface BaseService<V, B> {

    /**
     * 查询列表
     */
    Page<V> list(B bo);

    /**
     * 查询列表
     */
    V selectOne(B bo);

    /**
     * 更新列表
     */
    int updateOne(B bo);

    /**
     * 更新列表
     */
    int updateByBo(B bo);

    /**
     * 更新列表
     */
    boolean updateAll(List<B> boList);

    /**
     * 插入列表
     */
    int insertOne(B bo);

    /**
     * 插入列表
     */
    boolean insertAll(List<B> boList);

    /**
     * 删除列表
     */
    int deleteOne(String id);

    /**
     * 删除列表
     */
    int deleteByBo(B bo);

    /**
     * 删除列表
     */
    int deleteAll(List<String> idList);

    /**
     * 插入或更新列表
     */
    boolean insertOrUpdate(List<B> boList);
}
