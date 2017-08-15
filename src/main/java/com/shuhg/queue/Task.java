package com.shuhg.queue;

import com.shuhg.service.ExecuteTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送任务
 * Created by 大舒 on 2017/8/10.
 */
public class Task {
    private static Logger LOGGER = LoggerFactory.getLogger(Task.class);
    /**
     * 当值=0 ，则需要执行延时消息
     */
    private int cycleNum;
    /**
     * 消息
     */
    private DelayMessage delayMessage;
    /**
     * 预计执行时间
     */
    private Date executeDate;

    /**
     * 实际方法执行类
     */
    private ExecuteTaskService taskService;

    public Task(int cycleNum, DelayMessage delayMessage, ExecuteTaskService taskService) {
        this.cycleNum = cycleNum;
        this.delayMessage = delayMessage;
        this.taskService = taskService;
    }


    public boolean run() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            if (this.cycleNum == 0) {
                //TODO
                LOGGER.info("发现任务！--- {}",df.format(new Date()));

                taskService.taskFun(this.delayMessage);
                return true;
            } else {

                //System.out.println("开始执行：" + Main.num + "   " + df.format(new Date()));
                LOGGER.info("任务未到时间！--- {}" , df.format(new Date()));
                this.cycleNum--;
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }


    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public int getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(int cycleNum) {
        this.cycleNum = cycleNum;
    }

    public DelayMessage getDelayMessage() {
        return delayMessage;
    }

    public void setDelayMessage(DelayMessage delayMessage) {
        this.delayMessage = delayMessage;
    }
}