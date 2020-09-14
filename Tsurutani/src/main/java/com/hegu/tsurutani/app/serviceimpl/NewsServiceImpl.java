package com.hegu.tsurutani.app.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.mapper.AppNewsMapper;
import com.hegu.tsurutani.app.service.NewsService;
import com.hegu.tsurutani.entity.UserMsg;
import com.hegu.tsurutani.entity.UserParentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("newsService")
public class NewsServiceImpl implements NewsService {
    @Autowired
    private AppNewsMapper appNewsMapper;
    @Override
    public List<Map<String, Object>> getMsgGroupList(String uId) {
        return appNewsMapper.getMsgGroupList(uId);
    }

    @Override
    public PageInfo<Map<String, Object>> getGroupfriends(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getGroupfriends(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> getMsgChatList(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getMsgChatList(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> getUserInfo(Map<String, Object> params) {
        return appNewsMapper.getUserInfo(params);
    }

    @Override
    public void addfriends(Map<String, Object> params) {
        appNewsMapper.addfriends(params);
    }

    @Override
    public void addmsgchat(Map<String, Object> params) {
        appNewsMapper.addmsgchat(params);
        if(String.valueOf(params.get("cType")).equals("1")){
            String cId1= UUID.randomUUID().toString().replace("-","");
            String cId2=UUID.randomUUID().toString().replace("-","");
            Map<String,Object> param1=new HashMap<>();
            param1.put("cId",cId1);
            param1.put("uId",params.get("uId"));
            param1.put("fuId",params.get("fuId"));
            param1.put("isshow",true);
            param1.put("mcId",params.get("mcId"));

            Map<String,Object> param2=new HashMap<>();
            param2.put("cId",cId2);
            param2.put("uId",params.get("fuId"));
            param2.put("fuId",params.get("uId"));
            param2.put("isshow",true);
            param2.put("mcId",params.get("mcId"));
            appNewsMapper.addmsgchatChilder(param1);
            appNewsMapper.addmsgchatChilder(param2);
        }else if(String.valueOf(params.get("cType")).equals("2")){
            List<String> groupUId=appNewsMapper.getGroupUIdsByCIId(String.valueOf(params.get("fuId")));
            //SELECT uId FROM sys_crowd_friends WHERE cIId=#{cIId}
            for (String uIds:groupUId){
                String cId= UUID.randomUUID().toString().replace("-","");
                Map<String,Object> param1=new HashMap<>();
                param1.put("cId",cId);
                param1.put("uId",uIds);
                param1.put("fuId",params.get("fuId"));
                param1.put("isshow",true);
                param1.put("mcId",params.get("mcId"));
                appNewsMapper.addmsgchatChilder(param1);
            }
        }
    }

    @Override
    public void addGroup(Map<String, Object> params) {
        appNewsMapper.addGroup(params);
    }

    @Override
    public void movegroup(Map<String, Object> params) {
        appNewsMapper.deleteGroupFriends(params);
        appNewsMapper.addGroupFriends(params);
    }

    @Override
    public void updategroup(Map<String, Object> params) {
        appNewsMapper.updategroup(params);
    }

    @Override
    public Map<String, Object> getLastMsg(String mcId) {
        return appNewsMapper.getLastMsg(mcId);
    }

    @Override
    public PageInfo<Map<String, Object>> getusercrowdList(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getusercrowdList(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getCrowdMsgByCIId(String cIId) {
        return appNewsMapper.getCrowdMsgByCIId(cIId);
    }

    @Override
    public void addcrowdInfo(Map<String, Object> params) {
        appNewsMapper.addcrowdInfo(params);
    }

    @Override
    public void addcrowdfriends(Map<String, Object> params) {
        appNewsMapper.addcrowdfriends(params);
    }

    @Override
    public Map<String, Object> getcrowdInfo(Map<String, Object> params) {
        return appNewsMapper.getcrowdInfo(params);
    }

    @Override
    public List<Map<String, Object>> crowdUserList(Map<String, Object> params) {
        return appNewsMapper.crowdUserList(params);
    }

    @Override
    public void deleteGroup(Map<String, Object> params) {
        //删除分组
        appNewsMapper.deleteGroup(params);
        //删除分组好友
        //appNewsMapper.deleteGroupFriends(params);
    }

    @Override
    public void deletemsgchat(Map<String, Object> params) {
        appNewsMapper.deletemsgchat(params);
    }

    @Override
    public void deletefriends(Map<String, Object> params) {
        appNewsMapper.deletefriends(params);
    }

    @Override
    public PageInfo<Map<String, Object>> getMsgChatListPage(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getMsgChatListPage(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void addmsgchatmsg(UserParentMessage userMsg) {
        try {
            appNewsMapper.addmsgchatmsg(userMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addcrowdMsg(UserMsg userMsg) {
        appNewsMapper.addcrowdMsg(userMsg);
    }

    @Override
    public void addGroupFriends(Map<String, Object> params2) {
        appNewsMapper.addGroupFriends(params2);
    }

    @Override
    public PageInfo<Map<String, Object>> getMsgChatListfPage(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getMsgChatListfPage(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public String getGroupGIdByuId(String uId) {
        return appNewsMapper.getGroupGIdByuId(uId);
    }

    @Override
    public void updateGroupFir(Map<String, Object> params) {
        appNewsMapper.updateGroupFir(params);
    }

    @Override
    public void deleteCrowdUser(Map<String, Object> params) {
        appNewsMapper.deleteCrowdUser(params);
    }

    @Override
    public Integer getNoReadCount(String mcId) {
        return appNewsMapper.getNoReadCount(mcId);
    }

    @Override
    public String getFriendsByUIdAndfUId(String uId, String fuId) {
        return appNewsMapper.getFriendsByUIdAndfUId(uId,fuId);
    }

    @Override
    public List<String> getCIdBymcId(String conversationId) {
        return appNewsMapper.getCIdBymcId(conversationId);
    }

    @Override
    public void addmsgchilder(String cId, String id,boolean isRead,String status) {
        appNewsMapper.addmsgchilder(cId,id,isRead,status);
    }

    @Override
    public void saveMsg(UserParentMessage message) {
        appNewsMapper.saveMsg(message);
    }

    @Override
    public PageInfo<Map<String, Object>> getSysMsgInfo(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=appNewsMapper.getSysMsgInfo(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public void updatefriendsstatus(Map<String, Object> param) {
        appNewsMapper.updatefriendsstatus(param);
    }

    @Override
    public void saveFriendsMsg(String fId, String id) {
        appNewsMapper.saveFriendsMsg(fId,id);
    }

    @Override
    public Map<String, Object> getMsgChatByUIdAndFuId(String uId, String fuId) {
        return appNewsMapper.getMsgChatByUIdAndFuId(uId,fuId);
    }

    @Override
    public void updateChatChilder(String cId, String status) {
        appNewsMapper.updateChatChilder(cId,status);
    }

    @Override
    public void updateIsRead(Map<String, Object> params) {
        appNewsMapper.updateIsRead(params);
    }
}
