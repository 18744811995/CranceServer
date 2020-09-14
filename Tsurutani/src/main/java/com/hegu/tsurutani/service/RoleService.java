package com.hegu.tsurutani.service;

import com.hegu.tsurutani.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<Role> findRoleByUId(String uId);

    PageInfo<Role> findRolePage(String rName, Integer page, Integer limit);

    void add(String rName,String rId);

    void delete(String rId);

    void update(String rId, String rName);

    List<String> findRoleMenu(String rId);

    void deleteRoleMenuByRId(String rId);

    void addRoleMenu(String rId, String[] rmIds);

    List<Map<String,Object>> findRoleAll();
}
