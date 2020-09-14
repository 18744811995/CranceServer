package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.reqparam.AdminVideoReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AdminVideoMapper {

    List<Map<String,Object>> getApplyList(AdminVideoReq videoReq);

    Map<String,Object> getVideoInfoByvId(String vId);

    void apply(AdminVideoReq videoReq);

    void deleteByVId(AdminVideoReq videoReq);
}
