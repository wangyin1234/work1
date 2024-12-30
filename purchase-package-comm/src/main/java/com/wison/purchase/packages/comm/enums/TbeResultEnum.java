package com.wison.purchase.packages.comm.enums;

import cn.hutool.core.util.StrUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TbeResultEnum {
    /**
     * 合格优选
     */
    QUALIFIED_PREFERENCE("0", "合格优选"),
    /**
     * 合格
     */
    QUALIFIED("1", "合格"),
    /**
     * 合格慎选
     */
    QUALIFIED_CAREFULLY("2", "合格慎选"),
    /**
     * 不合格
     */
    UNQUALIFIED("3", "不合格");

    /**
     * 结果
     */
    public final String result;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 获取结果描述
     *
     * @param result 类型
     */
    public static String findByResult(String result) {
        if (StringUtils.isBlank(result)) {
            return StrUtil.EMPTY;
        }
        return Arrays.stream(TbeResultEnum.values())
                .filter(tbeResultEnum -> tbeResultEnum.getResult().equals(result))
                .findFirst()
                .map(TbeResultEnum::getDesc)
                .orElse(StrUtil.EMPTY);
    }

    /**
     * 获取结果
     *
     * @param result 类型
     */
    public static TbeResultEnum get(String result) {
        return Arrays.stream(TbeResultEnum.values())
                .filter(tbeResultEnum -> tbeResultEnum.getResult().equals(result))
                .findFirst().orElse(null);
    }
}
