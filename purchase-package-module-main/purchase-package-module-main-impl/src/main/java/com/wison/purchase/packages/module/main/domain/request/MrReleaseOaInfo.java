package com.wison.purchase.packages.module.main.domain.request;

import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MrReleaseOaInfo extends OaInfo {

    private static final long serialVersionUID = 1L;

    private String type;
    private String wisonFileNum;
    private String otherFileNum;
    private String fileName;
    private String version;
    private String writeDate;
    private String remark;
    private String fileUrl;
    private String isReview;
    private String designer;
    private String engineers;
    private String status;
    private String applicant;
    private String flag;
    private String projectCode;
}
