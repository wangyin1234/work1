package com.wison.purchase.packages.comm.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadInfoHolder {
    private static final HeadInfo headInfo = new HeadInfo();


    public static void save(String token, String zoneCode, String projectCode) {
        headInfo.setToken(token);
        headInfo.setZoneCode(zoneCode);
        headInfo.setProjectCode(projectCode);
    }

    public static String getToken() {
        return headInfo.getToken();
    }

    public static String getZoneCode() {
        return headInfo.getZoneCode();
    }

    public static String getProjectCode() {
        return headInfo.getProjectCode();
    }
}
