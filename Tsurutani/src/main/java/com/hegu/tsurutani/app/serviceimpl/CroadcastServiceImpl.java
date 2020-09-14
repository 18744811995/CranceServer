package com.hegu.tsurutani.app.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.mapper.CroadcastMapper;
import com.hegu.tsurutani.app.service.CroadcastService;
import com.hegu.tsurutani.entity.AnchorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CroadcastServiceImpl implements CroadcastService {
    @Autowired
    private CroadcastMapper croadcastDao;
    @Override
    public void addAnchorInfo(AnchorInfo anchorInfo) {
        croadcastDao.addAnchorInfo(anchorInfo);
    }

    @Override
    public void updateSattusAndrtmpPath(Map<String, Object> params) {
        croadcastDao.updateSattusAndrtmpPath(params);
    }

    @Override
    public PageInfo<Map<String, Object>> getcroadcastList(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=croadcastDao.getcroadcastList(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void updateCroadcastTitleAndContent(Map<String, Object> params) {
        croadcastDao.updateCroadcastTitleAndContent(params);
    }

    @Override
    public Map<String, Object> getCroadcast(String roomId) {
        return croadcastDao.getCroadcast(roomId);
    }

    @Override
    public Integer getFollow(Map<String, Object> params) {
        return croadcastDao.getFollow(params);
    }

    @Override
    public void deleteFollow(Map<String, Object> params) {
        croadcastDao.deleteFollow(params);
    }

    @Override
    public void addFollow(Map<String, Object> params) {
        croadcastDao.addFollow(params);
    }

    @Override
    public PageInfo<Map<String, Object>> getRoomUserList(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=croadcastDao.getRoomUserList(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public Integer getcroadcastfxCount(String roomId) {
        return croadcastDao.getcroadcastfxCount(roomId);
    }

    @Override
    public void addcroadcastfxInfo(Map<String, Object> params) {
        croadcastDao.addcroadcastfxInfo(params);
    }

    @Override
    public void saveSendGift(Map<String, String> params) {
        croadcastDao.saveSendGift(params);
    }

    @Override
    public List<Map<String, Object>> getGiftList(Map<String, Object> params) {
        return croadcastDao.getGiftList(params);
    }
}
