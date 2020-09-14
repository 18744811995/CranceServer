package com.hegu.tsurutani.entity;

public class Video {
    private String vId;
    private String vpath;
    private String uId;
    private String clickcount;
    private String playcount;
    private String status;
    private String uploadtime;
    private String title;
    private String content;
    private String videoImgPath;
    private String city;

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getVpath() {
        return vpath;
    }

    public void setVpath(String vpath) {
        this.vpath = vpath;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getClickcount() {
        return clickcount;
    }

    public void setClickcount(String clickcount) {
        this.clickcount = clickcount;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
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

    public String getVideoImgPath() {
        return videoImgPath;
    }

    public void setVideoImgPath(String videoImgPath) {
        this.videoImgPath = videoImgPath;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
