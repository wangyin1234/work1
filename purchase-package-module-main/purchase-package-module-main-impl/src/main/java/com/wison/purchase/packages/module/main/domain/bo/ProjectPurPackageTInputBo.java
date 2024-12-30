package com.wison.purchase.packages.module.main.domain.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wison.purchase.packages.module.main.domain.ProjectPurPackageT;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 项目级采购包(project_pur_package_t)导入实体类
 *
 * @author yinxin
 * @description 自动创建
 * @since 2024-09-23 15:21:39
 */
@Data
public class ProjectPurPackageTInputBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(serialize = false)
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    private ProjectPurPackageT projectPurPackage;
}