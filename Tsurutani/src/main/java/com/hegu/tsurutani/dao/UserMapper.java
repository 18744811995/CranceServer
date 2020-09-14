package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.Admin_User;
import com.hegu.tsurutani.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    void addUseer(@Param("users") Admin_User users);

    Admin_User findUsersByUsernoAndPws(Admin_User users);

    List<Admin_User> findUserAll(@Param("userName") String userName, @Param("status") String status);

    Admin_User findUserByUIdAndPws(@Param("uId") String uId, @Param("pws") String pws);

    void updateUserPws(@Param("uId") String uId,@Param("newpws") String newpws);

    void updateUserStutas(@Param("uId") String uId,@Param("status") String status);

    List<String> findUserRoleByUId(@Param("uId") String uId);

    void updateUserRole(@Param("uId") String uId,@Param("rIds") String[] ridArray);

    void deleteUserRoleByUId(@Param("uId") String uId);

    Admin_User getUserByPhone(@Param("phone") String phone);
}
