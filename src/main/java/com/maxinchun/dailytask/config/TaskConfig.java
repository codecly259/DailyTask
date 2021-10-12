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
        /**
         * 名称
         */
        private String name;
        /**
         * 账单日, 从1 开始; < 1 表示没有
         */
        private int billDay;
        /**
         * 还款日, 从1 开始
         */
        private int dueDay;
    }

}
