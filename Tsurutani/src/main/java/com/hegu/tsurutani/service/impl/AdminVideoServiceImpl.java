package com.hegu.tsurutani.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.dao.AdminVideoMapper;
import com.hegu.tsurutani.entity.reqparam.AdminVideoReq;
import com.hegu.tsurutani.service.AdminVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminVideoServiceImpl implements AdminVideoService {
    @Autowired
    private AdminVideoMapper adminVideoMapper;
    @Override
    public PageInfo<Map<String, Object>> applyListPage(Integer page, Integer limit, AdminVideoReq videoReq) {
        PageHelper.offsetPage((page-1)*limit,limit);
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=adminVideoMapper.getApplyList(videoReq);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getVideoInfoByvId(String vId) {
        return adminVideoMapper.getVideoInfoByvId(vId);
    }

    @Override
    public void apply(AdminVideoReq videoReq) {
        adminVideoMapper.apply(videoReq);
    }

    @Override
    public void deleteByVId(AdminVideoReq videoReq) {
        adminVideoMapper.deleteByVId(videoReq);
    }
}
