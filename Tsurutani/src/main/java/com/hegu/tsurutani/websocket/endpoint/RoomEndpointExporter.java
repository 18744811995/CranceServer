package com.hegu.tsurutani.websocket.endpoint;

import com.alibaba.fastjson.JSON;
import com.hegu.tsurutani.app.mapper.CroadcastMapper;
import com.hegu.tsurutani.entity.RoomMsg;
import com.hegu.tsurutani.utils.SpringCtxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ws/croadcastroom/{roomId}/{userId}")
public class RoomEndpointExporter {
    private Logger log= LoggerFactory.getLogger(RoomEndpointExporter.class);
    private Session session; //建立连接的会话
    private String userId; //当前连接用户id   路径参数
    private String roomId;//房间编号
    private  CroadcastMapper croadcastMapper;
    /**
     * 存放存活的Session集合(map保存)
     */
    private static ConcurrentHashMap<String , RoomEndpointExporter> livingSession = new ConcurrentHashMap<String, RoomEndpointExporter>();

    /**
     *  建立连接的回调
     *  session 建立连接的会话
     *  userId 当前连接用户id   路径参数
     */
    @OnOpen
    public void onOpen(Session session,  @PathParam("userId") String userId,@PathParam("roomId") String roomId){
        this.session = session;
        this.userId = userId;
        this.roomId=roomId;
        this.croadcastMapper= (CroadcastMapper) SpringCtxUtils.getBeanById("croadcastMapper");
        livingSession.put(userId, this);
        Map<String,Object> params=new HashMap<>();
        params.put("userId",userId);
        params.put("roomId",roomId);
        this.croadcastMapper.addCroadcastUser(params);//存储到数据库
        Integer count=croadcastMapper.getCroadcastCount(roomId);//查询在线人数
        log.info("----[ WebSocket ]---- 用户id为 : {} 的用户进入WebSocket连接 ! 当前在线人数为 : {} 人 !--------",userId,count);
    }

    /**
     *  关闭连接的回调
     *  移除用户在线状态
     */
    @OnClose
    public void onClose(){
        livingSession.remove(userId);
        Map<String,Object> params=new HashMap<>();
        params.put("userId",userId);
        params.put("roomId",roomId);
        this.croadcastMapper.deleteCroadcastUser(params);//存储到数据库
        Integer count=croadcastMapper.getCroadcastCount(roomId);//查询在线人数
        log.info("----[ WebSocket ]---- 用户id为 : {} 的用户退出WebSocket连接 ! 当前在线人数为 : {} 人 !--------",userId,count);
    }

    @OnMessage
    public void onMessage(String message, Session session,  @PathParam("userId") String userId) {
        log.info("--------收到用户id为 : {} 的用户发送的消息 ! 消息内容为 : ------------------"+userId+":"+message);
        RoomMsg roomMsg= JSON.parseObject(message,RoomMsg.class);
        //保存弹幕信息
        roomMsg.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        roomMsg.setMsgId(UUID.randomUUID().toString().replace("-",""));
        this.croadcastMapper.addCroadcastRoomMsg(roomMsg);
        sendMessageOnline(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("----------------WebSocket发生错误----------------");
        log.info(error.getStackTrace() + "");
    }

    /**
     *  根据userId发送给用户
     * @param userId
     * @param message
     */
    public void sendMessageById(String userId, String message) {
        livingSession.forEach((sessionId, session) -> {
            //发给指定的接收用户
            if (userId.equals(session.userId)) {
                sendMessageBySession(session.session, message);
            }
        });
    }

    /**
     *  根据Session发送消息给用户
     * @param session
     * @param message
     */
    public void sendMessageBySession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.info("----[ WebSocket ]------给用户发送消息失败---------");
            e.printStackTrace();
        }
    }

    /**
     *  给发送弹幕所在房间所有用户发送弹幕消息
     * @param message
     */
    public void sendMessageOnline(String message) {
        List<Map<String,Object>> roomIdMap=croadcastMapper.getCroadcastUserInfo(roomId);
        for(int i=0;i<livingSession.size();i++){
            RoomEndpointExporter session=livingSession.get(String.valueOf(roomIdMap.get(i).get("uId")));
            if(session.session.isOpen()){
                sendMessageBySession(session.session, message);
            }
        }
    }
}
