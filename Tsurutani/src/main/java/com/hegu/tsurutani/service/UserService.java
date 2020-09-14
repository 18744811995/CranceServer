package com.hegu.tsurutani.service;

import com.hegu.tsurutani.entity.Admin_User;
import com.hegu.tsurutani.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    public void addUser(Admin_User users);

    Admin_User findUsersByUsernoAndPws(Admin_User users);

    PageInfo<Admin_User> findUserAll(String userName, String status, Integer page, Integer limit);

    Admin_User findUserByUIdAndPws(String uId, String pws);

    void updateUserPws(String uId, String newpws);

    void updateUserStutas(String uId, String status);

    List<String> findUserRoleByUId(String rId);

    void updateUserRole(String uId, String[] ridArray);

    Admin_User getUserByPhone(String phone);
}
