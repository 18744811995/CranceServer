package com.hegu.tsurutani.service.impl;

import com.hegu.tsurutani.dao.VideoMapper;
import com.hegu.tsurutani.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper sysLogMapper;
    @Override
    public void add(String msg, String opt, String date, int uId) {
        sysLogMapper.add(msg,opt,date,uId);
    }

    @Override
    public PageInfo<Map<String, Object>> findLogPage(String userno, String optdate, Integer page, Integer limit) {
        PageInfo<Map<String,Object>> pageInfo=null;
        PageHelper.offsetPage((page-1)*limit,limit);
        List<Map<String,Object>> logs=sysLogMapper.findLogPage(userno,optdate);
        pageInfo=new PageInfo<>(logs);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> findCoypPage(Integer page, Integer limit) {
        PageInfo<Map<String,Object>> pageInfo=null;
        PageHelper.offsetPage((page-1)*limit,limit);
        List<Map<String,Object>> resList=sysLogMapper.findCoypPage();
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void copy() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuId= UUID.randomUUID().toString().replace("-","");
        sysLogMapper.saveBackupsRoder(uuId,format.format(new Date()));
        sysLogMapper.backupsAssets(uuId);
        sysLogMapper.backupsAssetsck(uuId);
        sysLogMapper.backupsAssetsUpdate(uuId);
        sysLogMapper.backupsAssetsBx(uuId);
        sysLogMapper.backupsAssetszzc(uuId);
    }

    @Override
    public void restore(String backupsno) {
        sysLogMapper.deleteAssets();
        sysLogMapper.deleteAssetsck();
        sysLogMapper.deleteAssetsUpdate();
        sysLogMapper.deleteAssetsBx();
        sysLogMapper.deleteAssetszzc();
        sysLogMapper.restoreAssets(backupsno);
        sysLogMapper.restoreAssetsck(backupsno);
        sysLogMapper.restoreAssetsUpdate(backupsno);
        sysLogMapper.restoreAssetsBx(backupsno);
        sysLogMapper.restoreAssetszzc(backupsno);
    }
}
