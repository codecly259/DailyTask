package com.maxinchun.dailytask.util;

import org.junit.Test;

/**
 * 日期工具测试类
 *
 * @author Lucky-maxinchun
 * @date 2021/10/12 10:31
 */
public class DayUtilsTest {

    @Test
    public void dayDiffTest() {
        int dayDiff = DayUtils.dayDiff(10);
        int dayDiff1 = DayUtils.dayDiff(12);
        int dayDiff2 = DayUtils.dayDiff(13);
        System.out.println(dayDiff);
        System.out.println(dayDiff1);
        System.out.println(dayDiff2);
    }

}
