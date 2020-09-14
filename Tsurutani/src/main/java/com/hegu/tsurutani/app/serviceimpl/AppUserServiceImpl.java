package com.hegu.tsurutani.app.serviceimpl;

import com.hegu.tsurutani.app.mapper.AppUserMapper;
import com.hegu.tsurutani.app.service.AppUserServicce;
import com.hegu.tsurutani.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AppUserServiceImpl implements AppUserServicce {
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public User appUserLogin(User user) {
        return appUserMapper.appUserLogin(user);
    }

    @Override
    public User checkPhone(String phone) {
        return appUserMapper.checkPhone(phone);
    }

    @Override
    public void addUser(User user) {
        appUserMapper.addUser(user);
    }

    @Override
    public User findTimeMaxUser() {
        return appUserMapper.findTimeMaxUser();
    }

    @Override
    public Map<String, Object> getUserByUIdAndPassword(Map<String, Object> params) {
        return appUserMapper.getUserByUIdAndPassword(params);
    }

    @Override
    public void updatePassWordByUId(Map<String, Object> params) {
        appUserMapper.updatePassWordByUId(params);
    }

    @Override
    public Map<String, Object> getUserMoneyInfo(Map<String, Object> params) {
        return appUserMapper.getUserMoneyInfo(params);
    }

    @Override
    public void addUserMoney(Map<String, Object> params) {
        appUserMapper.addUserMoney(params);
    }

    @Override
    public String getUserMoney(Map<String, Object> params) {
        return appUserMapper.getUserMoney(params);
    }

    @Override
    public void updateUserMoney(Map<String, Object> params) {
        appUserMapper.updateUserMoney(params);
    }

    @Override
    public void addGroup(String uId, String gId, String gname) {
        appUserMapper.addGroup(uId,gId,gname);
    }
}
