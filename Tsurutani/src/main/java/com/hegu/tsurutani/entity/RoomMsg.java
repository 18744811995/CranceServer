package com.hegu.tsurutani.entity;

/**
 * 直播间弹幕
 */
public class RoomMsg {
    private String msgId;
    private String userId;//用户登录账号
    private String roomId;//房间号
    private String uckname;//用户昵称
    private String content;//弹幕
    private String sendTime;//发送时间
    private String imgurl;//用户头像

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUckname() {

        return uckname;
    }

    public void setUckname(String uckname) {
        this.uckname = uckname;
    }
}
