package com.shuhg.delayed.queue;

import com.shuhg.delayed.service.ExecuteTaskProcessor;
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
    private ExecuteTaskProcessor taskService;
    public Task(){}
    public Task(int cycleNum, DelayMessage delayMessage, ExecuteTaskProcessor taskService) {
        this.cycleNum = cycleNum;
        this.delayMessage = delayMessage;
        this.taskService = taskService;
    }

    /**
     * 执行任务
     * @return
     */
    public boolean run() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        try {
            if (this.cycleNum == 0) {
                Date now = new Date();
                LOGGER.info("发现任务！--- {}", df.format(now));
                taskService.taskFun(this.delayMessage);
                //当前时间和task时间预期执行时间相差5S，不执行任务
                if ((now.getTime()-this.executeDate.getTime() ) > 5000) {
                    //TODO 暂时取消 实际不判断时间有效期
                    //taskService.taskFun(this.delayMessage);
                    LOGGER.warn("当前时间--{}与任务预期执行时间--{}相差大于5S",df.format(now),df.format(this.executeDate.getTime()));
                }
                return true;
            } else {

                //System.out.println("开始执行：" + Main.num + "   " + df.format(new Date()));
                LOGGER.info("任务未到时间！--- {}", df.format(new Date()));
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

    public void setTaskService(ExecuteTaskProcessor taskService) {
        this.taskService = taskService;
    }
}
