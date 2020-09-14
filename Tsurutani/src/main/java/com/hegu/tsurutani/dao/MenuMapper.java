package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.Admin_Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MenuMapper {

    List<Admin_Menu> findMenuByUId(@Param("uId") int uId);

    List<Map<String,Object>> findMenuMapByUId(@Param("uId") String uId,@Param("id") String id);

    List<Admin_Menu> findMenuPage(@Param("mName") String mName);

    List<Map<String,Object>> findMenuAll();

    List<Map<String,Object>> findDtreeMenuAll();

    void addParentMenu(@Param("menu") Admin_Menu menu);

    void deleteParentByMId(@Param("mId") String mId);

    void deleteChilendByPId(@Param("mId") String mId);

    void deleteRoleMenu(@Param("mId") String mId);

    void updateMenu(@Param("mId") String mId,@Param("mName") String mName);

    void addChilderMenu(@Param("menu") Admin_Menu menu);
}
