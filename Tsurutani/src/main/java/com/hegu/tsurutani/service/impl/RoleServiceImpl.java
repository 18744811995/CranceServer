package com.hegu.tsurutani.service.impl;

import com.hegu.tsurutani.dao.RoleMapper;
import com.hegu.tsurutani.entity.Role;
import com.hegu.tsurutani.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Role> findRoleByUId(String uId) {
        return roleMapper.findRoleByUId(uId);
    }

    @Override
    public PageInfo<Role> findRolePage(String rName, Integer page, Integer limit) {
        PageInfo<Role> pageInfo=null;
        PageHelper.offsetPage((page-1)*limit,limit);
        List<Role> roles=roleMapper.findRolePage(rName);
        pageInfo=new PageInfo<Role>(roles);
        return pageInfo;
    }

    @Override
    public void add(String rName,String rId) {
        roleMapper.add(rName,rId);
    }

    @Override
    public void delete(String rId) {
        roleMapper.delete(rId);
        roleMapper.deleteRoleMenuByRId(rId);
        roleMapper.deleteUserRoleByRId(rId);
    }

    @Override
    public void update(String rId, String rName) {
        roleMapper.update(rId,rName);
    }

    @Override
    public List<String> findRoleMenu(String rId) {
        return roleMapper.findRoleMenu(rId);
    }

    @Override
    public void deleteRoleMenuByRId(String rId) {
        roleMapper.deleteRoleMenuByRId(rId);
    }

    @Override
    public void addRoleMenu(String rId, String[] rmIds) {
        roleMapper.addRoleMenu(rId,rmIds);
    }

    @Override
    public List<Map<String, Object>> findRoleAll() {
        return roleMapper.findRoleAll();
    }
}
