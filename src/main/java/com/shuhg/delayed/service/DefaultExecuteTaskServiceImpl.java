package com.shuhg.delayed.service;

import com.shuhg.delayed.queue.DelayMessage;

/**
 * Created by 大舒 on 2017/8/11.
 */
public class DefaultExecuteTaskServiceImpl implements ExecuteTaskService {
    @Override
    public void taskFun(DelayMessage delayMessage) {
        System.out.println(delayMessage.toString());
    }
}
