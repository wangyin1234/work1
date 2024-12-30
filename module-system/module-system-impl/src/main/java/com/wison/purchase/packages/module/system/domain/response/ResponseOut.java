package com.wison.purchase.packages.module.system.domain.response;


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
public class ResponseOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "ns1:out")
    private String out;
}
