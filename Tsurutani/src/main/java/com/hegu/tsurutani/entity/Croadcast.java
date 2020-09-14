package com.hegu.tsurutani.entity;

/**
 * 直播间实体类
 */
public class Croadcast {
    //直播间号
    private String roomId;
    //标题
    private String title;
    //描述
    private String content;
    //视屏地址
    private String rtmPath;
    //直播状态
    private String status;
    //在线人数
    private String personCount;
    //房间主播
    private String anchorId;

    private String update_time;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRtmPath() {
        return rtmPath;
    }

    public void setRtmPath(String rtmPath) {
        this.rtmPath = rtmPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }
}
