package com.shuhg.delayed;

import com.shuhg.delayed.queue.DelayMessage;
import com.shuhg.delayed.service.DefaultExecuteTaskServiceImpl;
import com.shuhg.delayed.service.ExecuteTaskService;
import com.shuhg.delayed.service.ThreadExecute;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class Main {
    final ThreadExecute execute = new ThreadExecute();
    public static int num = 3600;

    public Main(){
        //TODO 从redis恢复延时消息
        execute.initMessage(null);
    }
    public static void main(String[] args) {
        Main main = new Main();
        ExecuteTaskService defaultService= new DefaultExecuteTaskServiceImpl();
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setDelayTime("0.2h,10s,20");
        delayMessage.setMsg("test msg!");
        main.execute.addMessage(delayMessage,defaultService);
        ThreadExecute.init();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                //程序退出时，把当前节点index写入redis
                ThreadExecute.delayCycleQueue.syncCurrentIndex();
                ThreadExecute.shutdownNow();
            }
        });

        main.execute.run();
    }

}
