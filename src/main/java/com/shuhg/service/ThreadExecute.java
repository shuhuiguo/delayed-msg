package com.shuhg.service;

import com.shuhg.queue.DelayMessage;
import com.shuhg.queue.DelayQueue;
import com.shuhg.queue.TaskRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class ThreadExecute {
    final static DelayQueue delayQueue = new DelayQueue();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void addMessage(DelayMessage delayMessage, ExecuteTaskService executeTaskService) {
        delayQueue.addMessage(delayMessage, executeTaskService);
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(new TaskRunnable(this.delayQueue), 2, 1, TimeUnit.SECONDS);
    }

}
