package com.maxinchun.dailytask.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

        String result = HttpUtils.postJson(url, bodyJsonString);
        if (StringUtils.isBlank(result)) {
            log.error("发送钉钉失败!!");
        }

    }

}
