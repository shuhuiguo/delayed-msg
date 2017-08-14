package com.shuhg;

import com.shuhg.queue.DelayMessage;
import com.shuhg.service.DefaultExecuteTaskServiceImpl;
import com.shuhg.service.ExecuteTaskService;
import com.shuhg.service.ThreadExecute;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class Main {
    ThreadExecute execute = new ThreadExecute();
    public static int num = 3600;

    public static void main(String[] args) {
        ThreadExecute execute = new ThreadExecute();
        ExecuteTaskService defaultService= new DefaultExecuteTaskServiceImpl();
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setDelayTime("0.2h,10s,20");
        delayMessage.setMsg("test msg!");
        execute.addMessage(delayMessage,defaultService);
        execute.run();


    }

}
