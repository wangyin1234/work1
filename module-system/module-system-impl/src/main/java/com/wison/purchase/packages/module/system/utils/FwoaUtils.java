package com.wison.purchase.packages.module.system.utils;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.FwoaAutoComplete;
import com.wison.purchase.packages.comm.utils.HttpUtils;
import com.wison.purchase.packages.comm.utils.XmlUtils;
import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import com.wison.purchase.packages.module.system.domain.request.*;
import com.wison.purchase.packages.module.system.domain.response.CommonOaResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
public class FwoaUtils {

    private static String FWOA_URL;

    private static String request(Object object, String method) {
        return postXml(FWOA_URL, object, method);
    }

    public static String doCreate(OaInfo oaInfo, List<WorkflowRequestTableField> workflowRequestTableFields, List<WorkflowDetailTable> workflowDetailTables) {
        WorkflowBaseInfo workflowBaseInfo = WorkflowBaseInfo.builder().workflowId(oaInfo.getWorkFlowId()).build();
        WorkflowMainTableInfo workflowMainTableInfo = WorkflowMainTableInfo.builder().requestRecords(new RequestRecordsInfo(new WorkflowRequestTableRecord(new WorkflowRequestTableFields(workflowRequestTableFields)))).build();
        WorkflowDetailTableInfo workflowDetailTableInfo = WorkflowDetailTableInfo.builder().workflowDetailTables(workflowDetailTables).build();
        FlowInfo flowInfo = FlowInfo.builder().requestName(oaInfo.getRequestName()).creatorId(oaInfo.getCreatorId()).workflowBaseInfo(workflowBaseInfo).workflowMainTableInfo(workflowMainTableInfo).workflowDetailTableInfo(workflowDetailTableInfo).build();
        DoCreateFlowInfo doCreateFlowInfo = DoCreateFlowInfo.builder().in0(flowInfo).in1(oaInfo.getCreatorId()).build();
        DoCreateFlowMethodInfo doCreateFlowMethodInfo = DoCreateFlowMethodInfo.builder().method(doCreateFlowInfo).build();
        return request(doCreateFlowMethodInfo, "doCreateWorkflowRequestResponse");
    }

    public static String getUserId(String loginName) {
        GetUserIdInfo getUserIdInfo = GetUserIdInfo.builder().in0(loginName).build();
        GetUserIdMethod getUserIdMethod = GetUserIdMethod.builder().method(getUserIdInfo).build();
        return request(getUserIdMethod, "getUserIdResponse");
    }

    public static String commonValidate(FwoaAutoComplete fwoaAutoComplete) {
        log.info("流程结束");
        JSONObject json = JSONUtil.parseObj(fwoaAutoComplete.getJsonStr());
        String requestId = String.valueOf(json.get("requestId"));
        if (ObjectUtil.isEmpty(requestId)) {
            log.info("流程ID不能为空");
            throw new BusinessException("流程ID不能为空");
        }
        log.info(requestId);
        return requestId;
    }

    public static RequestBody buildXml(Object object) {
        CommonXmInfo commonXmInfo = CommonXmInfo.builder().body(object).build();
        String xml = XmlUtils.toXml(commonXmInfo);
        String decodeStr = URLDecoder.decode(xml, Charset.defaultCharset());
        log.info("xml: {}", decodeStr);
        MediaType mediaType = MediaType.parse("text/xml");
        return RequestBody.Companion.create(xml == null ? "" : decodeStr, mediaType);
    }

    private static String postXml(String url, Object object, String method) {
        RequestBody body = buildXml(object);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "text/xml")
                .post(body)
                .build();
        String res = HttpUtils.commonHttp(request);
        CommonOaResponse oaResponse = new CommonOaResponse(res, method);
        return (String) oaResponse.getOut();
    }

    @Value("${fwoa.url}")
    public void setFwoaUrl(String fwoaUrl) {
        FWOA_URL = fwoaUrl;
    }

}
