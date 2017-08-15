package com.shuhg.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 大舒 on 2017/8/15.
 */
public class TaskRunnable implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskRunnable.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(100);
    private DelayQueue delayQueue;

    public TaskRunnable(DelayQueue delayQueue){
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        LOGGER.info("开始执行：{}   当前节点：{}", DelayQueue.currentIndex.get() ,df.format(new Date()));
        if (DelayQueue.currentIndex.get() >= 3600) {
            DelayQueue.currentIndex.getAndSet(0);
        }
        executorService.submit( new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delayQueue.run(DelayQueue.currentIndex.get());
            }
        });
        DelayQueue.currentIndex.addAndGet(1);

    }
}
