package com.hegu.tsurutani.entity;


public class UserParentMessage {
    //private String id;
    private String formIcon;
    private String formName;
    private String formUser;
    private boolean isGroup;
    private long msgTime;
    private Integer msgType;
    private String toUser;
    //private String dataPath;
    private String ConversationId;//聊天列表Id
    private UserChilderMessage message;
    public String getFormIcon() {
        return formIcon;
    }

    public void setFormIcon(String formIcon) {
        this.formIcon = formIcon;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }


    public boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }


    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public UserChilderMessage getMessage() {
        return message;
    }

    public void setMessage(UserChilderMessage message) {
        this.message = message;
    }

    public String getConversationId() {
        return ConversationId;
    }

    public void setConversationId(String conversationId) {
        ConversationId = conversationId;
    }

    /*public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }*/
    /* public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/
}
