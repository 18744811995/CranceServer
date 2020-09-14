package com.hegu.tsurutani.controller;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.reqparam.AdminLogReqParam;
import com.hegu.tsurutani.handler.SysLogEntity;
import com.hegu.tsurutani.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/log")
public class LogController {
    @Autowired
    private SysLogService sysLogService;
    /**
     * 跳转到查询系统日志界面
     */
    @RequestMapping("/goLogGrid")
    public ModelAndView goLogGrid(ModelAndView view){
        view.setViewName("Log/logGrid");
        return view;
    }
    /**
     * 查询当前页日志数据
     */
    @ResponseBody
    @RequestMapping("/getLogPage")
    public Map<String,Object> getLogPage(AdminLogReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        if(reqParam.getPage()==null){
            reqParam.setPage(1);
        }
        if(reqParam.getLimit()==null){
            reqParam.setLimit(10);
        }
        PageInfo<SysLogEntity> pageInfo=sysLogService.getLogPage(reqParam);
        resMap.put("code",0);
        resMap.put("msg","");
        resMap.put("count",pageInfo.getTotal());
        resMap.put("data",pageInfo.getList());
       /* resMap.put("pageIndex", reqParam.getPage());
        resMap.put("pageSize", reqParam.getLimit());
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());*/
        return resMap;
    }
}
