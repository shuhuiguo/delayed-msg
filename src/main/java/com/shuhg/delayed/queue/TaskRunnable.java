package com.shuhg.delayed.queue;

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
        LOGGER.debug("开始执行：{}   当前节点：{}",df.format(new Date()), delayCycleQueue.getCurrentIndex());
        //当节点大于3600，重新开始循环
        if (delayCycleQueue.getCurrentIndex()>= 3600) {
            delayCycleQueue.setCurrentIndex(0);
        }
        //TODO 同步index到redis
        final int index = delayCycleQueue.getCurrentIndex();
        executorService.submit( new Runnable(){

            @Override
            public void run() {
                delayCycleQueue.run(index);
            }
        });
        delayCycleQueue.addAndGetCurrentIndex(1);

    }
}
