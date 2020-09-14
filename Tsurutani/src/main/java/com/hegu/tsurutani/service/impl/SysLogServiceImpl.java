package com.hegu.tsurutani.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.dao.SysLogMapper;
import com.hegu.tsurutani.entity.reqparam.AdminLogReqParam;
import com.hegu.tsurutani.handler.SysLogEntity;
import com.hegu.tsurutani.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Override
    public void saveSysLog(SysLogEntity logEntity) {
        sysLogMapper.saveSysLog(logEntity);
    }

    @Override
    public PageInfo<SysLogEntity> getLogPage(AdminLogReqParam reqParam) {
        PageHelper.offsetPage((reqParam.getPage()-1)*reqParam.getLimit(),reqParam.getLimit());
        List<SysLogEntity> resList=sysLogMapper.getLogList(reqParam);
        PageInfo<SysLogEntity> pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }
}
