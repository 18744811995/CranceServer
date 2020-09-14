package com.hegu.tsurutani.controller;

import com.hegu.tsurutani.entity.Role;
import com.hegu.tsurutani.entity.User;
import com.hegu.tsurutani.service.RoleService;
import com.hegu.tsurutani.service.VideoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 跳转到角色管理界面
     */
    @RequestMapping("/role/roleGrid")
    public ModelAndView goRoleGrid(ModelAndView view){
        view.setViewName("role/roleGrid");
        return view;
    }
    /**
     * 查询当前页数据
     */
    @RequestMapping("/role/findRolePage")
    @ResponseBody
    public Map<String,Object> findRolePage(String rName,Integer page,Integer limit){
        Map<String,Object> resultMap=new HashMap<>();
        PageInfo<Role> users=roleService.findRolePage(rName,page,limit);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",users.getTotal());
        resultMap.put("data",users.getList());
        return resultMap;
    }
    /**
     * 跳转到添加角色界面
     */
    @RequestMapping("/role/goaddrole")
    public ModelAndView goAddRole(ModelAndView view){
        view.setViewName("role/addRole");
        return view;
    }
    /**
     * 新增角色
     */
    @RequestMapping("/role/addRole")
    @ResponseBody
    public Map<String,Object> addRole(String rName, HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            String rId= UUID.randomUUID().toString().replace("-","");
            roleService.add(rName,rId);
            resMap.put("code","success");
            resMap.put("msg","添加成功！");
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","添加失败"+e.getMessage());
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @ResponseBody
    @RequestMapping("/role/deleteRole")
    public Map<String,Object> deleteRole(String rId,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            roleService.delete(rId);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","删除失败"+e.getMessage());
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @RequestMapping("/role/goupdaterole")
    public  ModelAndView goUpdateRole(String rId,String rName,ModelAndView view){
        view.setViewName("role/updateRole");
        view.addObject("rId",rId);
        view.addObject("rName",rName);
        return view;
    }
    @ResponseBody
    @RequestMapping("/role/updateRole")
    public Map<String, Object> updateRole(String rId,String rName,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            roleService.update(rId,rName);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","修改失败");
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @RequestMapping("/role/gorolemenu")
    public  ModelAndView goroleMenu(String rId,ModelAndView view){
        List<String> rmIds=roleService.findRoleMenu(rId);
        String rmIdStr="";
        for(int i=0;i<rmIds.size();i++){
            if(i==rmIds.size()-1){
                rmIdStr+=rmIds.get(i);
            }else{
                rmIdStr+=rmIds.get(i)+",";
            }
        }
        view.addObject("rmIds",rmIdStr);
        view.addObject("rId",rId);
        view.setViewName("role/rolemenu");
        return view;
    }
    @ResponseBody
    @RequestMapping("/role/updateRmId")
    public Map<String,Object> updateRmId(String rId,String rmIds,String uprmIds,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            roleService.deleteRoleMenuByRId(rId);
            String[] rmIdsup =uprmIds.split(",");
            roleService.addRoleMenu(rId,rmIdsup);
            resMap.put("code","success");
            resMap.put("msg","角色授权成功");
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","角色授权失败"+e.getMessage());
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @ResponseBody
    @RequestMapping("/role/findRoleAll")
    public List<Map<String,Object>> findRoleAll(){
        return roleService.findRoleAll();
    }
}
