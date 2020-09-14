package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RoleMapper {

    List<Role> findRoleByUId(@Param("uId") String uId);

    List<Role> findRolePage(@Param("rName") String rName);

    void add(@Param("rName") String rName,@Param("rId")String rId);

    void delete(@Param("rId") String rId);

    void update(@Param("rId") String rId,@Param("rName") String rName);

    List<String> findRoleMenu(@Param("rId") String rId);

    void deleteRoleMenuByRId(@Param("rId") String rId);

    void addRoleMenu(@Param("rId") String rId,@Param("rmIds") String[] rmIds);

    List<Map<String,Object>> findRoleAll();

    void deleteUserRoleByRId(@Param("rId") String rId);
}
