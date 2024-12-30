package com.wison.purchase.packages.module.system.domain.request;


import lombok.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "soapenv:Envelope")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DoCreateFlowMethodInfo.class, GetUserIdMethod.class})
public class CommonXmInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "xmlns:soapenv")
    private final String soapenv = "http://schemas.xmlsoap.org/soap/envelope/";
    @XmlAttribute(name = "xmlns:web")
    private final String web = "webservices.services.weaver.com.cn";
    @XmlAttribute(name = "xmlns:web1")
    private final String web1 = "http://webservices.workflow.weaver";
    @XmlElement(name = "soapenv:Header")
    private final String Header = "";
    @XmlElement(name = "soapenv:Body")
    private Object body;
}


