package com.wison.purchase.packages.module.system.domain.request;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "web1:creatorId")
    private String creatorId;
    @XmlElement(name = "web1:requestName")
    private String requestName;
    @XmlElement(name = "web1:workflowBaseInfo")
    private WorkflowBaseInfo workflowBaseInfo;
    @XmlElement(name = "web1:workflowMainTableInfo")
    private WorkflowMainTableInfo workflowMainTableInfo;
    @XmlElement(name = "web1:workflowDetailTableInfos")
    private WorkflowDetailTableInfo workflowDetailTableInfo;
}
