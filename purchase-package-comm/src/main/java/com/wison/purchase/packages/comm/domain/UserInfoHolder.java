package com.wison.purchase.packages.comm.domain;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class UserInfoHolder implements Serializable {
    @Getter
    private static UserInfo userInfo;

    @SuppressWarnings("unchecked")
    public static void setUserInfo(Object obj) {
        Map<String, Object> map = (Map<String, Object>) obj;
        userInfo = BeanUtil.toBean(map.get("UserInfo"), UserInfo.class);
    }
}
