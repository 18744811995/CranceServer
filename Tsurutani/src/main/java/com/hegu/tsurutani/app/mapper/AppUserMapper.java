package com.hegu.tsurutani.app.mapper;

import com.hegu.tsurutani.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface AppUserMapper {
    User appUserLogin(User user);

    /**
     * 查询用户手机号码是否存在
     * @param phone
     * @return
     */
    User checkPhone(String phone);

    /**
     * 新增用户信息
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

    void addGroup(@Param("uId") String uId, @Param("gId")String gId,@Param("gname") String gname);
}
