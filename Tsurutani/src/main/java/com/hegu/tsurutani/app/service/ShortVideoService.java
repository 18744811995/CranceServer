package com.hegu.tsurutani.app.service;


import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.entity.Video;

import java.util.Map;

public interface ShortVideoService {
    public void saveShortVideoInfo(Video video);

    PageInfo<Map<String,Object>> getVideoPage(Integer pageIndex, Integer pageSize,String vType,String uId,String city);

    PageInfo<Map<String,Object>> getBarrageList(String resourceId, Integer pageIndex, Integer pageSize);

    Map<String,Object> getVideoInfoByVId(String vId);

    void updateVideoCollect(String vId, Integer num);

    void addVideocomment(String vcId, String vId, String uId, String content, String updatetime,String pvcId,String imgpath);

    void updateVideoPlaycount(String vId, Integer num);

    PageInfo<Map<String,Object>> getvideocomment(Map<String, Object> paramMap);

    PageInfo<Map<String,Object>> getChilderComment(Map<String, Object> params);

    Integer getChilderCommentCount(String pvcId);

    Integer getdzcount(Map<String, Object> paramsMap);

    void deletedzbyuIdandcId(Map<String, Object> paramsMap);

    void updatedzbyuIdandcId(Map<String, Object> paramsMap);

    void adddzbyuIdandcId(Map<String, Object> paramsMap);

    void videofx(Map<String, Object> params);

    Integer getVideofenxCount(String vId);

    void addFollow(Map<String, Object> params);

    void addVideogGift(Map<String, Object> params);

    void love(Map<String, Object> params);

    PageInfo<Map<String,Object>> getGuanZhuVideoPage(Map<String, Object> params);

    PageInfo<Map<String,Object>> getToncVideoPage(Map<String, Object> params);

    PageInfo<Map<String,Object>> getBqVideoList(Map<String, Object> params);
}
