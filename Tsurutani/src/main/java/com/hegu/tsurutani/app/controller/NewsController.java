package com.hegu.tsurutani.app.controller;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.service.NewsService;
import com.hegu.tsurutani.entity.UserChilderMessage;
import com.hegu.tsurutani.entity.UserParentMessage;
import com.hegu.tsurutani.entity.reqparam.AppNewsReqParam;
import com.hegu.tsurutani.websocket.endpoint.MsgEndpointExporter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author FYH
 * 消息控制器
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @Value("${server.req.sources.path}")
    private String httppath;
    @Value("${server.msg.file.path}")
    private String msgfilepath;
    //MsgEndpointExporter msgWebsocket=new MsgEndpointExporter();
    //websocket.onMessage(json.toJSONString());
    /**
     * 查询分组列表
     */
    @RequestMapping(value = "/getMsgGroupList",method = RequestMethod.GET)
    public List<Map<String,Object>> getMsgGroupList(AppNewsReqParam reqParam){
        List<Map<String,Object>> resList=newsService.getMsgGroupList(reqParam.getuId());
        return resList;
    }
     /**
     * 查询分组下用户
     */
    @RequestMapping(value = "/getGroupfriends",method =RequestMethod.GET)
    public Map<String,Object> getGroupfriends(AppNewsReqParam reqParam){
        if (reqParam.getPage()==null){
            reqParam.setPage(1);
        }
        if(reqParam.getLimit()==null){
            reqParam.setLimit(10);
        }
        Map<String,Object> params=new HashMap<>();
        params.put("page",reqParam.getPage());
        params.put("limit",reqParam.getLimit());
        params.put("uId",reqParam.getuId());
        params.put("gId",reqParam.getgId());
        PageInfo<Map<String,Object>> pageInfo=newsService.getGroupfriends(params);
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("pageIndex", reqParam.getPage());
        resMap.put("pageSize", reqParam.getLimit());
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }
    /**
     * 查询聊天列表
     */
    @RequestMapping(value = "/getMsgChatList",method = RequestMethod.GET)
    public Map<String,Object> getMsgChatList(AppNewsReqParam reqParam) throws ParseException {
        if (reqParam.getPage()==null){
            reqParam.setPage(1);
        }
        if(reqParam.getLimit()==null){
            reqParam.setLimit(10);
        }
        Map<String,Object> params=new HashMap<>();
        params.put("page",reqParam.getPage());
        params.put("limit",reqParam.getLimit());
        params.put("uId",reqParam.getuId());
        PageInfo<Map<String,Object>> pageInfo=newsService.getMsgChatList(params);
        for(int i=0;i<pageInfo.getList().size();i++){
            pageInfo.getList().get(i).put("lastMsg",getLastMsg(String.valueOf(pageInfo.getList().get(i).get("cId"))));
            pageInfo.getList().get(i).put("noreadCount",getNoReadCount(String.valueOf(pageInfo.getList().get(i).get("cId"))));
            pageInfo.getList().get(i).put("create_time",getdateTime(String.valueOf(pageInfo.getList().get(i).get("create_time"))));
        }
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("pageIndex", reqParam.getPage());
        resMap.put("pageSize", reqParam.getLimit());
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }

    private Object getNoReadCount(String mcId) {
        return newsService.getNoReadCount(mcId);
    }

    private long getdateTime(String dateStr) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long datetime=format.parse(dateStr).getTime();
        return datetime;
    }

    /**
     * 查询聊天最后一条数据库
     */
    private UserParentMessage getLastMsg(String cId){
        Map<String,Object> resMap=newsService.getLastMsg(cId);
        if(resMap==null||resMap.size()<=0){
            return null;
        }
        UserParentMessage parentMessage=formatterMessage(resMap);
        return parentMessage;
    }
    /**
     * 转换成消息类型数据
     */
    public UserParentMessage formatterMessage(Map<String,Object> resMap){
        UserParentMessage parentMessage=new UserParentMessage();
        if(!StringUtils.isEmpty(resMap.get("toUser"))){
            parentMessage.setToUser(String.valueOf(resMap.get("toUser")));
        }
        if(!StringUtils.isEmpty(resMap.get("ConversationId"))){
            parentMessage.setConversationId(String.valueOf(resMap.get("ConversationId")));
        }
        if(!StringUtils.isEmpty(resMap.get("formIcon"))){
            parentMessage.setFormIcon(String.valueOf(resMap.get("formIcon")));
        }
        if(!StringUtils.isEmpty(resMap.get("formName"))){
            parentMessage.setFormName(String.valueOf(resMap.get("formName")));
        }
        if(!StringUtils.isEmpty(resMap.get("formUser"))){
            parentMessage.setFormUser(String.valueOf(resMap.get("formUser")));
        }
        if(!StringUtils.isEmpty(resMap.get("isGroup"))){
            if(String.valueOf(resMap.get("isGroup")).equals("1")){
                parentMessage.setIsGroup(true);
            }else if(String.valueOf(resMap.get("isGroup")).equals("0")){
                parentMessage.setIsGroup(false);
            }
            //parentMessage.setIsGroup(Boolean.valueOf(String.valueOf(resMap.get("isGroup"))));
        }
        if(!StringUtils.isEmpty(resMap.get("msgTime"))){
            parentMessage.setMsgTime(new Long(String.valueOf(resMap.get("msgTime"))));
        }
        if(!StringUtils.isEmpty(resMap.get("msgType"))){
            parentMessage.setMsgType(Integer.valueOf(String.valueOf(resMap.get("msgType"))));
        }
        UserChilderMessage childerMessage=new UserChilderMessage();
        if(!StringUtils.isEmpty(resMap.get("id"))){
            childerMessage.setId(String.valueOf(resMap.get("id")));
        }
        if(!StringUtils.isEmpty(resMap.get("duration"))){
            childerMessage.setDuration(Integer.valueOf(String.valueOf( resMap.get("duration"))));
        }
        if(!StringUtils.isEmpty(resMap.get("extra"))){
            childerMessage.setExtra(String.valueOf(resMap.get("extra")));
        }
        if(!StringUtils.isEmpty(resMap.get("formUser"))){
            childerMessage.setFormUser(String.valueOf(resMap.get("formUser")));
        }
        if(!StringUtils.isEmpty(resMap.get("height"))){
            childerMessage.setHeight(Integer.valueOf(String.valueOf(resMap.get("height"))));
        }
        if(!StringUtils.isEmpty(resMap.get("msgTime"))){
            childerMessage.setMsgTime(new Long(String.valueOf(resMap.get("msgTime"))));
        }
        if(!StringUtils.isEmpty(resMap.get("msgType"))){
            childerMessage.setMsgType(Integer.valueOf(String.valueOf(resMap.get("msgType"))));
        }
        if(!StringUtils.isEmpty(resMap.get("isRead"))){
            if(String.valueOf(resMap.get("isRead")).equals("1")){
                childerMessage.setRead(true);
            }else if(String.valueOf(resMap.get("isRead")).equals("0")){
                childerMessage.setRead(false);
            }
            //childerMessage.setRead(Boolean.valueOf(String.valueOf(resMap.get("isRead"))));
        }
        if(!StringUtils.isEmpty(resMap.get("self"))){
            if(String.valueOf(resMap.get("self")).equals("1")){
                childerMessage.setSelf(true);
            }else if(String.valueOf(resMap.get("self")).equals("0")){
                childerMessage.setSelf(false);
            }
        }
        if(!StringUtils.isEmpty(resMap.get("status"))){
            childerMessage.setStatus(String.valueOf(resMap.get("status")));
        }
        if(!StringUtils.isEmpty(resMap.get("text"))){
            childerMessage.setText(String.valueOf(resMap.get("text")));
        }
        if(!StringUtils.isEmpty(resMap.get("width"))){
            childerMessage.setWidth(Integer.valueOf(String.valueOf(resMap.get("width"))));
        }
        if(!StringUtils.isEmpty(resMap.get("dataPath"))){
            childerMessage.setDataPath(String.valueOf(resMap.get("dataPath")));
        }
        parentMessage.setMessage(childerMessage);
        return parentMessage;
    }
    /**
     * 查询用户资料
     */
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public List<Map<String,Object>> getUserInfo(AppNewsReqParam reqParam){
        Map<String,Object> params=new HashMap<>();
        params.put("ckName",reqParam.getCkName());
        params.put("username",reqParam.getUsername());
        params.put("uId",reqParam.getuId());
        params.put("fuId",reqParam.getFuId());
        List<Map<String,Object>> resList=newsService.getUserInfo(params);
        for(int i=0;i<resList.size();i++){
            if(!String.valueOf(resList.get(i).get("isFriends")).equals("0")){
                resList.get(i).put("remark",getFriendsByUIdAndfUId(reqParam.getuId(),String.valueOf(resList.get(i).get("uId"))));
            }
        }
        return resList;
    }
    private String getFriendsByUIdAndfUId(String uId,String fuId){
        return newsService.getFriendsByUIdAndfUId(uId,fuId);
    }
    /**
     * 添加好友
     */
    @RequestMapping(value = "/addfriends",method = RequestMethod.POST)
    public Map<String,Object> addfriends(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            String fId= UUID.randomUUID().toString().replace("-","");
            String create_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            params.put("fId",fId);
            params.put("uId",reqParam.getuId());
            params.put("fuId",reqParam.getFuId());
            params.put("remark",reqParam.getRemark());
            params.put("create_time",create_time);
            params.put("status","0");
            newsService.addfriends(params);
            resMap.put("code","success");
            resMap.put("msg","已提交好友申请");
            Map<String,Object> params2=new HashMap<>();
            params2.put("fId",fId);
            params2.put("gIds",reqParam.getgId());
            newsService.addGroupFriends(params2);
            sendSysMsg(fId,reqParam.getuId(),reqParam.getFuId(),"121");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","success");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 同意添加好友
     */
    @RequestMapping(value = "/updatefriendsstatus",method = RequestMethod.GET)
    public Map<String,Object> updatefriendsstatus(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> param=new HashMap<>();
            param.put("id",reqParam.getId());
            param.put("status",reqParam.getStatus());
            newsService.updatefriendsstatus(param);
            resMap.put("code","success");
            resMap.put("msg","审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","审核失败");
        }
        return  resMap;
    }
    /**
     * 发送系统消息
     */
    private void sendSysMsg(String fId,String uId,String fuId,String msgType){
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("toUser",fuId);
        resMap.put("ConversationId","");
        resMap.put("formIcon","");
        resMap.put("formName","鹤谷");
        resMap.put("formUser","0f003788ddb34cdc9834f2a8684ddbb4");
        resMap.put("id",UUID.randomUUID().toString().replace("-",""));
        resMap.put("extra","系统通知");
        resMap.put("msgTime",new Date().getTime());
        resMap.put("msgType",msgType);
        resMap.put("isRead",false);
        resMap.put("status",0);
        resMap.put("text",uId+"发起添加好友申请");
        UserParentMessage message=formatterMessage(resMap);
        MsgEndpointExporter msgEndpointExporter=new MsgEndpointExporter("101");
        msgEndpointExporter.sendSysMsg(message,fId);
    }
    /**
     * 添加聊天
     * String uId,String fuId,String funame,String fuimg,String cType
     */
    @RequestMapping(value = "/addmsgchat",method = RequestMethod.POST)
    public Map<String,Object> addmsgchat(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            //根据uId、fuId查询是否已存在当前会话，如果存在修改状态，返回mcId如果不存在，执行添加代码
            String mcId="";
            Map<String,Object> chartMap=newsService.getMsgChatByUIdAndFuId(reqParam.getuId(),reqParam.getFuId());
            if(chartMap==null||chartMap.size()<=0){
                mcId=UUID.randomUUID().toString().replace("-","");
                String create_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Map<String,Object> params=new HashMap<>();
                params.put("mcId",mcId);
                params.put("create_time",create_time);
                params.put("uId",reqParam.getuId());
                params.put("fuId",reqParam.getFuId());
                params.put("funame",reqParam.getFuname());
                params.put("fuimg",reqParam.getFuimg());
                params.put("cType",reqParam.getcType());
                newsService.addmsgchat(params);
            }else{
                mcId=String.valueOf(chartMap.get("mcId"));
                String cId=String.valueOf(chartMap.get("cId"));
                newsService.updateChatChilder(cId,"1");
            }
            resMap.put("code","success");
            resMap.put("msg","添加聊天成功");
            resMap.put("data",mcId);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
            resMap.put("data","");
        }
        return resMap;
    }

    /**
     * 添加分组
     */
    @RequestMapping(value = "/addgroup",method = RequestMethod.POST)
    public Map<String,Object> addgroup(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            String gId=UUID.randomUUID().toString();
            params.put("gId",gId);
            params.put("uId",reqParam.getuId());
            params.put("gname",reqParam.getGname());
            newsService.addGroup(params);
            resMap.put("code","success");
            resMap.put("msg","添加分组成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","success");
            resMap.put("msg","添加分组失败");
        }
        return resMap;
    }
    /**
     * 移动分组好友
     * String gId,String gIds,String fId
     */
    @RequestMapping(value = "/movegroup",method = RequestMethod.GET)
    public Map<String,Object> movegroup(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("gId",reqParam.getgId());
            params.put("gIds",reqParam.getgIds());
            params.put("fId",reqParam.getfId());
            newsService.movegroup(params);
            resMap.put("code","success");
            resMap.put("msg","移动成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }

    /**
     * 修改分组
     * String gId,String gname,String uId
     */
    @RequestMapping(value = "/updategroup",method = RequestMethod.GET)
    public Map<String,Object> updategroup(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("gId",reqParam.getgId());
            params.put("gname",reqParam.getGname());
            params.put("uId",reqParam.getuId());
            newsService.updategroup(params);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }

    /**
     * 查询群列表
     * String uId,Integer page,Integer limit
     */
    @RequestMapping(value = "/getusercrowdList",method = RequestMethod.GET)
    public Map<String,Object> getusercrowdList(AppNewsReqParam reqParam){
        if(reqParam.getPage()==null){
            reqParam.setPage(1);
        }
        if(reqParam.getLimit()==null){
            reqParam.setLimit(10);
        }
        Map<String,Object> resMap=new HashMap<>();
        Map<String,Object> params=new HashMap<>();
        params.put("uId",reqParam.getuId());
        params.put("page",reqParam.getPage());
        params.put("limit",reqParam.getLimit());
        PageInfo<Map<String,Object>> pageInfo=newsService.getusercrowdList(params);
        /*for(int i=0;i<pageInfo.getList().size();i++){
            pageInfo.getList().get(i).put("lastMsg",getCrowdMsgByCIId(String.valueOf(pageInfo.getList().get(i).get("cIId"))));
        }*/
        resMap.put("pageIndex", reqParam.getPage());
        resMap.put("pageSize", reqParam.getLimit());
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }
    private Map<String,Object> getCrowdMsgByCIId(String cIId){
        Map<String,Object> resMap=newsService.getCrowdMsgByCIId(cIId);
        return resMap;
    }
    /**
     * 发起群聊
     * String uId,String uIds,String cIName
     */
    @RequestMapping(value = "/addcrowdInfo",method = RequestMethod.GET)
    public Map<String,Object> addcrowdInfo(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            String cIId=UUID.randomUUID().toString().replace("-","");//群ID
            String[] cuId=reqParam.getuIds().split(",");
            params.put("cIId",cIId);
            params.put("cIName",reqParam.getcIName());
            params.put("uId",reqParam.getuId());
            params.put("cfId",UUID.randomUUID().toString().replace("-",""));
            newsService.addcrowdInfo(params);
            newsService.addcrowdfriends(params);
            for(int i=0;i<cuId.length;i++){
                params.put("uId",cuId[i]);
                params.put("cfId",UUID.randomUUID().toString().replace("-",""));
                newsService.addcrowdfriends(params);
            }
            resMap.put("code","success");
            resMap.put("msg","创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }

    /**
     * 查询群资料
     */
    @RequestMapping(value = "/getcrowdInfo",method = RequestMethod.GET)
    public Map<String,Object> getcrowdInfo(AppNewsReqParam reqParam){
        Map<String,Object> params=new HashMap<>();
        params.put("cIId",reqParam.getcIId());
        Map<String,Object> crowdInfo=newsService.getcrowdInfo(params);
        List<Map<String,Object>> crowdUserList=newsService.crowdUserList(params);
        crowdInfo.put("crowdUserList",crowdUserList);
        return crowdInfo;
    }

    /**
     * 删除分组
     */
    @RequestMapping(value = "/deleteGroup",method = RequestMethod.GET)
    public Map<String,Object> deleteGroup(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("gId",reqParam.getgId());
            params.put("uId",reqParam.getuId());
            //查询当前用户我的好友分组Id
            String gIds=newsService.getGroupGIdByuId(reqParam.getuId());
            //移动删除分组下面好友都当前用户我的好友下面
            Map<String,Object> paarms=new HashMap<>();
            params.put("gId",reqParam.getgId());
            paarms.put("gIds",gIds);
            newsService.updateGroupFir(params);
            newsService.deleteGroup(params);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","删除失败");
        }
        return resMap;
    }

    /**
     * 删除聊天
     */
    @RequestMapping(value = "/deletemsgchat",method = RequestMethod.GET)
    public Map<String,Object> deletemsgchat(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("mcId",reqParam.getMcId());
            params.put("uId",reqParam.getuId());
            params.put("status","0");
            newsService.deletemsgchat(params);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","删除失败");
        }
        return resMap;
    }

    /**
     * 删除好友
     */
    @RequestMapping(value = "/deletefriends",method = RequestMethod.GET)
    public Map<String,Object> deletefriends(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("uId",reqParam.getuId());
            params.put("fuId",reqParam.getFuId());
            newsService.deletefriends(params);
            resMap.put("code","success");
            resMap.put("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","删除失败");
        }
        return resMap;
    }

    /**
     * 查询系统消息
     */
     @RequestMapping(value = "/getSysMsgInfo",method = RequestMethod.GET)
     public Map<String,Object> getSysMsgInfo(AppNewsReqParam reqParam){
         if (reqParam.getPage()==null){
             reqParam.setPage(1);
         }
         if(reqParam.getLimit()==null){
             reqParam.setLimit(10);
         }
         Map<String,Object> params=new HashMap<>();
         params.put("uId",reqParam.getuId());
         params.put("page",reqParam.getPage());
         params.put("limit",reqParam.getLimit());
         PageInfo<Map<String,Object>> pageInfo=newsService.getSysMsgInfo(params);
         Map<String,Object> resMap=new HashMap<>();

         resMap.put("pageIndex", reqParam.getPage());
         resMap.put("pageSize", reqParam.getLimit());
         resMap.put("totalpage", pageInfo.getPages());
         resMap.put("dataList", pageInfo.getList());
         return resMap;
     }



    /**
     * 查看聊天消息
     * Integer page,Integer limit,String ConversationId,String toUser,String uId
     */
    @RequestMapping(value = "/getMsgChatListPage",method = RequestMethod.GET)
    public Map<String,Object> getMsgChatListPage(AppNewsReqParam reqParam){
        if (reqParam.getPage()==null){
            reqParam.setPage(1);
        }
        if(reqParam.getLimit()==null){
            reqParam.setLimit(10);
        }
        Map<String,Object> resMap=new HashMap<>();
        Map<String,Object> params=new HashMap<>();
        params.put("mcId",reqParam.getConversationId());
        params.put("toUser",reqParam.getToUser());
        params.put("uId",reqParam.getuId());
        params.put("page",reqParam.getPage());
        params.put("limit",reqParam.getLimit());
        PageInfo<Map<String,Object>> pageInfo=newsService.getMsgChatListfPage(params);
        List<UserParentMessage> resList=new ArrayList<>();
        for (Map<String,Object> map:pageInfo.getList()){
            UserParentMessage parentMessage=new UserParentMessage();
            parentMessage=formatterMessage(map);
            resList.add(parentMessage);
        }
        resMap.put("pageIndex", reqParam.getPage());
        resMap.put("pageSize", reqParam.getLimit());
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", resList);
        return resMap;

    }
    /**
     * 邀请成员加入群
     * String cIId,String uId,String uIds
     */
    @RequestMapping(value = "addCrowdUser",method = RequestMethod.GET)
    public Map<String,Object> addCrowdUser(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("cIId",reqParam.getcIId());
            params.put("uId",reqParam.getuIds());
            params.put("cfId",UUID.randomUUID().toString().replace("-",""));
            newsService.addcrowdfriends(params);
            resMap.put("code","success");
            resMap.put("msg","邀请成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 踢出群成员
     * String cIId,String uId,String uIds
     */
    @RequestMapping(value = "deleteCrowdUser",method = RequestMethod.GET)
    public Map<String,Object> deleteCrowdUser(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("cIId",reqParam.getcIId());
            params.put("uId",reqParam.getuIds());
            newsService.deleteCrowdUser(params);
            resMap.put("code","success");
            resMap.put("msg","移除成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }

    /**
     * 群主解散群
     * String cIId,String uId,String uIds
     */
    @RequestMapping(value = "deleteCrowdUserAll",method = RequestMethod.GET)
    public Map<String,Object> deleteCrowdUserAll(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("cIId",reqParam.getcIId());
            newsService.deleteCrowdUser(params);
            resMap.put("code","success");
            resMap.put("msg","解散成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 上传文件
     */
    @RequestMapping(value = "/uploadMsgFile",method = RequestMethod.POST)
    public Map<String,Object> uploadMsgFile(MultipartFile file){
        Map<String, Object> resMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Random random = new Random();
        InputStream is = null;
        FileOutputStream out = null;
        String savePath = null;
        //保存文件
        try {
            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                throw new Exception("上传文件为空！！！");
            }
            String tmpPath=format.format(new Date()) + "/" + String.valueOf(random.nextInt(999)) + "_" + file.getOriginalFilename();
            savePath = msgfilepath + tmpPath;
            File file1 = new File(savePath);
            File parent = file1.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file1.exists()) {
                file1.createNewFile();
            }
            is = file.getInputStream();
            out = new FileOutputStream(file1);
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            out.close();
            is.close();
            String savePath2 = httppath+"msg/files/"+tmpPath;
            String tmpVideoPath=savePath;
            resMap.put("code", "success");
            resMap.put("msg", "上传成功");
            resMap.put("data", savePath2);

        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "error：" + e.getMessage());
        } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return resMap;
    }

    /**
     * 修改消息为已读
     * String id,String cId
     */
    @RequestMapping(value = "/updateIsRead",method = RequestMethod.GET)
    public Map<String,Object> updateIsRead(AppNewsReqParam reqParam){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("mId",reqParam.getId());
            params.put("cId",reqParam.getcId());
            params.put("isRead",true);
            newsService.updateIsRead(params);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("error","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
}
