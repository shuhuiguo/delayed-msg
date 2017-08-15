package com.shuhg.service;

import com.shuhg.queue.DelayMessage;
import com.shuhg.queue.DelayCycleQueue;
import com.shuhg.queue.TaskRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class ThreadExecute {
    final static DelayCycleQueue DELAY_CYCLE_QUEUE = new DelayCycleQueue();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void addMessage(DelayMessage delayMessage, ExecuteTaskService executeTaskService) {
        DELAY_CYCLE_QUEUE.addMessage(delayMessage, executeTaskService);
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(new TaskRunnable(this.DELAY_CYCLE_QUEUE), 2, 1, TimeUnit.SECONDS);
    }

}
