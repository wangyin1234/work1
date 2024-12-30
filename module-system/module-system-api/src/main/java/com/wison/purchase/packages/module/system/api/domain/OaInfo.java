package com.wison.purchase.packages.module.system.api.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class OaInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String creatorId;
    private String requestName;
    private String workFlowId;
}
