package com.shuhg.service;

import com.shuhg.Main;
import com.shuhg.queue.DelayMessage;
import com.shuhg.queue.DelayQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class ThreadExecute {
    final static DelayQueue delayQueue = new DelayQueue();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(60);

    public void addMessage(DelayMessage delayMessage, ExecuteTaskService executeTaskService) {
        delayQueue.addMessage(delayMessage, executeTaskService);
    }

    public void run() {
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                System.out.println("开始执行：" + Main.num + "   " + df.format(new Date()));
                delayQueue.run();
                //try {
                //   Thread.sleep(2000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                Main.num--;
            }
        }, 5, 1, TimeUnit.SECONDS);
    }

}
