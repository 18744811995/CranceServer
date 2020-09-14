package com.hegu.tsurutani.entity;

public class UserMsg {
    private String cIId;
    private String sendType;//1发给好友的消息，2群消息
    private String sendUId;//发送消息用户ID
    private String senduImg;//发送消息用户头像地址
    private String sendUckName;//发送消息用户昵称
    private String sendMsg;//发送消息
    private String msgType;//消息类型1文字2语音3图片4视频5
    private String msgUId;//接收用户Id
    private String msguImg;//接收用户头像地址
    private String msguCkName;//接受用户昵称
    private String cretime;//创建时间
    private String mcId;//群id
    private String status;
    private String cmId;

    public String getCmId() {
        return cmId;
    }

    public void setCmId(String cmId) {
        this.cmId = cmId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendUId() {
        return sendUId;
    }

    public void setSendUId(String sendUId) {
        this.sendUId = sendUId;
    }

    public String getSenduImg() {
        return senduImg;
    }

    public void setSenduImg(String senduImg) {
        this.senduImg = senduImg;
    }

    public String getSendUckName() {
        return sendUckName;
    }

    public void setSendUckName(String sendUckName) {
        this.sendUckName = sendUckName;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgUId() {
        return msgUId;
    }

    public void setMsgUId(String msgUId) {
        this.msgUId = msgUId;
    }

    public String getMsguImg() {
        return msguImg;
    }

    public void setMsguImg(String msguImg) {
        this.msguImg = msguImg;
    }

    public String getMsguCkName() {
        return msguCkName;
    }

    public void setMsguCkName(String msguCkName) {
        this.msguCkName = msguCkName;
    }

    public String getCretime() {
        return cretime;
    }

    public void setCretime(String cretime) {
        this.cretime = cretime;
    }

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getcIId() {
        return cIId;
    }

    public void setcIId(String cIId) {
        this.cIId = cIId;
    }
}
