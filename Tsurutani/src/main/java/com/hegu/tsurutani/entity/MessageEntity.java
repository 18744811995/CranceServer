package com.hegu.tsurutani.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author 永勇
 *
 * desc:消息实体类
 *
 * @// TODO: 2020/7/9
 */
public class MessageEntity implements Serializable, Comparable<MessageEntity> {

    /**
     * 消息類型
     */

    /**
     * 文本
     */
    public static final int MESSAGE_ENTITY_TEXT = 101;

    /**
     * 图片
     */
    public static final int MESSAGE_ENTITY_IMAGE = 102;

    /**
     * 视频
     */
    public static final int MESSAGE_ENTITY_VIDEO = 103;

    /**
     * 语音
     */
    public static final int MESSAGE_ENTITY_AUDIO = 104;

    /**
     * 位置
     */
    public static final int MESSAGE_ENTITY_LOCATION= 105;

    /**
     * 红包
     */
    public static final int MESSAGE_ENTITY_PACKAGE = 106;

    /**
     * 转账
     */
    public static final int MESSAGE_ENTITY_TRANSFER = 107;

    /**
     * 文件
     */
    public static final int MESSAGE_ENTITY_FILE = 108;

    /**
     * 收藏
     */
    public static final int MESSAGE_ENTITY_COLLECT = 109;

    /**
     * 语音通话
     */
    public static final int MESSAGE_ENTITY_AUDIO_CALL = 110;

    /**
     * 视频通话
     */
    public static final int MESSAGE_ENTITY_VIDEO_CALL = 111;

    /**
     * 正在输入
     */
    public static final int MESSAGE_ENTITY_TYPING = 112;

    /**
     * 提示大于等于时都输入提示消息，再根据具体类型设置显示
     * 提示：创建群组、邀请加入群组，退出群组，好友资料变更，群成员资料变更，添加好友，删除好友(通知对方手机更新通讯录)等消息
     */
    public static final int MESSAGE_ENTITY_TIPS = 130;

    /* ---------  --------- */

    /**
     * 消息ID
     */
    private String id = UUID.randomUUID().toString();

    /**
     * 消息发送者
     */
    private String formUser;

    /**
     * 消息类型
     */
    private int msgType = MESSAGE_ENTITY_TEXT;

    /**
     * 消息状态
     */
    //private MessageStatus status = MessageStatus.NORMAL;

    /**
     * 是否为自己发送的
     */
    private boolean self;

    /**
     * 发送时间
     */
    private long msgTime = System.currentTimeMillis() / 1000;

    /**
     * 内容
     */
    private String text;

    /**
     * 多媒体消息的保存地址
     */
    private String dataPath;

    /**
     * 多媒体消息的描述，如：[图片]
     */
    private String extra;

    /**
     * 图片、视频的宽高
     */
    private int width;
    private int height;

    /**
     * 视频、语言时长
     */
    private long duration;

    /**
     * 是否已读
     */
    private boolean isRead;

    public String getId() {
        return id;
    }

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    /*public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }*/

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    /*@Override
    public String toString() {
        return "MessageEntity{" +
                "id='" + id + '\'' +
                ", formUser='" + formUser + '\'' +
                ", msgType=" + msgType +
                ", status=" + status +
                ", self=" + self +
                ", msgTime=" + msgTime +
                ", text='" + text + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", extra='" + extra + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", duration=" + duration +
                ", isRead=" + isRead +
                '}';
    }
*/
    @Override
    public int compareTo(MessageEntity o) {
        return this.getMsgTime() > o.getMsgTime() ? - 1 : 1;
    }
}
