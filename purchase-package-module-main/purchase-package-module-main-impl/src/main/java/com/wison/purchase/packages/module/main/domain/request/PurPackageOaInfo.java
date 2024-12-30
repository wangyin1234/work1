package com.wison.purchase.packages.module.main.domain.request;

import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
public class PurPackageOaInfo extends OaInfo {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "项目号不能为空")
    private String projectCode;
    @NotBlank(message = "详情链接不能为空")
    private String packageDetails;
}
