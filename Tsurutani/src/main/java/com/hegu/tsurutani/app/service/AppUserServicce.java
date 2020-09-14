package com.hegu.tsurutani.app.service;

import com.hegu.tsurutani.entity.User;

import java.util.Map;

public interface AppUserServicce {
    public User appUserLogin(User user);

    /***
     * 查询用户手机号码是否存在
     * @param phone
     * @return
     */
    User checkPhone(String phone);

    /***
     *添加用户
     * @param user
     */
    void addUser(User user);

    User findTimeMaxUser();

    Map<String,Object> getUserByUIdAndPassword(Map<String, Object> params);

    void updatePassWordByUId(Map<String, Object> params);

    Map<String,Object> getUserMoneyInfo(Map<String, Object> params);

    void addUserMoney(Map<String, Object> params);

    String getUserMoney(Map<String, Object> params);

    void updateUserMoney(Map<String, Object> params);

    void addGroup(String uId, String gId, String gname);
}
