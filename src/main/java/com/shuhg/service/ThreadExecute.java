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
    public final static DelayCycleQueue DELAY_CYCLE_QUEUE = new DelayCycleQueue();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void addMessage(DelayMessage delayMessage, ExecuteTaskService executeTaskService) {
        DELAY_CYCLE_QUEUE.addMessage(delayMessage, executeTaskService);
    }

    public void addMessage(Task task, int nodeIndex) {
        DELAY_CYCLE_QUEUE.addMessage(nodeIndex, task);
    }

    /**
     * 初始化消息
     * @param executeTaskServiceMap 消息类型-实际任务执行类
     */
    public void initMessage(Map<String, ExecuteTaskService> executeTaskServiceMap) {
        Set<Tuple> set = DELAY_CYCLE_QUEUE.getAllDelayMessage();
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

        DELAY_CYCLE_QUEUE.initCurrentIndex();
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(new TaskRunnable(this.DELAY_CYCLE_QUEUE), 2, 1, TimeUnit.SECONDS);
    }


}
