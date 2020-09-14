package com.hegu.tsurutani.app.service;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.UserMsg;
import com.hegu.tsurutani.entity.UserParentMessage;

import java.util.List;
import java.util.Map;

public interface NewsService {
    List<Map<String,Object>> getMsgGroupList(String uId);

    PageInfo<Map<String,Object>> getGroupfriends(Map<String, Object> params);

    PageInfo<Map<String,Object>> getMsgChatList(Map<String, Object> params);

    List<Map<String,Object>> getUserInfo(Map<String, Object> params);

    void addfriends(Map<String, Object> params);

    void addmsgchat(Map<String, Object> params);

    void addGroup(Map<String, Object> params);

    void movegroup(Map<String, Object> params);

    void updategroup(Map<String, Object> params);

    Map<String,Object> getLastMsg(String mcId);

    PageInfo<Map<String,Object>> getusercrowdList(Map<String, Object> params);

    Map<String,Object> getCrowdMsgByCIId(String cIId);

    void addcrowdInfo(Map<String, Object> params);

    void addcrowdfriends(Map<String, Object> params);

    Map<String,Object> getcrowdInfo(Map<String, Object> params);

    List<Map<String,Object>> crowdUserList(Map<String, Object> params);

    void deleteGroup(Map<String, Object> params);

    void deletemsgchat(Map<String, Object> params);

    void deletefriends(Map<String, Object> params);

    PageInfo<Map<String,Object>> getMsgChatListPage(Map<String, Object> params);

    void addmsgchatmsg(UserParentMessage userMsg);

    void addcrowdMsg(UserMsg userMsg);

    void addGroupFriends(Map<String, Object> params2);

    PageInfo<Map<String,Object>> getMsgChatListfPage(Map<String, Object> params);

    String getGroupGIdByuId(String uId);

    void updateGroupFir(Map<String, Object> params);

    void deleteCrowdUser(Map<String, Object> params);

    Integer getNoReadCount(String mcId);

    String getFriendsByUIdAndfUId(String uId, String fuId);

    List<String> getCIdBymcId(String conversationId);

    void addmsgchilder(String cId, String id,boolean isRead,String status);

    void saveMsg(UserParentMessage message);

    PageInfo<Map<String,Object>> getSysMsgInfo(Map<String, Object> params);

    void updatefriendsstatus(Map<String, Object> param);

    void saveFriendsMsg(String fId, String id);

    Map<String,Object> getMsgChatByUIdAndFuId(String uId, String fuId);

    void updateChatChilder(String cId, String status);

    void updateIsRead(Map<String, Object> params);
}
