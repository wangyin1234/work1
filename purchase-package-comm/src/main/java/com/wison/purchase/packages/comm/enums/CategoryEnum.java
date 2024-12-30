package com.wison.purchase.packages.comm.enums;

import cn.hutool.core.util.StrUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CategoryEnum {
    /**
     * 供请购
     */
    FOR_PURCHASE("1", "供请购"),
    /**
     * 供预询价
     */
    FOR_INQUIRY("2", "供预询价");

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
        return Arrays.stream(CategoryEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getCode().equals(code))
                .findFirst()
                .map(CategoryEnum::getValue)
                .orElse(StrUtil.EMPTY);
    }

    /**
     * 获取值
     *
     * @param code 类型
     */
    public static CategoryEnum get(String code) {
        return Arrays.stream(CategoryEnum.values())
                .filter(flowStatusEnum -> flowStatusEnum.getCode().equals(code))
                .findFirst().orElse(null);
    }
}
