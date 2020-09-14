package com.hegu.tsurutani.controller;

import com.hegu.tsurutani.entity.Admin_User;
import com.hegu.tsurutani.entity.User;
import com.hegu.tsurutani.entity.Admin_Menu;
import com.hegu.tsurutani.service.MenuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/menu/menuGrid")
    public ModelAndView goMenuGrid(ModelAndView view){
        view.setViewName("menu/menuGrid");
        return view;
    }
    @RequestMapping("/menu/findMenuPage")
    @ResponseBody
    public Map<String,Object> findMenuPage(String mName,Integer page,Integer limit){
        Map<String,Object> resultMap=new HashMap<>();
        PageInfo<Admin_Menu> users=menuService.findMenuPage(mName,page,limit);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",users.getTotal());
        resultMap.put("data",users.getList());
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/menu/findMenuAll")
    public List<Map<String,Object>> findMenuAll(){
        List<Map<String,Object>> menus=menuService.findMenuAll();
        return menus;
    }
    @ResponseBody
    @RequestMapping("/menu/findDtreeMenuAll")
    public Map<String,Object> findDtreeMenuAll(){
        Map<String,Object> status=new HashMap<>();
        status.put("code","200");
        status.put("message","操作成功");
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("status",status);
        List<Map<String,Object>> maps=menuService.findDtreeMenuAll();
        resMap.put("data",maps);
        return resMap;
    }

    @RequestMapping("/menu/getMenuNavbar")
    @ResponseBody
    public List<Map<String,Object>> getMenuNavbar(String id,HttpServletRequest request){
        Admin_User users= (Admin_User) request.getSession().getAttribute("userInfo");
        List<Map<String,Object>> menus=null;
        if(users!=null){
            if (id==null){
                id="0";
            }
            menus=menuService.findMenuMapByUId(users.getuId(),id);
        }
        return menus;
    }
    @RequestMapping("/menu/goAddParentMenu")
    public ModelAndView goAddParentMenu(ModelAndView view){
            view.setViewName("menu/addParentMenu");
            return view;
    }
    @RequestMapping("/menu/addParentMenu")
    @ResponseBody
    public Map<String,Object> addParentMenu(String mName,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Admin_Menu menu=new Admin_Menu();
            menu.setmName(mName);
            menu.setMurl("");
            menu.setpId("0");
            menu.setmId(UUID.randomUUID().toString().replace("-",""));
            menuService.addParentMenu(menu);
            resMap.put("code","success");
            resMap.put("msg","添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","添加失败"+e.getMessage());
        } finally {
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    Admin_User user=(Admin_User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @RequestMapping("/menu/delete")
    @ResponseBody
    public Map<String,Object> delete(String mId,String pId,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            menuService.deleteParentByMId(mId);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","删除失败"+e.getMessage());
        } finally {
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @RequestMapping("/menu/goupdateMenu")
    public ModelAndView goUpdateMenu(ModelAndView view,String mId,String mName){
        view.addObject("mId",mId);
        view.addObject("mName",mName);
        view.setViewName("menu/updateMenu");
        return view;
    }
    @ResponseBody
    @RequestMapping("/menu/updateMenu")
    public Map<String,Object> updateMenu(String mId,String mName,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            menuService.updateMenu(mId,mName);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","修改失败"+e.getMessage());
        } finally {
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }
    @RequestMapping("/menu/goaddChildMenu")
    public ModelAndView goaddChildMenu(ModelAndView view,String mId){
        view.addObject("mId",mId);
        view.setViewName("menu/addChildMenu");
        return view;
    }
    @ResponseBody
    @RequestMapping("/menu/goAddChilderMenu")
    public Map<String,Object> goAddChilderMenu(String pId,String mName,String murl,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Admin_Menu menu=new Admin_Menu();
            menu.setmName(mName);
            menu.setMurl(murl);
            menu.setmId(UUID.randomUUID().toString().replace("-",""));
            menu.setpId(pId);
            menuService.addChilderMenu(menu);
            resMap.put("code","success");
            resMap.put("msg","新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","success");
            resMap.put("msg","新增失败"+e.getMessage());
        } finally {
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return  resMap;
    }
}
