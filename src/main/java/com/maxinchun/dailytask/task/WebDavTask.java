package com.maxinchun.dailytask.task;

import com.maxinchun.dailytask.Task;
import com.maxinchun.dailytask.config.TaskConfig;
import com.maxinchun.dailytask.util.DayUtils;
import com.maxinchun.dailytask.util.JsonUtils;
import com.maxinchun.dailytask.util.SendDingTalk;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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

        StringBuilder resultBuilder = new StringBuilder();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String result = response.body();
            log.debug("dailyConfig: {}", result);

            // 开始解析任务配置
            Yaml yaml = new Yaml();
            TaskConfig taskConfig = yaml.loadAs(result, TaskConfig.class);
            if (taskConfig == null || taskConfig.getBills() == null || taskConfig.getBills().size() < 1) {
                log.warn("没有账单配置，不做账单处理!!!");
                return;
            }

            // 循环处理每个日期判断
            for (TaskConfig.Bill bill : taskConfig.getBills()) {
                log.debug("开始判断提醒日期判断,名称：[{}]", bill.getName());
                // 1. 账单日判断, 当天提醒
                int billDay = bill.getBillDay();
                if (billDay > 0) {
                    log.debug("开始账单日判断...[{}]", billDay);
                    int dayDiff = DayUtils.dayDiff(billDay);
                    if (dayDiff == 0) {
                        // 需要通知：账单日
                        resultBuilder.append("今天是").append(bill.getName()).append("的账单日, 请查询关注还款数据").append("  \n  ");
                    }
                }

                // 2. 还款日判断, 当天及前两天(共3天)提醒
                int dueDay = bill.getDueDay();
                if (dueDay > 0) {
                    log.debug("开始还款日判断...[{}]", dueDay);
                    int dayDiff = DayUtils.dayDiff(dueDay);
                    if (dayDiff < 3) {
                        // 需要通知：还款日
                        if (dayDiff == 0) {
                            resultBuilder.append("今天是").append(bill.getName()).append("的还款日, 注意是否已还款").append("  \n  ");
                        } else {
                            resultBuilder.append("离").append(bill.getName()).
                                    append("的还款日还有"+dayDiff+"天, 请注意按时还款").append("\n");
                        }
                    }
                }
            }


        } catch (Exception e) {
            log.error("获取 webDav 文件失败", e);
        }

        // 发送结果通知
        if (StringUtils.isNotBlank(System.getenv("DING_TALK_URL")) && StringUtils.isNotBlank(resultBuilder.toString())) {
            // 一言: international.v1.hitokoto.cn
            HttpRequest hitokotoRequest = HttpRequest.newBuilder().uri(URI.create("https://international.v1.hitokoto.cn")).GET().build();
            try {
                HttpResponse<String> hitokotoResponse = client.send(hitokotoRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                if (hitokotoResponse.statusCode() == 200) {
                    String body = hitokotoResponse.body();
                    Map map = JsonUtils.fromJson(body, Map.class);
                    log.info("一言：{}", map.get("hitokoto"));
                    resultBuilder.append(map.get("hitokoto"));
                } else {
                    log.error("hitokoto 返回码:{}", hitokotoResponse.statusCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(resultBuilder.toString());

            SendDingTalk.send(System.getenv("DING_TALK_URL"), resultBuilder.toString());
        }

    }
}
