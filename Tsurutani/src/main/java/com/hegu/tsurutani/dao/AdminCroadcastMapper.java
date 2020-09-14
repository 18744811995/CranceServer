package com.hegu.tsurutani.dao;

import com.hegu.tsurutani.entity.Croadcast;
import com.hegu.tsurutani.entity.reqparam.AdminCroadcastReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AdminCroadcastMapper {
    List<Map<String,Object>> getapplyanchorList(AdminCroadcastReq croadcastReq);

    void apply(AdminCroadcastReq croadcastReq);

    Map<String,Object> getanchorbyaId(AdminCroadcastReq croadcastReq);

    void deleteCroadcastByAId(AdminCroadcastReq croadcastReq);

    void deleteAnchorByAId(AdminCroadcastReq croadcastReq);

    List<Map<String,Object>> getCroadcastList(AdminCroadcastReq croadcastReq);

    void croadcastseizure(AdminCroadcastReq croadcastReq);

    void croadcastnnseal(AdminCroadcastReq croadcastReq);

    void addCroadcastInfo(Croadcast croadcast);

    Map<String,Object> getCroadcastByRoomId(AdminCroadcastReq croadcastReq);
}
