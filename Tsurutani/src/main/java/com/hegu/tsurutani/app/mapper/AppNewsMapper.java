package com.hegu.tsurutani.app.mapper;

import com.hegu.tsurutani.entity.UserMsg;
import com.hegu.tsurutani.entity.UserParentMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AppNewsMapper {

    List<Map<String,Object>> getMsgGroupList(String uId);

    List<Map<String,Object>> getGroupfriends(Map<String, Object> params);

    List<Map<String,Object>> getMsgChatList(Map<String, Object> params);

    List<Map<String,Object>> getUserInfo(Map<String, Object> params);

    void addfriends(Map<String, Object> params);

    void addmsgchat(Map<String, Object> params);

    void addGroup(Map<String, Object> params);

    void deleteGroupFriends(Map<String, Object> params);

    void addGroupFriends(Map<String, Object> params);

    void updategroup(Map<String, Object> params);

    Map<String,Object> getLastMsg(String cId);

    List<Map<String,Object>> getusercrowdList(Map<String, Object> params);

    Map<String,Object> getCrowdMsgByCIId(String cIId);

    void addcrowdInfo(Map<String, Object> params);

    void addcrowdfriends(Map<String, Object> params);

    Map<String,Object> getcrowdInfo(Map<String, Object> params);

    List<Map<String,Object>> crowdUserList(Map<String, Object> params);

    void deleteGroup(Map<String, Object> params);

    void deletemsgchat(Map<String, Object> params);

    void deletefriends(Map<String, Object> params);

    List<Map<String,Object>> getMsgChatListPage(Map<String, Object> params);

    void addmsgchatmsg(UserParentMessage userMsg);

    void addcrowdMsg(UserMsg userMsg);

    List<Map<String,Object>> getMsgChatListfPage(Map<String, Object> params);

    String getGroupGIdByuId(String uId);

    void updateGroupFir(Map<String, Object> params);

    void deleteCrowdUser(Map<String, Object> params);

    Integer getNoReadCount(String cId);

    String getFriendsByUIdAndfUId(@Param("uId") String uId,@Param("fuId") String fuId);

    void addmsgchatChilder(Map<String, Object> param1);

    List<String> getCIdBymcId(@Param("mcId") String conversationId);

    void addmsgchilder(@Param("cId") String cId, @Param("id") String id,@Param("isRead") boolean isRead,@Param("status") String status);

    List<String> getGroupUIdsByCIId(@Param("cIId") String fuId);

    void saveMsg( UserParentMessage message);

    List<Map<String,Object>> getSysMsgInfo(Map<String, Object> params);

    void updatefriendsstatus(Map<String, Object> param);

    void saveFriendsMsg(@Param("fId") String fId,@Param("mId") String id);

    Map<String,Object> getMsgChatByUIdAndFuId(@Param("uId") String uId,@Param("fuId") String fuId);

    void updateChatChilder(@Param("cId") String cId, @Param("status") String status);

    void updateIsRead(Map<String, Object> params);
}
