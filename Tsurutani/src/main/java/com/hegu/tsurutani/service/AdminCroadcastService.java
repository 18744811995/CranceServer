package com.hegu.tsurutani.service;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.Croadcast;
import com.hegu.tsurutani.entity.reqparam.AdminCroadcastReq;

import java.util.Map;

public interface AdminCroadcastService {
    PageInfo<Map<String,Object>> getapplyanchorList(Integer page, Integer limit, AdminCroadcastReq croadcastReq);

    void apply(AdminCroadcastReq croadcastReq);

    Map<String,Object> getanchorbyaId(AdminCroadcastReq croadcastReq);

    void deleteCroadcastByAId(AdminCroadcastReq croadcastReq);

    void deleteAnchorByAId(AdminCroadcastReq croadcastReq);

    PageInfo<Map<String,Object>> getCroadcastList(Integer page, Integer limit, AdminCroadcastReq croadcastReq);

    void croadcastseizure(AdminCroadcastReq croadcastReq);

    void croadcastnnseal(AdminCroadcastReq croadcastReq);

    void addCroadcastInfo(Croadcast croadcast);

    Map<String,Object> getCroadcastByRoomId(AdminCroadcastReq croadcastReq);
}
