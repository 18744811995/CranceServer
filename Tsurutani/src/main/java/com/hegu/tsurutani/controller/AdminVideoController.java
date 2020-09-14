package com.hegu.tsurutani.controller;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.reqparam.AdminVideoReq;
import com.hegu.tsurutani.service.AdminVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台视频管理
 */
@Controller
@RequestMapping("/video")
public class AdminVideoController {
    private Logger logger= LoggerFactory.getLogger(AdminVideoController.class);
    @Autowired
    private AdminVideoService adminVideoService;
    @Value("${server.file.prefix}")
    private String fileSuffix;
    /***
     * 跳转到待审核列表界面
     */
    @RequestMapping("/applyGrid")
    public ModelAndView goApplyGrid(ModelAndView view){
        view.setViewName("video/applyGrid");
        return view;
    }
    /**
     * 查询当前页待审核列表数据
     */
    @ResponseBody
    @RequestMapping("/applyListPage")
    public Map<String,Object> applyListPage(Integer page, Integer limit, AdminVideoReq videoReq){
        Map<String,Object> jsonMap=new HashMap<>();
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }
        PageInfo<Map<String,Object>> pageInfo=adminVideoService.applyListPage(page,limit,videoReq);
        jsonMap.put("code",0);
        jsonMap.put("msg","");
        jsonMap.put("count",pageInfo.getTotal());
        jsonMap.put("data",pageInfo.getList());
        return jsonMap;
    }
    /**
     * 跳转到查询封面界面
     */
    @RequestMapping("/goshowImg")
    public ModelAndView goshowImg(String vId,ModelAndView view){
        view.addObject("vId",vId);
        view.setViewName("video/showImg");
        return view;
    }
    /**
     * 跳转到查询视频界面
     */
    @RequestMapping("/goshowvideo")
    public ModelAndView goshowvideo(String vId,ModelAndView view){
        Map<String,Object> resMap=adminVideoService.getVideoInfoByvId(vId);
        view.addObject("vId",vId);
        view.addObject("src",resMap.get("vpath"));
        view.setViewName("video/showvideo");
        return view;
    }
    /**
     * 根据vId查询VideoInfo
     */
    @ResponseBody
    @RequestMapping(value = "/getVideoInfoByvId",method = RequestMethod.GET)
    public Map<String,Object> getVideoInfoByvId(String vId){
        Map<String,Object> resMap=adminVideoService.getVideoInfoByvId(vId);
        return resMap;
    }
    /**
     * 视频审核
     */
    @ResponseBody
    @RequestMapping("/apply")
    public Map<String,Object> apply(AdminVideoReq videoReq){
        Map<String,Object> resMap=new HashMap<>();
        try{
            adminVideoService.apply(videoReq);
            resMap.put("code","success");
            resMap.put("msg","审批成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg",e.getMessage());
        }
        return resMap;
    }
    /**
     * 跳转到视屏列表
     */
    @ResponseBody
    @RequestMapping("/videoInfoGrid")
    public ModelAndView videoInfoGrid(ModelAndView view){
        view.setViewName("video/videoInfoGrid");
        return view;
    }
    /**
     * 删除视屏信息
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Map<String,Object> delete(AdminVideoReq videoReq){
        Map<String,Object> resMap=new HashMap<>();
        try{
            Map<String,Object> videoInfo=adminVideoService.getVideoInfoByvId(videoReq.getvId());
            String imgPath="";
            String videoPath="";
            if(!StringUtils.isEmpty(videoInfo.get("videoImgPath"))){
                imgPath=String.valueOf(videoInfo.get("videoImgPath")).replace("http://116.62.110.155/static/",fileSuffix);
                deleteFile(imgPath);
            }
            if(!StringUtils.isEmpty(videoInfo.get("vpath"))){
                videoPath=String.valueOf(videoInfo.get("vpath")).replace("http://116.62.110.155/static/",fileSuffix);
                deleteFile(videoPath);
            }
            adminVideoService.deleteByVId(videoReq);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","删除失败，error:"+e.getMessage());
        }
        return resMap;
    }
    private void deleteFile(String filePath){
        File file=new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }
}
