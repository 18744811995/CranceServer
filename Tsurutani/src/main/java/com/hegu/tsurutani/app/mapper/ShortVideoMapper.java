package com.hegu.tsurutani.app.mapper;

import com.hegu.tsurutani.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ShortVideoMapper {

    void saveShortVideoInfo(Video video);

    List<Map<String,Object>> getVideoPage(@Param("uId") String uId);

    List<Map<String,Object>> getGzVideoPage(@Param("uId") String uId);

    List<Map<String,Object>> getTcVideoPage(@Param("city") String city,@Param("uId") String uId);

    List<Map<String,Object>> getBarrageList(@Param("resourceId") String resourceId);

    Map<String,Object> getVideoInfoByVId(@Param("vId") String vId);

    void updateVideoCollect(@Param("vId") String vId,@Param("num") Integer num);

    void addVideocomment(@Param("vcId") String vcId, @Param("vId") String vId, @Param("uId") String uId, @Param("content") String content, @Param("updatetime") String updatetime,@Param("pcvId")String pvcId,@Param("imgpath")String imgpath);


    void updateVideoPlaycount(@Param("vId") String vId,@Param("num") Integer num);

    List<Map<String,Object>> getvideocomment(Map<String, Object> paramMap);

    List<Map<String,Object>> getChilderComment(Map<String, Object> params);

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

    Integer getVideoLove(Map<String, Object> params);

    void deleteLove(Map<String, Object> params);

    List<Map<String,Object>> getGuanZhuVideoPage(Map<String, Object> params);

    List<Map<String,Object>> getToncVideoPage(Map<String, Object> params);

    List<Map<String,Object>> getBqVideoList(Map<String, Object> params);
}
