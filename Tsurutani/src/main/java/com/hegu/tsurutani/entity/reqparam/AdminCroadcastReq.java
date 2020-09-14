package com.hegu.tsurutani.entity.reqparam;

public class AdminCroadcastReq {
    private String aId;
    private String aStatus;

    private String roomId;
    private String title;
    private String status;
    private String userCkName;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getaStatus() {
        return aStatus;
    }

    public void setaStatus(String aStatus) {
        this.aStatus = aStatus;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCkName() {
        return userCkName;
    }

    public void setUserCkName(String userCkName) {
        this.userCkName = userCkName;
    }
}
