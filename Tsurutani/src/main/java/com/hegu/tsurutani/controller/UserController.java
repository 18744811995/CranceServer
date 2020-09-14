package com.hegu.tsurutani.controller;

import com.hegu.tsurutani.entity.reqparam.AdminUserReqParam;
import com.hegu.tsurutani.entity.Admin_User;
import com.hegu.tsurutani.service.MenuService;
import com.hegu.tsurutani.service.RoleService;
import com.hegu.tsurutani.service.impl.UserServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl usersService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    private SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");//用于生成用户账号
    /**
     * 跳转到登录界面
     * @param view
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView goLogin(ModelAndView view){
        view.setViewName("login/login");
        view.addObject("msg","");
        return view;
    }
    /**
     * 跳转到注册界面
     */
    @RequestMapping("/user/goregister")
    public ModelAndView goRegister(ModelAndView view){
        view.setViewName("register");
        return view;
    }
    @RequestMapping("/user/goadduser")
    public ModelAndView goadduser(ModelAndView view){
        view.setViewName("user/adduser");
        return view;
    }
    @ResponseBody
    @RequestMapping("/user/checkPhone")
    public Map<String,Object> checkPhone(Admin_User user1){
        Map<String,Object> resMap=new HashMap<>();
        try{
            Admin_User user=usersService.getUserByPhone(user1.getPhone());
            if(null==user){
                resMap.put("code","success");
            }else{
                resMap.put("code","error");
            }
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","error");
        }
        return resMap;
    }
    /**
     * 注册用户
     * @param request
     * @param response
     * @param users
     * @return
     */
    @ResponseBody
    @RequestMapping("/user/adduser")
    public Map<String,Object> registerUser(HttpServletRequest request, HttpServletResponse response
            ,Admin_User users){
        Map<String,Object> resMap=new HashMap<>();
        users.setuId(UUID.randomUUID().toString().replace("-",""));
        users.setStatus("0");
        users.setUsername(users.getPhone());
        users.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            usersService.addUser(users);
            resMap.put("code","success");
            resMap.put("data",users);
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","000");
        }
        return resMap;
    }
    /**
     * 用户认证
     */
    @RequestMapping("/user/login")
    public ModelAndView userLogin(ModelAndView view, Admin_User users, HttpServletRequest request){
        try {
            System.out.println("================"+users.getUsername()+";"+users.getPassword());
            Admin_User user=usersService.findUsersByUsernoAndPws(users);
            if (user==null){
                view.addObject("code","403");
                view.addObject("msg","用户名密码不匹配！");
                view.setViewName("login/login");
            }else {
                if(!user.getStatus().equals("0")){
                    view.addObject("code","403");
                    view.addObject("msg","当前用户已被冻结！");
                    view.setViewName("login/login");
                }else {
                    request.getSession().setAttribute("userInfo",user);
                    view.addObject("user", user);
                    view.setViewName("index");
                    //List<Menu> menus=jylMenuService.findMenuByUId(user.getuId());
                    //List<JylRole> roles=jylRoleService.findRoleByUId(user.getuId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            view.addObject("code","309");
            view.addObject("msg",e.getMessage());
            view.setViewName("login/login");
        }

        return view;
    }
    @RequestMapping("/user/exit")
    public ModelAndView userExit(ModelAndView view){
        view.setViewName("login/login");
        return view;
    }
    /**
     * 跳转到用户管理界面
     */
    @RequestMapping("/user/userGrid")
    public  ModelAndView goUserGrid(ModelAndView view){
        view.setViewName("user/userGrid");
        return view;
    }
    /**
     * 查询用户列表
     */
    @RequestMapping("/user/findUserAll")
    @ResponseBody
    public Map<String,Object> findUserAll(AdminUserReqParam userParam){
        Map<String,Object> jsonMap=new HashMap<>();
        PageInfo<Admin_User> users=usersService.findUserAll(userParam.getUserName(),userParam.getStatus(),userParam.getPage(),userParam.getLimit());
        jsonMap.put("code",0);
        jsonMap.put("msg","");
        jsonMap.put("count",users.getTotal());
        jsonMap.put("data",users.getList());
        return jsonMap;
    }
    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user/goUpdatePws")
    public ModelAndView goUpdatePws(ModelAndView view,AdminUserReqParam userParam){
        view.addObject("uId",userParam.getuId());
        view.setViewName("user/updatePws");
        return view;
    }
    /**
     * 确认旧密码
     */
    @RequestMapping("/user/userPwsRz")
    @ResponseBody
    public boolean userPwsRz(AdminUserReqParam userParam){
        try {
            Admin_User users=usersService.findUserByUIdAndPws(userParam.getuId(),userParam.getPws());
            if (users==null){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 修改用户密码
     */
    @RequestMapping("/user/updateUserPws")
    @ResponseBody
    public Map<String,Object> updateUserPws(AdminUserReqParam userParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            usersService.updateUserPws(userParam.getuId(),userParam.getNewpws());
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","303");
            resMap.put("msg",e.getMessage());
        }
        return resMap;
    }
    /**
     * 修改用户状态
     */
    @RequestMapping("/user/updateStatus")
    @ResponseBody
    public Map<String,Object> updateStatus(AdminUserReqParam userParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            usersService.updateUserStutas(userParam.getuId(),userParam.getStatus());
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","303");
            resMap.put("msg",e.getMessage());
        }
        return resMap;
    }
    /**
     * 跳转到分配角色界面
     */
    @RequestMapping("/user/gouserrole")
    public ModelAndView goUserRole(AdminUserReqParam userParam, ModelAndView  view){
        view.addObject("uId",userParam.getuId());
        view.setViewName("user/userrole");
        return view;
    }
    /**
     * 查询用户所拥有的角色
     */
    @ResponseBody
    @RequestMapping("/user/findUserRoleByUId")
    public List<String> findUserRoleByUId(AdminUserReqParam userParam){
        return usersService.findUserRoleByUId(userParam.getuId());
    }
    /**
     * 修改用户角色
     */
    @ResponseBody
    @RequestMapping("/user/updateUserRole")
    public Map<String,Object> updateUserRole(AdminUserReqParam userParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            String [] ridArray=userParam.getrIds().split(",");
            usersService.updateUserRole(userParam.getuId(),ridArray);
            resMap.put("code","success");
            resMap.put("msg","授权成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","501");
            resMap.put("msg","授权失败"+e.getMessage());
        }finally {
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    User user=(User)request.getSession().getAttribute("userInfo");
                }
            }).start();*/
        }
        return resMap;
    }

}
