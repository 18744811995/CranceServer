package com.hegu.tsurutani.entity.reqparam;

/**
 * 后台管理控制台请求参数类
 */
public class AdminUserReqParam {
    private String userName;
    private String status;
    private Integer page;
    private Integer limit;
    private String uId;
    private String pws;
    private String newpws;
    private String rIds;

    public String getrIds() {
        return rIds;
    }

    public void setrIds(String rIds) {
        this.rIds = rIds;
    }

    public String getNewpws() {
        return newpws;
    }

    public void setNewpws(String newpws) {
        this.newpws = newpws;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getPws() {
        return pws;
    }

    public void setPws(String pws) {
        this.pws = pws;
    }
}
