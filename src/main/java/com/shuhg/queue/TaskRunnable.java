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
    private DelayCycleQueue delayCycleQueue;

    public TaskRunnable(DelayCycleQueue delayCycleQueue){
        this.delayCycleQueue = delayCycleQueue;
    }

    @Override
    public void run() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        LOGGER.info("开始执行：{}   当前节点：{}", DelayCycleQueue.currentIndex.get() ,df.format(new Date()));
        if (DelayCycleQueue.currentIndex.get() >= 3600) {
            DelayCycleQueue.currentIndex.getAndSet(0);
        }
        executorService.submit( new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delayCycleQueue.run(DelayCycleQueue.currentIndex.get());
            }
        });
        DelayCycleQueue.currentIndex.addAndGet(1);

    }
}
