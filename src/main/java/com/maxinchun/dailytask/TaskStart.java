package com.maxinchun.dailytask;

import com.maxinchun.dailytask.util.TaskRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * 启动类，程序运行开始的地方
 */
@Slf4j
public class TaskStart {


    public static void main(String[] args) {
        if(checkEnv()){
            log.error("💔请在Github Secrets中添加你的Web dav 文件信息");
            return;
        }

        log.info("开始运行本次任务。");
        // 获取文件
        /* 动态执行task包下的所有java代码 */
        scanTask();

        log.info("本次任务运行完毕。");

    }

    /**
     *@描述 检查配置
     *@参数  []
     *@返回值  boolean
     *@创建人  maxinchun
     *@创建时间  2021/9/29
     *@修改人和其它信息
     */
    private static boolean checkEnv() {
        String webDav = System.getenv("webDav");
        String account = System.getenv("account");
        String passwd = System.getenv("passwd");
        String appId = System.getenv("appId");
        return StringUtils.isAnyBlank(webDav, account, passwd, appId);
    }

    /**
     * 存储所有 class 全路径名
     * 因为测试的时候发现，在 windows 中是按照字典排序的
     * 但是在 Linux 中并不是字典排序我就很迷茫
     * 因为部分任务是需要有顺序的去执行
     */
    private static void scanTask() {
        List<Class<?>> clazzList = new ArrayList<>();
        TaskRegistry pack = new TaskRegistry() {
            @Override
            public void dealClass(String className) {
                try{
                    Class<?> clazz = Class.forName(className);
                    // 判断类是否实现了接口Task
                    if (Arrays.stream(clazz.getInterfaces()).parallel().anyMatch(taskI -> taskI.equals(Task.class))) {
                        clazzList.add(clazz);
                    }
                } catch (Exception e){
                    log.error("💔反射获取对象错误 : ", e);
                }
            }
        };
        pack.runTasks();

        clazzList.stream().sorted(Comparator.comparing(Class::getName)).forEach(clazz -> {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object object = constructor.newInstance();
                Method method = object.getClass().getMethod("run");
                method.invoke(object);
            } catch (Exception e){
                log.error("💔反射获取对象错误 : ", e);
            }
        });
    }

}
