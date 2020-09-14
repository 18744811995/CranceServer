package com.hegu.tsurutani.app.mapper;

import com.hegu.tsurutani.entity.AnchorInfo;
import com.hegu.tsurutani.entity.RoomMsg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CroadcastMapper {
    void addAnchorInfo(AnchorInfo anchorInfo);

    void updateSattusAndrtmpPath(Map<String, Object> params);

    List<Map<String,Object>> getcroadcastList(Map<String, Object> params);

    void updateCroadcastTitleAndContent(Map<String, Object> params);

    Map<String,Object> getCroadcast(String roomId);

    void addCroadcastUser(Map<String, Object> params);

    Integer getCroadcastCount(String roomId);

    void deleteCroadcastUser(Map<String, Object> params);

    List<Map<String,Object>> getCroadcastUserInfo(String roomId);

    void addCroadcastRoomMsg(RoomMsg roomMsg);

    Integer getFollow(Map<String, Object> params);

    void deleteFollow(Map<String, Object> params);

    void addFollow(Map<String, Object> params);

    List<Map<String,Object>> getRoomUserList(Map<String, Object> params);

    Integer getcroadcastfxCount(String roomId);

    void addcroadcastfxInfo(Map<String, Object> params);

    void saveSendGift(Map<String, String> params);

    List<Map<String,Object>> getGiftList(Map<String, Object> params);
}
