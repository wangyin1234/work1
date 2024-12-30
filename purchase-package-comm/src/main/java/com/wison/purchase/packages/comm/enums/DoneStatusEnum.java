package com.wison.purchase.packages.comm.enums;

import cn.hutool.core.util.StrUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum DoneStatusEnum {
    /**
     * 未分配
     */
    NOT_ALLOCATED("1", "未分配"),
    /**
     * 已分配
     */
    ASSIGNED("2", "已分配"),
    /**
     * 不采购
     */
    NOT_PURCHASE("3", "不采购"),
    /**
     * 驳回
     */
    REJECT("4", "驳回");

    /**
     * code
     */
    public final String code;

    /**
     * 描述
     */
    public final String value;

    /**
     * 获取流程状态描述
     *
     * @param code 类型
     */
    public static String findByType(String code) {
        if (StringUtils.isBlank(code)) {
            return StrUtil.EMPTY;
        }
        return Arrays.stream(DoneStatusEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getCode().equals(code))
                .findFirst()
                .map(DoneStatusEnum::getValue)
                .orElse(StrUtil.EMPTY);
    }

    /**
     * 获取值
     *
     * @param code 类型
     */
    public static DoneStatusEnum get(String code) {
        return Arrays.stream(DoneStatusEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getCode().equals(code))
                .findFirst().orElse(null);
    }
}
