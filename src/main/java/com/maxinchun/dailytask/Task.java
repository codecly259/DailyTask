package com.maxinchun.dailytask;

/**
 * @描述 所有的任务功能都要实现这个接口，以达到动态加载
 * @创建时间 2021/9/29 22:23
 * @创建人 maxinchun
 */
public interface Task {

    /**
     * 任务功能开始的地方
     */
    void run();

}
