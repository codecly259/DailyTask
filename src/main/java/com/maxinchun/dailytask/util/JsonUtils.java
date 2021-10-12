package com.maxinchun.dailytask.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @描述
 * @创建时间 2021/10/6 2:22
 * @创建人 maxinchun
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * json 字符串转换为对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("解析 json 字符串异常", e);
            return null;
        }
    }

    /**
     * 类转换为字符串
     * @param clazz
     * @return
     */
    public static String  toJson(Object clazz) {
        try {
            return OBJECT_MAPPER.writeValueAsString(clazz);
        } catch (Exception e) {
            log.error("转换成 json 字符串异常", e);
            return null;
        }
    }

}
