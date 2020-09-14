package com.hegu.tsurutani.service.impl;

import com.hegu.tsurutani.dao.MenuMapper;
import com.hegu.tsurutani.entity.Admin_Menu;
import com.hegu.tsurutani.service.MenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Admin_Menu> findMenuByUId(int uId) {
        return menuMapper.findMenuByUId(uId);
    }

    @Override
    public List<Map<String, Object>> findMenuMapByUId(String uId,String id) {
        return menuMapper.findMenuMapByUId(uId,id);
    }

    @Override
    public PageInfo<Admin_Menu> findMenuPage(String mName, Integer page, Integer limit) {
        PageInfo<Admin_Menu> pageInfo=null;
        PageHelper.offsetPage((page-1)*limit,limit);
        List<Admin_Menu> menus=menuMapper.findMenuPage(mName);
        pageInfo=new PageInfo<Admin_Menu> (menus);
        return pageInfo;
    }

    @Override
    public List<Map<String,Object>> findMenuAll() {
        List<Map<String,Object>> menus=menuMapper.findMenuAll();
        return menus;
    }

    @Override
    public List<Map<String, Object>> findDtreeMenuAll() {
        return menuMapper.findDtreeMenuAll();
    }

    @Override
    public void addParentMenu(Admin_Menu menu) {
        menuMapper.addParentMenu(menu);
    }

    @Override
    public void deleteParentByMId(String mId) {
        menuMapper.deleteParentByMId(mId);
    }

    @Override
    public void deleteChilendByPId(String mId) {
        menuMapper.deleteChilendByPId(mId);
        menuMapper.deleteRoleMenu(mId);
    }

    @Override
    public void updateMenu(String mId, String mName) {
        menuMapper.updateMenu(mId,mName);
    }

    @Override
    public void addChilderMenu(Admin_Menu menu) {
        menuMapper.addChilderMenu(menu);
    }
}
