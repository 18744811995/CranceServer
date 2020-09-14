package com.hegu.tsurutani.websocket.endpoint;

import com.alibaba.fastjson.JSON;
import com.hegu.tsurutani.app.service.NewsService;
import com.hegu.tsurutani.app.serviceimpl.NewsServiceImpl;
import com.hegu.tsurutani.entity.UserChilderMessage;
import com.hegu.tsurutani.entity.UserMsg;
import com.hegu.tsurutani.entity.UserParentMessage;
import com.hegu.tsurutani.utils.SpringCtxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ws/msg/{userId}")
public class MsgEndpointExporter {
    private Logger log= LoggerFactory.getLogger(MsgEndpointExporter.class);
    private Session session; //建立连接的会话
    private String userId; //当前连接用户id   路径参数
    private NewsServiceImpl newsService;
    public MsgEndpointExporter(){

    }
    public MsgEndpointExporter(String init){
        this.newsService= (NewsServiceImpl) SpringCtxUtils.getBeanById("newsService");
    }
    /**
     * 存放存活的Session集合(map保存)
     */
   private static ConcurrentHashMap<String , MsgEndpointExporter> livingSession = new ConcurrentHashMap<String, MsgEndpointExporter>();

    /**
     *  建立连接的回调
     *  session 建立连接的会话
     *  userId 当前连接用户id   路径参数
     */
    @OnOpen
    public void onOpen(Session session,  @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        this.newsService= (NewsServiceImpl) SpringCtxUtils.getBeanById("newsService");
        livingSession.put(userId, this);
        Map<String,Object> params=new HashMap<>();
        params.put("userId",userId);
        log.info("----[ WebSocket ]---- 用户id为 : {} 的用户进入WebSocket连接 ! 当前在线人数为 : {} 人 !--------"+livingSession.size(),userId);
    }

    /**
     *  关闭连接的回调
     *  移除用户在线状态
     */
    @OnClose
    public void onClose(){
        livingSession.remove(userId);
        log.info("----[ WebSocket ]---- 用户id为 : {} 的用户退出WebSocket连接 ! 当前在线人数为 : {} 人 !--------"+livingSession.size());
    }

    @OnMessage
    public void onMessage(String message, Session session,  @PathParam("userId") String userId) {
        log.info("--------收到用户id为 : {} 的用户发送的消息 ! 消息内容为 : ------------------"+userId+":"+message);
        //UserMsg userMsg= JSON.parseObject(message,UserMsg.class);
        UserParentMessage userMsg= JSON.parseObject(message,UserParentMessage.class);
        if(StringUtils.isEmpty(userMsg.getIsGroup())){
            log.info("===============>userId:"+userId+"；发送消息类型为空");
        }
        callMsg(userMsg.getFormUser(),userMsg.getMessage().getId());
        if(userMsg.getMsgType()==101||userMsg.getMsgType()==102){
            if(!userMsg.getIsGroup()){
                //给好友发消息
                //userMsg.getMessage().setId(UUID.randomUUID().toString().replace("-",""));
                newsService.addmsgchatmsg(userMsg);
                //查询当前力聊天下面的会话列表
                List<String> cIds=newsService.getCIdBymcId(userMsg.getConversationId());
                //保存消息关系
                for(String cId:cIds){
                    newsService.addmsgchilder(cId,userMsg.getMessage().getId(),false,"0");
                }
                sendMessageByuserId(userMsg.getToUser(),message);
            }else if(userMsg.getIsGroup()){
                //发送群消息
                if(StringUtils.isEmpty(userMsg.getToUser())){
                    log.info("============>userId:"+userId+"；群编号为空");
                }
                //插入发送消息
                Map<String,Object> params=new HashMap<>();
                params.put("cIId",userMsg.getToUser());
                List<Map<String,Object>> mcUserList=newsService.crowdUserList(params);
                for (int i=0;i<mcUserList.size();i++){
                    Map<String,Object> map=mcUserList.get(i);
                    userMsg.setToUser(String.valueOf(map.get("uId")));
                    String msg=JSON.toJSONString(userMsg);
                    newsService.addmsgchatmsg(userMsg);
                    sendMessageByuserId(userMsg.getToUser(),msg);
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("----------------WebSocket发生错误----------------");
        log.info(error.getStackTrace() + "");
    }

    /**
     * 发送消息回调
     */
    public void callMsg(String toUser,String msgId){
        long time=new Date().getTime();
        UserParentMessage parentMessage=new UserParentMessage();
        parentMessage.setToUser(toUser);
        //parentMessage.setFormIcon();
        parentMessage.setFormName("鹤谷");
        parentMessage.setFormUser("123456");
        parentMessage.setIsGroup(false);
        parentMessage.setMsgTime(time);
        parentMessage.setMsgType(10);
        UserChilderMessage childerMessage=new UserChilderMessage();
        childerMessage.setId(UUID.randomUUID().toString().replace("-",""));
        //childerMessage.setDuration(Integer.valueOf(String.valueOf( resMap.get("duration"))));
        //childerMessage.setExtra(String.valueOf(resMap.get("extra")));
        childerMessage.setFormUser("123456");
        //childerMessage.setHeight(Integer.valueOf(String.valueOf(resMap.get("height"))));
        childerMessage.setMsgTime(time);
        childerMessage.setMsgType(10);
        childerMessage.setRead(false);
        //childerMessage.setRead(Boolean.valueOf(String.valueOf(resMap.get("isRead"))));
        childerMessage.setSelf(false);
        childerMessage.setStatus("0");
        childerMessage.setText(msgId);
        //childerMessage.setWidth(Integer.valueOf(String.valueOf(resMap.get("width"))));
        parentMessage.setMessage(childerMessage);
        sendMessageByuserId(userId,JSON.toJSONString(parentMessage));
    }

    /**
     *  根据userId发送给用户
     * @param userId
     */
    public void sendMessageByuserId(String userId,String message) {
        log.info("==========>发送消息");
        log.info("==========>发送消息给用户："+userId);
        livingSession.forEach((sessionId, session) -> {
            //发给指定的接收用户
            if (userId.equals(session.userId)) {
                log.info("==========>发送消息:"+message);
                sendMessageBySession(session.session, message);
            }
        });
    }

    /**
     *  推送消息
     * @param session
     * @param message
     */
    public void sendMessageBySession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.info("----[ WebSocket ]------发送消息失败---------");
            e.printStackTrace();
        }
    }

    /**
     *  发送群消息
     * @param message
     */
    public void sendMessageOnline(String message) {
        //List<Map<String,Object>> roomIdMap=croadcastMapper.getCroadcastUserInfo(roomId);
        for(int i=0;i<livingSession.size();i++){
            /*MsgEndpointExporter session=livingSession.get(String.valueOf(roomIdMap.get(i).get("uId")));
            if(session.session.isOpen()){
                sendMessageBySession(session.session, message);
            }*/
        }
    }

    public void sendSysMsg(UserParentMessage message,String fId) {
        log.info("==========>当前在线用户数量："+livingSession.size());
        log.info("==========>发送消息");
        log.info("==========>发送消息给用户："+message.getToUser());
        //入库
        newsService.saveMsg(message);
        newsService.saveFriendsMsg(fId,message.getMessage().getId());
        livingSession.forEach((sessionId, session) -> {
            //发给指定的接收用户
            if (message.getToUser().equals(session.userId)) {
                log.info("==========>发送消息:"+JSON.toJSONString(message));
                sendMessageBySession(session.session,JSON.toJSONString(message));
            }
        });
    }
}
