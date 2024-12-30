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
public class WorkflowRequestTableField implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "web1:edit")
    private boolean edit;
    @XmlElement(name = "web1:fieldName")
    private String fieldName;
    @XmlElement(name = "web1:fieldValue")
    private String fieldValue;
    @XmlElement(name = "web1:view")
    private boolean view;
}
