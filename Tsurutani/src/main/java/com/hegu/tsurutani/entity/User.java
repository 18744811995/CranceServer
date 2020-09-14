package com.hegu.tsurutani.entity;

/**
 * @author FYH
 * @Date 2020/03/27
 * 系统用户类
 */
public class User {
    private String uId; //主键自增长
    private String username;//登录账号
    private String carId;//身份证号
    private String password; //登录密码
    private String userCkName;//昵称
    private String ustatus;//用户状态默认0正常
    private String phone;//用户手机号

    private String uType;//用户类型
    private String sex;//用户性别
    private String create_longitude;//用户注册所在地维度
    private String create_latitude;//用户注册所在地经度
    private String update_longitude;
    private String update_latitude;
    private String uHerder_Img_herder;
    private String user_key;
    private String expire_time;//秘钥到期日期
    private String create_time;
    private String login_type;
    private String city;
    private String phoneyzm;

    public String getPhoneyzm() {
        return phoneyzm;
    }

    public void setPhoneyzm(String phoneyzm) {
        this.phoneyzm = phoneyzm;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserCkName() {
        return userCkName;
    }

    public void setUserCkName(String userCkName) {
        this.userCkName = userCkName;
    }

    public String getUstatus() {
        return ustatus;
    }

    public void setUstatus(String ustatus) {
        this.ustatus = ustatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreate_longitude() {
        return create_longitude;
    }

    public void setCreate_longitude(String create_longitude) {
        this.create_longitude = create_longitude;
    }

    public String getCreate_latitude() {
        return create_latitude;
    }

    public void setCreate_latitude(String create_latitude) {
        this.create_latitude = create_latitude;
    }

    public String getUpdate_longitude() {
        return update_longitude;
    }

    public void setUpdate_longitude(String update_longitude) {
        this.update_longitude = update_longitude;
    }

    public String getUpdate_latitude() {
        return update_latitude;
    }

    public void setUpdate_latitude(String update_latitude) {
        this.update_latitude = update_latitude;
    }

    public String getuHerder_Img_herder() {
        return uHerder_Img_herder;
    }

    public void setuHerder_Img_herder(String uHerder_Img_herder) {
        this.uHerder_Img_herder = uHerder_Img_herder;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
