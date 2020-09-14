package com.hegu.tsurutani.service;

import com.hegu.tsurutani.entity.Admin_Menu;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<Admin_Menu> findMenuByUId(int uId);

    List<Map<String,Object>> findMenuMapByUId(String uId,String id);

    PageInfo<Admin_Menu> findMenuPage(String mName, Integer page, Integer limit);

    List<Map<String,Object>> findMenuAll();

    List<Map<String,Object>> findDtreeMenuAll();

    void addParentMenu(Admin_Menu menu);

    void deleteParentByMId(String mId);

    void deleteChilendByPId(String mId);

    void updateMenu(String mId, String mName);

    void addChilderMenu(Admin_Menu menu);
}
