package com.hegu.tsurutani.service;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.reqparam.AdminVideoReq;

import java.util.Map;

public interface AdminVideoService {
    PageInfo<Map<String,Object>> applyListPage(Integer page, Integer limit, AdminVideoReq videoReq);

    Map<String,Object> getVideoInfoByvId(String vId);

    void apply(AdminVideoReq videoReq);

    void deleteByVId(AdminVideoReq videoReq);
}
