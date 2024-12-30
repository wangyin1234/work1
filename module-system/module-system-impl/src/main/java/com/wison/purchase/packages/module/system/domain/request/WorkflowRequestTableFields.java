package com.wison.purchase.packages.module.system.domain.request;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowRequestTableFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "web1:WorkflowRequestTableField")
    private List<WorkflowRequestTableField> workflowRequestTableFields;
}
