package com.wison.purchase.packages.comm.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.FwoaAutoComplete;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FwoaUtils {

    public static String commonValidate(FwoaAutoComplete fwoaAutoComplete) {
        log.info("流程结束");
        JSONObject json = JSONUtil.parseObj(fwoaAutoComplete.getJsonStr());
        String requestId = String.valueOf(json.get("requestId"));
        if (ObjectUtil.isEmpty(requestId)) {
            log.info("流程ID不能为空");
            throw new BusinessException("流程ID不能为空");
        }
        log.info(requestId);
        return requestId;
    }

}
