package com.wison.purchase.packages.module.system.domain.response;

import cn.hutool.core.util.XmlUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonOaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object out;

    @SuppressWarnings({"unchecked"})
    public CommonOaResponse(String res, String method) {
        Document doc = XmlUtil.parseXml(res);
        Element eleG = XmlUtil.getRootElement(doc);
        Map<String, Object> oaResponse = XmlUtil.xmlToMap(eleG);
        Map<String, Object> body = (Map<String, Object>) oaResponse.get("soap:Body");
        Map<String, Object> resOut = (Map<String, Object>) body.get("ns1:" + method);
        this.out = resOut.get("ns1:out");
    }
}
