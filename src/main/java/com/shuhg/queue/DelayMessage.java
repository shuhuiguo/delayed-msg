package com.shuhg.queue;

/**
 * 消息体
 * Created by 大舒 on 2017/8/10.
 */
public class DelayMessage {
    /**
     * 消息ID
     */
    private String msgId;
    /**
     * 延时时间 单位：s,m,h
     */
    private String delayTime;
    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息类型
     */
    private String type;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "DelayMessage{" +
                "msgId='" + msgId + '\'' +
                ", delayTime='" + delayTime + '\'' +
                ", msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
