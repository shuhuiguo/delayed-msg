package com.shuhg.queue;

import com.alibaba.fastjson.JSONObject;
import com.shuhg.service.ExecuteTaskService;
import com.shuhg.utils.RedisUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 环型任务节点队列
 * 共 3600个节点，每次定时任务向前移动一个节点
 * Created by 大舒 on 2017/8/10.
 */
public class DelayQueue {
    private static String REDIS_QUEUE_KEY="delay_queue_msg";
    /**
     * 任务环
     */
    private static List<TaskNode> taskNodes = new ArrayList<>();
    //private static
    private static AtomicInteger currentIndex = new AtomicInteger(0);

    /**
     * 初始对象锁
     */
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 获取任务环
     *
     * @return
     */
    public List<TaskNode> getQueues() {
        if (taskNodes.size() < 3600) {
            lock.lock();
            if (taskNodes.size() < 3600) {
                for (int i = taskNodes.size(); i < 3600; i++) {
                    TaskNode taskNode = new TaskNode();
                    taskNode.setNodeIndex(i);
                    this.taskNodes.add(taskNode);
                }
            }
            lock.unlock();
        }
        return this.taskNodes;
    }

    /**
     * 添加延时消息到任务节点
     * 环型任务队列，共3600 个节点，每一秒移动一个节点
     * 延时时间超过3600S时，circle (循环数)数值增加3600倍数
     * 对3600 求余 ，把任务添加到当前运行节点值+余数的节点中
     *
     * @param delayMessage
     * @param taskService
     * @return
     */
    public List<TaskNode> addMessage(DelayMessage delayMessage, ExecuteTaskService taskService) {
        String time = delayMessage.getDelayTime();
        Calendar calendar = Calendar.getInstance();
        if (time != null && !time.trim().equals("")) {
            String[] delayTimes = time.split(",");
            for (String delayTime : delayTimes) {
                delayTime = delayTime.toUpperCase();
                calendar.setTime(new Date());
                int circle = 0;
                int nodeIndex = 0;
                Task task = null;
                //小时
                if (delayTime.indexOf("H") > -1) {
                    String hStr = delayTime.substring(0, delayTime.indexOf("H"));
                    try{
                        double h = Double.parseDouble(hStr);
                        circle = (int) (h / 1);
                        nodeIndex = (int) (((h % 1)) * 60 * 60);
                    }catch (Exception ex){
                        throw new RuntimeException("时间格式错误");
                    }

                    //分钟
                } else if (delayTime.indexOf("M") > -1) {
                    String mStr = delayTime.substring(0, delayTime.indexOf("M"));
                    try{
                        double m = Double.parseDouble(mStr);
                        circle = (int) (m / 60);
                        nodeIndex = (int) (m * 3600);
                    }catch (Exception ex){
                        throw new RuntimeException("时间格式错误");
                    }

                    //秒
                } else {
                    String sStr = delayTime.indexOf("S") > -1 ? delayTime.substring(0, delayTime.indexOf("S")) : delayTime;
                    try {
                        double s = Double.parseDouble(sStr);
                        //一个循环3600s = 1小时
                        circle = (int) (s / 3600);
                        //第多少s执行
                        nodeIndex = (int) (s % 3600);
                    } catch (Exception ex) {
                        throw new RuntimeException("时间格式错误");
                    }

                }
                if (circle > 0) {
                    calendar.add(Calendar.HOUR_OF_DAY, circle);
                }
                if (nodeIndex > 0) {
                    calendar.add(Calendar.SECOND, nodeIndex);
                }
                task = new Task(circle, delayMessage, taskService);
                task.setExecuteDate(calendar.getTime());
                if (nodeIndex != 0) {
                    nodeIndex--;
                }
                this.getQueues().get(nodeIndex).getTasks().add(task);
                //落地数据
                RedisUtil.getInstance().zadd(REDIS_QUEUE_KEY,nodeIndex, JSONObject.toJSONString(this.getQueues().get(nodeIndex)));
            }
        }
        return this.taskNodes;
    }

    /**
     * 移动环型节点，执行节点中的任务
     */
    public void run() {
        if (this.currentIndex.get() >= 3600) {
            this.currentIndex.getAndSet(0);
        }
        TaskNode taskNode = this.getQueues().get(this.currentIndex.get());
        if (taskNode.getTasks().size() > 0) {
            for (Task task : taskNode.getTasks()) {
                boolean rs = task.run();
                if (rs) {
                    taskNode.getTasks().remove(task);
                }
            }
            //落地数据
            RedisUtil.getInstance().removeZsetByScore(REDIS_QUEUE_KEY,taskNode.getNodeIndex());
            RedisUtil.getInstance().zadd(REDIS_QUEUE_KEY,taskNode.getNodeIndex(), JSONObject.toJSONString(taskNode));
        }
        this.currentIndex.addAndGet(1);
    }
}
