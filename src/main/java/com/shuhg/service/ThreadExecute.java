package com.shuhg.service;

import com.alibaba.fastjson.JSONObject;
import com.shuhg.queue.*;
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
    public static DelayCycleQueue delayCycleQueue = new DelayCycleQueue();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void addMessage(DelayMessage delayMessage, ExecuteTaskService executeTaskService) {
        delayCycleQueue.addMessage(delayMessage, executeTaskService);
    }

    public void addMessage(Task task, int nodeIndex) {
        delayCycleQueue.addMessage(nodeIndex, task);
    }

    /**
     * 初始化消息
     * @param executeTaskServiceMap 消息类型-实际任务执行类
     */
    public void initMessage(Map<String, ExecuteTaskService> executeTaskServiceMap) {
        Set<Tuple> set = delayCycleQueue.getAllDelayMessage();
        TaskNode taskNode = null;
        ExecuteTaskService taskService = null;
        for (Tuple tuple : set) {
            taskNode = JSONObject.parseObject(tuple.getElement(), TaskNode.class);
            if (taskNode.getTasks() != null && taskNode.getTasks().size() > 0) {
                for (Task task : taskNode.getTasks()) {

                    //默认
                    if (executeTaskServiceMap == null || executeTaskServiceMap.get(task.getDelayMessage().getType()) == null) {
                        taskService = new DefaultExecuteTaskServiceImpl();
                    }else {
                        taskService = executeTaskServiceMap.get(task.getDelayMessage().getType());
                    }
                    task.setTaskService(taskService);
                    addMessage(task, taskNode.getNodeIndex());
                }

            }
        }

        delayCycleQueue.initCurrentIndex();
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(
                new TaskRunnable(this.delayCycleQueue), 2, 1, TimeUnit.SECONDS);
    }


}
