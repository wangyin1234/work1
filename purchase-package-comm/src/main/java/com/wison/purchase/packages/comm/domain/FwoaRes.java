package com.wison.purchase.packages.comm.domain;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FwoaRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    public static String ok() {
        return JSONUtil.toJsonStr(baseReturn("1", "成功"));
    }

    public static String error(String msg) {
        return JSONUtil.toJsonStr(baseReturn("0", msg));
    }

    public static String error() {
        return error("失败");
    }

    private static FwoaRes baseReturn(String code, String msg) {
        FwoaRes res = new FwoaRes();
        res.setCode(code);
        res.setMsg(msg);
        return res;
    }
}
