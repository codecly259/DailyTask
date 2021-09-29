package com.maxinchun.dailytask.util;

import com.maxinchun.dailytask.Task;
import com.maxinchun.dailytask.task.WebDavTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @描述
 * @创建时间 2021/9/29 22:41
 * @创建人 maxinchun
 */
@Slf4j
public abstract class TaskRegistry {
    private static final List<Class<? extends Task>> REGISTERED_TASKS = Arrays.asList(
            WebDavTask.class
    );

    /**
     * 运行所有已注册任务
     */
    public void runTasks() {
        REGISTERED_TASKS.stream().map(Class::getName).forEach(this::dealClass);
    }

    /**
     * 获得真实的className
     *
     * @param className className
     */
    public abstract void dealClass(String className);

}
