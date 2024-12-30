package com.wison.purchase.packages.module.main.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * (mr_release_file_t)实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-10-29 12:45:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mr_release_file_t")
public class MrReleaseFileT extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * fileName
     */
    private String fileName;
    /**
     * fileLink
     */
    private String fileLink;
    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    /**
     * 询价技术文件编号(WISON)
     */
    private String technicalDocumentNoWison;
    /**
     * 版本
     */
    private String version;

    /**
     * 类别
     */
    private String category;

}