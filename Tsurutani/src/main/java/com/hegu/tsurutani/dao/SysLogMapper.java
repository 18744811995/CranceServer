package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.reqparam.AdminLogReqParam;
import com.hegu.tsurutani.handler.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysLogMapper {
    void saveSysLog(SysLogEntity logEntity);

    List<SysLogEntity> getLogList(AdminLogReqParam reqParam);
}
