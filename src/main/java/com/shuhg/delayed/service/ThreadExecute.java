package com.shuhg.delayed.service;

import com.alibaba.fastjson.JSONObject;
import com.shuhg.delayed.queue.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class ThreadExecute {
    private static Logger logger = LoggerFactory.getLogger(ThreadExecute.class);
    public static DelayCycleQueue delayCycleQueue = new DelayCycleQueue();
    private static ScheduledExecutorService executorService = null;


    public void addMessage(DelayMessage delayMessage, ExecuteTaskProcessor executeTaskService) {
        delayCycleQueue.addMessage(delayMessage, executeTaskService);
    }

    public void addMessage(Task task, int nodeIndex) {
        delayCycleQueue.addMessage(nodeIndex, task);
    }

    /**
     * 初始化消息
     * @param executeTaskServiceMap 消息类型-实际任务执行类
     */
    public void initMessage(Map<String, ExecuteTaskProcessor> executeTaskServiceMap) {

        Set<Tuple> set = delayCycleQueue.getAllDelayMessage();
        TaskNode taskNode = null;
        ExecuteTaskProcessor taskService = null;
        for (Tuple tuple : set) {
            taskNode = JSONObject.parseObject(tuple.getElement(), TaskNode.class);
            if (taskNode.getTasks() != null && taskNode.getTasks().size() > 0) {
                for (Task task : taskNode.getTasks()) {

                    //默认
                    if (executeTaskServiceMap == null || executeTaskServiceMap.get(task.getDelayMessage().getType()) == null) {
                        taskService = new DefaultExecuteTaskProcessor();
                    }else {
                        taskService = executeTaskServiceMap.get(task.getDelayMessage().getType());
                    }
                    task.setTaskService(taskService);
                    addMessage(task, taskNode.getNodeIndex());
                }

            }
        }

       int initIndex =  delayCycleQueue.initCurrentIndex();
        logger.info("初始化消息：节点index -- {} ,数据大小 -- {} ",initIndex,set.size());
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(
                new TaskRunnable(this.delayCycleQueue), 2, 1, TimeUnit.SECONDS);
    }

    public static void shutdownNow(){
        executorService.shutdownNow();
    }
    public static void init(){
        executorService =  Executors.newScheduledThreadPool(1);
    }
}
