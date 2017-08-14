package com.shuhg.service;

import com.shuhg.queue.DelayMessage;

/**
 * Created by 大舒 on 2017/8/11.
 */
public interface ExecuteTaskService {
    void taskFun(DelayMessage delayMessage);
}
