package com.hegu.tsurutani.app.controller;

import com.hegu.tsurutani.app.serviceimpl.AppUserServiceImpl;
import com.hegu.tsurutani.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author FYH
 * 手机端用户管理控制器
 */
@Controller
@RequestMapping("/api/user")
public class AppUserController {
    private Logger logger= LoggerFactory.getLogger(AppUserController.class);
    @Autowired
    private AppUserServiceImpl appUserServicce;
    @ResponseBody
    @RequestMapping("/test")
    public String test(){
        return "success";
    }
    /**
     * app用户登录认证
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,Object> appUserLogin(User user,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            if(null==user){
                logger.info("================用户信息为空");
                throw new Exception("没有获取到请求参数");
            }
            if(StringUtils.isEmpty(user.getLogin_type())){
                logger.info("================登录方式为空");
                throw new Exception("登录方式不能为空");
            }
            if(StringUtils.isEmpty(user.getUsername())){
                logger.info("================登录账号为空");
                throw new Exception("登录账号不能为空");
            }
            if(StringUtils.isEmpty(user.getPassword())){
                logger.info("================登录密码为空");
                throw new Exception("登录密码不能为空");
            }
            if(user.getLogin_type().equals("1")){
                //手机号+密码登录
                User paramUser=new User();
                paramUser.setUsername(user.getUsername());
                User user1=appUserServicce.appUserLogin(paramUser);
                if(null==user1){
                    throw new Exception("用户不存在！！！");
                }
                user1=appUserServicce.appUserLogin(user);
                if(null==user1){
                    throw new Exception("用户名密码不匹配！！！");
                }else{
                    resMap.put("code","success");
                    resMap.put("msg","登录成功");
                    resMap.put("data",user1);
                }
            }else if(user.getLogin_type().equals("2")){
                //手机号+验证码登录
                String server_yzm=String.valueOf(request.getSession().getAttribute(user.getUsername()));
                if(server_yzm.equals(user.getPhoneyzm())){
                    User paramUser=new User();
                    paramUser.setUsername(user.getUsername());
                    User user1=appUserServicce.appUserLogin(paramUser);
                    resMap.put("code","success");
                    resMap.put("msg","登录成功");
                    resMap.put("data",user1);
                }else{
                    throw new Exception("验证码不匹配！！！");
                }
            }
            return resMap;
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
            return resMap;
        }
    }
    /**
     * 校验用户手机号是否已经存在
     */
    @RequestMapping("/checkphone")
    @ResponseBody
    public Map<String,Object> checkphone(String phone){
        Map<String,Object> resMap=new HashMap<>();
        try {
            boolean flag=false;//不存在
            User user=appUserServicce.checkPhone(phone);
            if(user!=null){
                flag=true;
            }
            resMap.put("code","000000");
            resMap.put("msg","查询成功");
            resMap.put("data",flag);
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","000001");
            resMap.put("msg","查询异常："+e.getMessage());
            resMap.put("data","");
        }
        return resMap;
    }

    /**
     * 发送短信
     */
    @RequestMapping("/sendMsg")
    @ResponseBody
    public Map<String,Object> sendMsg(String phone,HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<>();
        try {
            if(StringUtils.isEmpty(phone)){
                throw new Exception("手机号码为空，请输入手机号码！！！");
            }
            //校验当前手机号是否存在
            boolean flag=false;//不存在
            User user=appUserServicce.checkPhone(phone);
            if(user!=null){
                flag=true;
            }
            if(flag){
                Random random=new Random();
                String phoneyzm=String.valueOf(random.nextInt(10))+String.valueOf(random.nextInt(10))+String.valueOf(random.nextInt(10))+String.valueOf(random.nextInt(10));
                request.getSession().setAttribute(phone,phoneyzm);
                resMap.put("code","000000");
                resMap.put("msg","查询成功");
                resMap.put("data",phoneyzm);
            }else{
                throw new Exception("该手机号还未注册用户");
            }
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","000001");
            resMap.put("msg","查询异常："+e.getMessage());
            resMap.put("data","");
        }
        return resMap;
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addUser(User user){
        Map<String,Object> resMap=new HashMap<>();
        try {
            checkUserInfo(user);//校验使用户信息是否填写完整
            String username="";
            User user1=appUserServicce.findTimeMaxUser();//查询最新新增的用户
            if(null==user1){
                username=String.valueOf(1000001);
            }else{
                if(user1.getUsername().equals("admin")){
                    username=String.valueOf(1000001);
                }else{
                    Integer userNameMax=Integer.valueOf(user1.getUsername());
                    userNameMax++;
                    username=String.valueOf(userNameMax);
                }
            }
            Map<String,Object> key_info=key_info();
            String uId=UUID.randomUUID().toString().replace("-","");
            user.setuId(uId);
            user.setUstatus("0");
            user.setUser_key(String.valueOf(key_info.get("user_key")));
            user.setExpire_time(String.valueOf(key_info.get("expire_time")));
            user.setUsername(username);
            user.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            appUserServicce.addUser(user);
            resMap.put("code","000000");
            resMap.put("msg","注册成功");
            resMap.put("data",user);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //创建好友列表默认分组
                    appUserServicce.addGroup(uId,UUID.randomUUID().toString().replace("-",""),"我的好友");
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","000001");
            resMap.put("msg","注册失败"+e.getMessage());
            resMap.put("data","");
        }
        return resMap;
    }
    private Map<String,Object> key_info(){
        String user_key= UUID.randomUUID().toString().replace("-","");
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 + 7);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("user_key",user_key);
        resMap.put("expire_time",dayAfter);
        return resMap;
    }
    /**
     * 校验用户信息
     * @param user
     * @throws Exception
     */
    private void checkUserInfo(User user) throws Exception {
        if(StringUtils.isEmpty(user.getPhone())){
            throw new Exception("用户手机号不能为空！！！");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            throw new Exception("密码不能为空！！！");
        }
        if(StringUtils.isEmpty(user.getUserCkName())){
            throw new Exception("用户昵称不能为空！！！");
        }
        if(StringUtils.isEmpty(user.getCreate_latitude())){
            throw new Exception("用户当前位置获取失败！！！");
        }
        if(StringUtils.isEmpty(user.getCreate_longitude())){
            throw new Exception("用户当前位置获取失败！！！");
        }
    }
    /*******************************************以下还未测试
     * 重置密码
     */
    @ResponseBody
    @RequestMapping(value = "/repassword",method = RequestMethod.GET)
    public Map<String,Object> repassword(String uId,String password,String newpassword){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("uId",uId);
            params.put("password",password);
            params.put("newpassword",newpassword);
            //查询旧密码是否匹配
            Map<String,Object> userMap=appUserServicce.getUserByUIdAndPassword(params);
            if(null==userMap||userMap.size()<=0){
                    throw new Exception("旧密码不匹配");
            }
            //修改密码
            appUserServicce.updatePassWordByUId(params);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 用户充值
     */
    @ResponseBody
    @RequestMapping(value = "/addmoney",method = RequestMethod.GET)
    public Map<String,Object> addmoney(String uId,String userkey,String money){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("uId",uId);
            params.put("money",money);
            params.put("umId",UUID.randomUUID().toString().replace("-",""));
            params.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //调用第三方接口扣除用户资产

            //查询用户当前资产
            Double updatemoney=0.0;
            Map<String,Object> map=appUserServicce.getUserMoneyInfo(params);
            if(null==map||map.size()<=0){
                //不存在新增
                updatemoney=Double.valueOf(money);
                params.put("money",updatemoney);
                appUserServicce.addUserMoney(params);
            }else{
                //已存在，修改
                String umoney=appUserServicce.getUserMoney(params);
                //更新用户资产
                updatemoney=Double.valueOf(umoney)+Double.valueOf(money);
                params.put("updatemoney",updatemoney);
                appUserServicce.updateUserMoney(params);
            }
            resMap.put("code","success");
            resMap.put("msg","操作成功");
            resMap.put("data",updatemoney);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败");
            resMap.put("data",0);
        }
        return resMap;
    }
    /**
     * 查询用户资产
     */
    @ResponseBody
    @RequestMapping(value = "/getUserMoney",method = RequestMethod.GET)
    public Double getUserMoney(String uId){
        Map<String,Object> params=new HashMap<>();
        params.put("uId",uId);
        String umoney=appUserServicce.getUserMoney(params);
        return Double.valueOf(umoney);
    }


}
