package com.wison.purchase.packages.module.tbe.enums;

import cn.hutool.core.util.StrUtil;
import com.wison.purchase.packages.comm.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TbeSupplierFileEnum {
    /**
     * 询价技术文件(澄清回复)
     */
    INQUIRY_TECHNICAL_DOC_REPLY("1", "询价技术文件(澄清回复)"),
    /**
     * 询价技术文件澄清
     */
    INQUIRY_TECHNICAL_DOC_CLARIFICATION("2", "询价技术文件澄清"),
    /**
     * 询价商务文件(澄清回复)
     */
    INQUIRY_BUSINESS_DOC_REPLY("3", "询价商务文件(澄清回复)"),
    /**
     * 询价商务文件澄清
     */
    INQUIRY_BUSINESS_DOC_CLARIFICATION("4", "询价商务文件澄清"),
    /**
     * 技术方案(澄清回复)
     */
    TECHNICAL_SOLUTION_REPLY("5", "技术方案(澄清回复)"),
    /**
     * 技术方案澄清
     */
    TECHNICAL_TECHNICAL("6", "技术方案澄清"),
    /**
     * 技术协议(澄清回复)
     */
    TA_REPLY("7", "技术协议(澄清回复)"),
    /**
     * 技术协议澄清
     */
    TA_CLARIFICATION("8", "技术协议澄清"),
    /**
     * 技术方案
     */
    TECHNICAL_SOLUTION("9", "技术方案");

    /**
     * type
     */
    public final String type;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 获取结果描述
     *
     * @param type 类型
     */
    public static String findByResult(String type) {
        if (StringUtils.isBlank(type)) {
            return StrUtil.EMPTY;
        }
        return Arrays.stream(TbeSupplierFileEnum.values())
                .filter(tbeResultEnum -> tbeResultEnum.getType().equals(type))
                .findFirst()
                .map(TbeSupplierFileEnum::getDesc)
                .orElse(StrUtil.EMPTY);
    }

    /**
     * 获取结果
     *
     * @param type 类型
     */
    public static TbeSupplierFileEnum get(String type) {
        return Arrays.stream(TbeSupplierFileEnum.values())
                .filter(tbeResultEnum -> tbeResultEnum.getType().equals(type))
                .findFirst().orElse(null);
    }
}
