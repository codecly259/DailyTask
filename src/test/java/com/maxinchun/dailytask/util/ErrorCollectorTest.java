package com.maxinchun.dailytask.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.core.Is.is;

/**
 * @描述
 * @创建时间 2021/11/21 23:49
 * @创建人 maxinchun
 *
 * https://www.guru99.com/junit-errorcollector.html
 */
public class ErrorCollectorTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void test() {
        System.out.println("Hello");
        collector.checkThat("数字不相等1", 0, is(1));
        collector.checkThat("数字不相等2", 0, is(1));
        try {
            Assert.assertThat(1, is(2));
        } catch (Throwable e) {
            collector.addError(e);
        }

        collector.checkThat("数字不相等3", 0, is(1));
        System.out.println("World!!!!");
    }

}
