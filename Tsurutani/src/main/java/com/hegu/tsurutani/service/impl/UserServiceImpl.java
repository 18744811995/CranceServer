package com.hegu.tsurutani.service.impl;

import com.hegu.tsurutani.dao.UserMapper;
import com.hegu.tsurutani.entity.Admin_User;
import com.hegu.tsurutani.entity.User;
import com.hegu.tsurutani.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usersMapper;
   @Override
    public void addUser(Admin_User users) {
        usersMapper.addUseer(users);
    }

    @Override
    public Admin_User findUsersByUsernoAndPws(Admin_User users) {
        return usersMapper.findUsersByUsernoAndPws(users);
    }

    @Override
    public PageInfo<Admin_User> findUserAll(String userName, String status, Integer page, Integer limit) {
        PageInfo<Admin_User> pageInfo=null;
        PageHelper.offsetPage((page-1)*limit,limit);
        List<Admin_User> users=usersMapper.findUserAll(userName,status);
        pageInfo=new PageInfo<Admin_User>(users);
        return pageInfo;
    }

    @Override
    public Admin_User findUserByUIdAndPws(String uId, String pws) {
        return usersMapper.findUserByUIdAndPws(uId,pws);
    }

    @Override
    public void updateUserPws(String uId, String newpws) {
        usersMapper.updateUserPws(uId,newpws);
    }

    @Override
    public void updateUserStutas(String uId, String status) {
        usersMapper.updateUserStutas(uId,status);
    }

    @Override
    public List<String> findUserRoleByUId(String uId) {
        return usersMapper.findUserRoleByUId(uId);
    }

    @Override
    public void updateUserRole(String uId, String[] ridArray) {
        usersMapper.deleteUserRoleByUId(uId);
        usersMapper.updateUserRole(uId,ridArray);
    }

    @Override
    public Admin_User getUserByPhone(String phone) {
        return usersMapper.getUserByPhone(phone);
    }
}
