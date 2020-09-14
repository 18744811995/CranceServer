package com.hegu.tsurutani.controller;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.Croadcast;
import com.hegu.tsurutani.entity.reqparam.AdminCroadcastReq;
import com.hegu.tsurutani.service.AdminCroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 直播后台管理
 */
@Controller
@RequestMapping("/croadcast")
public class AdminCroadcastController {
    @Autowired
    private AdminCroadcastService croadcastService;
    @Value("${server.file.prefix}")
    private String fileSuffix;
    /**
     * 跳转到主播资质列表界面
     */
    @RequestMapping(value = "/anchorInfo", method = RequestMethod.GET)
    public ModelAndView goapplyanchorGrid(ModelAndView view) {
        view.setViewName("croadcast/applyanchorGrid");
        return view;
    }

    /**
     * 查询主播资质信息
     */
    @ResponseBody
    @RequestMapping(value = "/getapplyanchorList", method = RequestMethod.GET)
    public Map<String, Object> getapplyanchorList(Integer page, Integer limit, AdminCroadcastReq croadcastReq) {
        Map<String, Object> resMap = new HashMap<>();
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        PageInfo<Map<String, Object>> pageInfo = croadcastService.getapplyanchorList(page, limit, croadcastReq);
        resMap.put("code", 0);
        resMap.put("msg", "");
        resMap.put("count", pageInfo.getTotal());
        resMap.put("data", pageInfo.getList());
        return resMap;
    }

    /**
     * 审批主播资质
     */
    @ResponseBody
    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public Map<String, Object> apply(AdminCroadcastReq croadcastReq) {
        Map<String, Object> resMap = new HashMap<>();
        try {
            croadcastService.apply(croadcastReq);
            if(croadcastReq.getaStatus().equals("1")){
                addCroadcastInfo(croadcastReq.getaId());
            }
            resMap.put("code", "success");
            resMap.put("msg", "审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "审核失败，error：" + e.getMessage());
        }
        return resMap;
    }

    /**
     * 根据Id查询主播资质信息
     */
    @ResponseBody
    @RequestMapping(value = "getanchorbyaId", method = RequestMethod.GET)
    public Map<String, Object> getanchorbyaId(AdminCroadcastReq croadcastReq) {
        Map<String, Object> resMap = croadcastService.getanchorbyaId(croadcastReq);
        return resMap;
    }

    /**
     * 跳转到主播资质查看界面
     */
    /*@RequestMapping(value = "/goanchorImg", method = RequestMethod.GET)
    public ModelAndView goanchorImg(ModelAndView view, AdminCroadcastReq croadcastReq) {
        view.addObject("aId", croadcastReq.getaId());
        view.setViewName("croadcast/anchorImg");
        return view;
    }*/

    /**
     * 跳转到主播资质管理界面
     */
    @RequestMapping(value = "/goanchorGrid")
    public ModelAndView goanchorGrid(ModelAndView view) {
        view.setViewName("croadcast/anchorGrid");
        return view;
    }

    /**
     * 删除直播信息
     */
    @ResponseBody
    @RequestMapping(value = "/deleteanchor", method = RequestMethod.GET)
    public Map<String, Object> deleteanchor(AdminCroadcastReq croadcastReq) {
        Map<String, Object> resMap = croadcastService.getanchorbyaId(croadcastReq);
        try {
            //删除身份证扫描件
            String zmPath = "";
            String fmPath = "";
            if (!StringUtils.isEmpty(resMap.get("filezmpath"))) {
                zmPath = String.valueOf(resMap.get("filezmpath")).replace("http://116.62.110.155/static/", fileSuffix);
                deleteFile(zmPath);
            }
            if (!StringUtils.isEmpty(resMap.get("filefmpath"))) {
                fmPath = String.valueOf(resMap.get("filefmpath")).replace("http://116.62.110.155/static/", fileSuffix);
                deleteFile(fmPath);
            }
            //删除直播间
            croadcastService.deleteCroadcastByAId(croadcastReq);
            //删除主播信息
            croadcastService.deleteAnchorByAId(croadcastReq);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","删除失败，error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 跳转到直播间列表管理
     */
    @RequestMapping(value = "/croadcastGrid",method = RequestMethod.GET)
    public ModelAndView goCroadcastGrid(ModelAndView view){
        view.setViewName("croadcast/croadcastGrid");
        return view;
    }
    /**
     * 查询直播间信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/getCroadcastList",method = RequestMethod.GET)
    public Map<String,Object> getCroadcastList(Integer page ,Integer limit,AdminCroadcastReq croadcastReq){
        Map<String, Object> resMap = new HashMap<>();
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        PageInfo<Map<String, Object>> pageInfo = croadcastService.getCroadcastList(page, limit, croadcastReq);
        resMap.put("code", 0);
        resMap.put("msg", "");
        resMap.put("count", pageInfo.getTotal());
        resMap.put("data", pageInfo.getList());
        return resMap;
    }
    /**
     * 查封直播间
     */
    @ResponseBody
    @RequestMapping(value ="/croadcastseizure" )
    public Map<String,Object> croadcastseizure(AdminCroadcastReq croadcastReq){
        Map<String,Object> resMap=new HashMap<>();
        try{
            croadcastService.croadcastseizure(croadcastReq);
            resMap.put("code","success");
            resMap.put("msg","查封成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","查封失败，error:"+e.getMessage());
        }
        return  resMap;
    }
    /**
     * 解封直播间
     */
    @ResponseBody
    @RequestMapping(value ="/croadcastnnseal" )
    public Map<String,Object> croadcastnnseal(AdminCroadcastReq croadcastReq){
        Map<String,Object> resMap=new HashMap<>();
        try{
            croadcastService.croadcastnnseal(croadcastReq);
            resMap.put("code","success");
            resMap.put("msg","解封成功");
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","解封失败，error:"+e.getMessage());
        }
        return  resMap;
    }
    /**
     * 查看主播资质
     */
    @ResponseBody
    @RequestMapping(value = "/goshowImg",method = RequestMethod.GET)
    public ModelAndView goshowImg(ModelAndView view,AdminCroadcastReq croadcastReq){
        Map<String, Object> map = croadcastService.getanchorbyaId(croadcastReq);
        view.addObject("filezmpath",map.get("filezmpath"));
        view.addObject("filefmpath",map.get("filefmpath"));
        view.setViewName("croadcast/showImg");
        return view;
    }
    /**
     * 删除文件
     * @param filePath
     */
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    /**
     * 查房
     */
    @RequestMapping(value = "/goshowrtmp",method = RequestMethod.GET)
    public ModelAndView goshowrtmp(ModelAndView view,AdminCroadcastReq croadcastReq){
        Map<String,Object> map=croadcastService.getCroadcastByRoomId(croadcastReq);
        view.addObject("src",map.get("rtmPath"));
        view.setViewName("croadcast/showrtmp");
        return view;
    }
    /**
     * 创建直播间
     */
    private void addCroadcastInfo(String aId) throws Exception {
        Croadcast croadcast=new Croadcast();
        croadcast.setStatus("0");
        croadcast.setAnchorId(aId);
        croadcast.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        croadcast.setRoomId(UUID.randomUUID().toString().replace("-",""));
        try {
            croadcastService.addCroadcastInfo(croadcast);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
