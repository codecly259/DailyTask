package com.maxinchun.dailytask.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

/**
 * 类描述
 *
 * @author Lucky-maxinchun
 * @date 2021/10/12 12:48
 */
@Slf4j
public class SendDingTalk {

    public static void send(String url, String msg) {
        LinkedHashMap markdownJson = new LinkedHashMap();
        markdownJson.put("title", "账期运行结果");
        markdownJson.put("text", msg);
        LinkedHashMap bodyJson = new LinkedHashMap();
        bodyJson.put("msgtype", "markdown");
        bodyJson.put("markdown", markdownJson);
        String bodyJsonString = JsonUtils.toJson(bodyJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJsonString))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String body = response.body();
            System.out.println("钉钉通知结果：" + body);
        } catch (Exception e) {
            log.error("发送钉钉通知失败", e);
        }


    }

}
