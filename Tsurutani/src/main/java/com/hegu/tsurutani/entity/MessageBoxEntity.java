package com.hegu.tsurutani.entity;

import java.io.Serializable;

/**
 * @author 永勇
 *
 * desc:消息传输包装类
 *
 * @// TODO: 2020/7/9
 */
public class MessageBoxEntity implements Serializable {

    /**
     * 收信人ID isGroup为true时为群组ID
     */
    private String toUser;

    /**
     * 发送者信息
     */
    private String formUser;
    private String formName;
    private String formIcon;

    /**
     * true为群组 false为单聊
     */
    private boolean isGroup;

    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 消息的实体类
     */
    private MessageEntity message;

    /**
     * 发送时间戳
     */
    private long msgTime = System.currentTimeMillis() / 1000;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormIcon() {
        return formIcon;
    }

    public void setFormIcon(String formIcon) {
        this.formIcon = formIcon;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    @Override
    public String toString() {
        return "MessageBoxEntity{" +
                "toUser='" + toUser + '\'' +
                ", formUser='" + formUser + '\'' +
                ", formName='" + formName + '\'' +
                ", formIcon='" + formIcon + '\'' +
                ", isGroup=" + isGroup +
                ", msgType=" + msgType +
                ", message=" + message +
                ", msgTime=" + msgTime +
                '}';
    }
}
