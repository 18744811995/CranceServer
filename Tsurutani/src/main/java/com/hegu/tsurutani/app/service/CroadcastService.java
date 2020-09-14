package com.hegu.tsurutani.app.service;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.AnchorInfo;

import java.util.List;
import java.util.Map;

public interface CroadcastService {
    void addAnchorInfo(AnchorInfo anchorInfo);

    void updateSattusAndrtmpPath(Map<String, Object> params);

    PageInfo<Map<String,Object>> getcroadcastList(Map<String, Object> params);

    void updateCroadcastTitleAndContent(Map<String, Object> params);

    Map<String,Object> getCroadcast(String roomId);

    Integer getFollow(Map<String, Object> params);

    void deleteFollow(Map<String, Object> params);

    void addFollow(Map<String, Object> params);

    PageInfo<Map<String,Object>> getRoomUserList(Map<String, Object> params);

    Integer getcroadcastfxCount(String roomId);

    void addcroadcastfxInfo(Map<String, Object> params);

    void saveSendGift(Map<String, String> params);

    List<Map<String,Object>> getGiftList(Map<String, Object> params);
}
