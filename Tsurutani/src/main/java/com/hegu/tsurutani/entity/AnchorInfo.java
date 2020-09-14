package com.hegu.tsurutani.entity;

public class AnchorInfo {
    //ID
    private String aId;
    //用户Id
    private String uId;
    //主播姓名
    private String aName;
    //主播身份证号
    private String aCarId;
    //主播状态
    private String aStatus;
    private String filezmpath;
    private String filefmpath;
    private String update_time;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaCarId() {
        return aCarId;
    }

    public void setaCarId(String aCarId) {
        this.aCarId = aCarId;
    }

    public String getaStatus() {
        return aStatus;
    }

    public void setaStatus(String aStatus) {
        this.aStatus = aStatus;
    }

    public String getFilezmpath() {
        return filezmpath;
    }

    public void setFilezmpath(String filezmpath) {
        this.filezmpath = filezmpath;
    }

    public String getFilefmpath() {
        return filefmpath;
    }

    public void setFilefmpath(String filefmpath) {
        this.filefmpath = filefmpath;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
