package com.shuhg.delayed.queue;

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
    private Object msg;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 通知路径
     */
    private String url;

    /**
     * 通知端Key
     */
    private String appKey;

    /**
     * 通知端secret
     */
    private String appSecret;

    /**
     * 通知端method,作用类似restfull : /user/update
     */
    private String method;

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

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "DelayMessage{" +
                "msgId='" + msgId + '\'' +
                ", delayTime='" + delayTime + '\'' +
                ", msg=" + msg +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", method='"+method+ '\'' +
                '}';
    }
}
