package com.maxinchun.dailytask.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 日期工具类
 *
 * @author Lucky-maxinchun
 * @date 2021/10/12 10:18
 */
@Slf4j
public class DayUtils {


    /**
     * 今天到定天(账单日/还款日) 相差天数;
     * 过了当前月的账单日/还款日，则到判断下个月的账单日/还款日还需要多少天
     * @param givenDay
     * @return
     */
    public static int dayDiff(int givenDay) {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        log.debug("日期配置：[{}], 当前日期：[{}]", givenDay, dayOfMonth);
        int result = givenDay - dayOfMonth;
        if (result >= 0) {
            log.debug("当月还没有到配置日期，到当月日期还需[{}]天", result);
            return result;
        }
        LocalDate givenDate = LocalDate.of(today.getYear(), today.getMonth(), givenDay);
        // 下个账单日/还款日
        LocalDate nextGivenDate = givenDate.plusMonths(1);
        result = (int)today.until(nextGivenDate, ChronoUnit.DAYS);
        log.debug("已过当月配置日期，到下个日期还需[{}]天", result);
        return result;
    }

}
