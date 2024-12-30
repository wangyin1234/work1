package com.wison.purchase.packages.comm.enums;

import cn.hutool.core.util.StrUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ProcessStatusEnum {
    /**
     * 未提交
     */
    Not_COMMIT("0", "未提交"),
    /**
     * 审核中
     */
    REVIEWING("1", "审核中"),
    /**
     * 完成
     */
    COMPLETE("2", "完成"),
    /**
     * 驳回
     */
    DISMISS("3", "驳回");

    /**
     * 状态
     */
    public final String status;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 获取流程状态描述
     *
     * @param status 类型
     */
    public static String findByType(String status) {
        if (StringUtils.isBlank(status)) {
            return StrUtil.EMPTY;
        }
        return Arrays.stream(ProcessStatusEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getStatus().equals(status))
                .findFirst()
                .map(ProcessStatusEnum::getDesc)
                .orElse(StrUtil.EMPTY);
    }

    /**
     * 获取流程状态
     *
     * @param status 类型
     */
    public static ProcessStatusEnum get(String status) {
        return Arrays.stream(ProcessStatusEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getStatus().equals(status))
                .findFirst().orElse(null);
    }
}
