package com.wison.purchase.packages.module.tbe.domain.request;

import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TbeOaInfo extends OaInfo {

    private static final long serialVersionUID = 1L;

    private String packetNumber;
    private String clarificationDeadline;
    private String technicalReviewTime;
    private String inquiryRequirements;
    private String fileUrl;
    private String inquiryFileUrl;
    private String status;
    private String projectCode;
    private String approver;
    private String applicant;
    private List<SupplierOaInfo> suppliers;
}
