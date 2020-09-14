package com.hegu.tsurutani.service;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.reqparam.AdminLogReqParam;
import com.hegu.tsurutani.handler.SysLogEntity;

public interface SysLogService {
    public void saveSysLog(SysLogEntity logEntity);

    PageInfo<SysLogEntity> getLogPage(AdminLogReqParam reqParam);
}
