package com.wison.purchase.packages.comm.domain;


import com.wison.base.core.domain.Result;

/**
 * web层通用数据处理
 */
public class BaseController {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Result<Void> toAjax(int rows) {
        return toAjax(rows, "操作");
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Result<Void> toAjax(int rows, String msgPrefix) {
        return rows > 0 ? Result.success(msgPrefix + "成功") : Result.error("失败");
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected Result<Void> toAjax(boolean result) {
        return toAjax(result, "操作");
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected Result<Void> toAjax(boolean result, String msgPrefix) {
        return result ? Result.success(msgPrefix + "成功") : Result.error("失败");
    }
}
