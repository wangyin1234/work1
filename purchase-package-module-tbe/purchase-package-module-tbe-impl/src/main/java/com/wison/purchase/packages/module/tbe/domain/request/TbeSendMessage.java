package com.wison.purchase.packages.module.tbe.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wison.purchase.packages.comm.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (tbe_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TbeSendMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 板块
     */
    private String zoneCode;

    /**
     * 项目code
     */
    private String projectCode;

    /**
     * 询价编号
     */
    private String priceCheckNumber;
    /**
     * 供应商编码
     */
    private String vendorCode;

    /**
     * 不再参与日期
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private Date participateFlagUpdateDate;

    /**
     * 技术评审意见
     */
    private boolean participateFlag;
}