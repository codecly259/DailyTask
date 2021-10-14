package com.maxinchun.dailytask.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtils
 *
 * @author maxinchun
 * @date 2021/10/14 下午9:45
 */
@Slf4j
public class HttpUtils {
    /**
     * 响应处理器
     */
    private static HttpResponse.BodyHandler<String> STRING_BODY_HANDLER = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
    /**
     * http 执行客户端
     */
    private static HttpClient CLIENT = HttpClient.newHttpClient();


    /**
     * get with HTTP Authorization
     * Authorization  使用 Basic 验证:  base64(账号:密码)
     *
     * @param url
     * @param account
     * @param passwd
     * @return
     */
    public static String getWithAuth(String url, String account, String passwd) {
        // Authorization  使用 Basic 验证:  base64(账号:密码)
        String auth = "Basic " + Base64.getEncoder().encodeToString((account + ":" + passwd).getBytes());
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", auth);

        return get(url, headMap);

    }

    /**
     * get 请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * get 请求, 自定义请求头
     *
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> headers) {
        try {
            log.debug("请求 url: {}", url);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse<String> response = CLIENT.send(builder.build(), STRING_BODY_HANDLER);
            String body = response.body();
            log.debug("请求返回数据:{}", body);
            return body;
        } catch (Exception e) {
            log.error("请求失败, url: {}", url, e);
            return null;
        }
    }

    /**
     * post 请求 json 数据
     *
     * @param url
     * @param json
     * @return
     */
    public static String postJson(String url, String json) {
        String result = "";
        log.debug("请求 url: {}", url);
        log.debug("请求参数: {}", json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            result = CLIENT.send(request, STRING_BODY_HANDLER).body();
            log.debug("请求响应数据:{}", result);
        } catch (Exception e) {
            log.error("发送 post 请求失败, 请求 url:{}, 请求数据:{}", url, json, e);
        }
        return result;
    }

}
