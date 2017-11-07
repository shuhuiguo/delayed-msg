package com.shuhg.delayed.service;

import com.shuhg.delayed.queue.DelayMessage;

/**
 * Created by 大舒 on 2017/8/11.
 */
public interface ExecuteTaskService {
    void taskFun(DelayMessage delayMessage);
}
