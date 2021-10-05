package com.maxinchun.dailytask.task;

import com.maxinchun.dailytask.Task;
import com.maxinchun.dailytask.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @描述
 * @创建时间 2021/9/29 22:43
 * @创建人 maxinchun
 */
@Slf4j
public class WebDavTask implements Task {
    @Override
    public void run() {
        String webDav = System.getenv("WEBDAV");
        String account = System.getenv("ACCOUNT");
        String passwd = System.getenv("PASSWD");

        // 获取任务配置
        log.debug("开始连接 webDav...");
        // Authorization  使用 Basic 验证:  base64(账号:密码)
        String auth = "Basic " + Base64.getEncoder().encodeToString((account+":"+passwd).getBytes());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webDav))
                .header("Authorization", auth)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String result = response.body();
            log.info("dailyConfig: {}", result);

            // 开始解析任务配置
        } catch (Exception e) {
            log.error("获取 webDav 文件失败", e);
        }

        // 一言: international.v1.hitokoto.cn
        HttpRequest hitokotoRequest = HttpRequest.newBuilder().uri(URI.create("https://international.v1.hitokoto.cn")).GET().build();
        try {
            HttpResponse<String> hitokotoResponse = client.send(hitokotoRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (hitokotoResponse.statusCode() == 200) {
                String body = hitokotoResponse.body();
                Map map = JsonUtils.fromJson(body, Map.class);
                log.info("一言：{}", map.get("hitokoto"));
            } else {
                log.error("hitokoto 返回码:{}", hitokotoResponse.statusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
