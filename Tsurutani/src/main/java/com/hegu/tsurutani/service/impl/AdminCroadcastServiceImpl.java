package com.hegu.tsurutani.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.dao.AdminCroadcastMapper;
import com.hegu.tsurutani.entity.Croadcast;
import com.hegu.tsurutani.entity.reqparam.AdminCroadcastReq;
import com.hegu.tsurutani.service.AdminCroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminCroadcastServiceImpl implements AdminCroadcastService {
    @Autowired
    private AdminCroadcastMapper croadcastMapper;
    @Override
    public PageInfo<Map<String, Object>> getapplyanchorList(Integer page, Integer limit, AdminCroadcastReq croadcastReq) {
        PageHelper.offsetPage((page-1)*limit,limit);
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=croadcastMapper.getapplyanchorList(croadcastReq);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void apply(AdminCroadcastReq croadcastReq) {
        croadcastMapper.apply(croadcastReq);
    }

    @Override
    public Map<String, Object> getanchorbyaId(AdminCroadcastReq croadcastReq) {
        return croadcastMapper.getanchorbyaId(croadcastReq);
    }

    @Override
    public void deleteCroadcastByAId(AdminCroadcastReq croadcastReq) {
        croadcastMapper.deleteCroadcastByAId(croadcastReq);
    }

    @Override
    public void deleteAnchorByAId(AdminCroadcastReq croadcastReq) {
        croadcastMapper.deleteAnchorByAId(croadcastReq);
    }

    @Override
    public PageInfo<Map<String, Object>> getCroadcastList(Integer page, Integer limit, AdminCroadcastReq croadcastReq) {
        PageHelper.offsetPage((page-1)*limit,limit);
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=croadcastMapper.getCroadcastList(croadcastReq);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void croadcastseizure(AdminCroadcastReq croadcastReq) {
        croadcastMapper.croadcastseizure(croadcastReq);
    }

    @Override
    public void croadcastnnseal(AdminCroadcastReq croadcastReq) {
        croadcastMapper.croadcastnnseal(croadcastReq);
    }

    @Override
    public void addCroadcastInfo(Croadcast croadcast) {
        croadcastMapper.addCroadcastInfo(croadcast);
    }

    @Override
    public Map<String, Object> getCroadcastByRoomId(AdminCroadcastReq croadcastReq) {
        return croadcastMapper.getCroadcastByRoomId(croadcastReq);
    }
}
