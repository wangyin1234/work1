package com.wison.purchase.packages.module.system.api.service;


import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import com.wison.purchase.packages.module.system.api.domain.bo.FlowLogTBo;
import com.wison.purchase.packages.module.system.api.domain.response.FwoaUserIdRes;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 */
public interface FwoaService {

    /**
     * 发起流程
     */
    <T extends OaInfo> String create(T t, Class<T> clazz) throws IllegalAccessException;

    /**
     * 获取用户id
     */
    String getUserId() throws IllegalAccessException;

    /**
     * 获取用户id
     */
    String getUserId(FwoaUserIdRes fwoaUserIdRes) throws IllegalAccessException;

    /**
     * 获取用户id
     */
    Map<String, FwoaUserIdRes> getUserId(List<FwoaUserIdRes> list) throws IllegalAccessException;

    /**
     * 删除流程
     */
    void delete(FlowLogTBo bo);
}
