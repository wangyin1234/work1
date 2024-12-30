package com.wison.purchase.packages.module.tbe.domain.request;

import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (tbe_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-10 09:45:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TbeTNotify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 开标日期
     */
    private String BidOpenDate;

    /**
     * 采购包号
     */
    private String priceCheckNumber;
}