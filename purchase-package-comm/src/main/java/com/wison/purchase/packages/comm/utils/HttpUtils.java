package com.wison.purchase.packages.comm.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    private final static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).callTimeout(60, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
            .build();
    ;
    private final static MediaType defaultMediaType = MediaType.parse("application/json;charset=utf-8");
    private final static String defaultContentType = "application/json";

    private static RequestBody buildJson(Object object) {
        String jsonString = JsonUtils.toJsonString(object);
        return RequestBody.Companion.create(jsonString == null ? "" : jsonString, defaultMediaType);
    }

    public static <T> T commonHttpJson(Request request) {
        String resBody = commonHttp(request);
        Map<String, Object> map = JsonUtils.parseMap(resBody);
        return Convert.convert(new TypeReference<T>() {
        }, map);
    }

    public static String commonHttp(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            log.info("流程返回数据：{}", response);
            if (response.body() != null && response.isSuccessful()) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getJson(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", defaultContentType)
                .get()
                .build();
        return commonHttpJson(request);
    }

    public static <T> T postJson(String url, Object object) {
        RequestBody body = buildJson(object);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", defaultContentType)
                .post(body)
                .build();
        return commonHttpJson(request);
    }
}
