package com.maxinchun.dailytask.config;

import lombok.Data;

import java.util.List;

/**
 * 类描述
 *
 * @author Lucky-maxinchun
 * @date 2021/10/11 19:06
 */
@Data
public class TaskConfig {

    private List<Bill> bills;

    @Data
    public static class Bill {
        private String name;
        private int billDate;
        private int dueDate;
    }

}
