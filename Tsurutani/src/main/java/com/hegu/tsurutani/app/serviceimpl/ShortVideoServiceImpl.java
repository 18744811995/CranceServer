package com.hegu.tsurutani.app.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.mapper.ShortVideoMapper;
import com.hegu.tsurutani.app.service.ShortVideoService;
import com.hegu.tsurutani.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShortVideoServiceImpl implements ShortVideoService {

    @Autowired
    private ShortVideoMapper shortVideoMapper;

    @Override
    public void saveShortVideoInfo(Video video) {
        shortVideoMapper.saveShortVideoInfo(video);
    }

    @Override
    public PageInfo<Map<String, Object>> getVideoPage(Integer pageIndex, Integer pageSize,String vType,String uId,String city) {
        PageHelper.offsetPage((pageIndex-1)*pageSize,pageSize);
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> dataList=null;
        if("1".equals(vType)){
            //查询推荐视频列表
            dataList=shortVideoMapper.getVideoPage(uId);
        }else if("2".equals(vType)){
            //查询关注用户视屏列表
            dataList=shortVideoMapper.getGzVideoPage(uId);
        }else if(vType.equals("3")){
            //查询同城视屏列表
            dataList=shortVideoMapper.getTcVideoPage(city,uId);
        }

        pageInfo=new PageInfo<>(dataList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> getBarrageList(String resourceId, Integer pageIndex, Integer pageSize) {
        PageHelper.offsetPage((pageIndex-1)*pageSize,pageSize);
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> dataList=shortVideoMapper.getBarrageList(resourceId);
        pageInfo=new PageInfo<>(dataList);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getVideoInfoByVId(String vId) {
        return shortVideoMapper.getVideoInfoByVId(vId);
    }

    @Override
    public void updateVideoCollect(String vId, Integer num) {
        shortVideoMapper.updateVideoCollect(vId,num);
    }

    @Override
    public void addVideocomment(String vcId, String vId, String uId, String content, String updatetime,String pvcId,String imgpath) {
        shortVideoMapper.addVideocomment(vcId,vId,uId,content,updatetime,pvcId,imgpath);
    }

    @Override
    public void updateVideoPlaycount(String vId, Integer num) {
        shortVideoMapper.updateVideoPlaycount(vId,num);
    }

    @Override
    public PageInfo<Map<String, Object>> getvideocomment(Map<String, Object> paramMap) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(paramMap.get("page")))-1)*Integer.valueOf(String.valueOf(paramMap.get("limit"))),Integer.valueOf(String.valueOf(paramMap.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=shortVideoMapper.getvideocomment(paramMap);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> getChilderComment(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=shortVideoMapper.getChilderComment(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public Integer getChilderCommentCount(String pvcId) {
        return shortVideoMapper.getChilderCommentCount(pvcId);
    }

    @Override
    public Integer getdzcount(Map<String, Object> paramsMap) {
        return shortVideoMapper.getdzcount(paramsMap);
    }

    @Override
    public void deletedzbyuIdandcId(Map<String, Object> paramsMap) {
        shortVideoMapper.deletedzbyuIdandcId(paramsMap);
    }

    @Override
    public void updatedzbyuIdandcId(Map<String, Object> paramsMap) {
        shortVideoMapper.updatedzbyuIdandcId(paramsMap);
    }

    @Override
    public void adddzbyuIdandcId(Map<String, Object> paramsMap) {
        shortVideoMapper.adddzbyuIdandcId(paramsMap);
    }

    @Override
    public void videofx(Map<String, Object> params) {
        shortVideoMapper.videofx(params);
    }

    @Override
    public Integer getVideofenxCount(String vId) {
        return shortVideoMapper.getVideofenxCount(vId);
    }

    @Override
    public void addFollow(Map<String, Object> params) {
        shortVideoMapper.addFollow(params);
    }

    @Override
    public void addVideogGift(Map<String, Object> params) {
        shortVideoMapper.addVideogGift(params);
    }

    @Override
    public void love(Map<String, Object> params) {
        Integer num=shortVideoMapper.getVideoLove(params);
        if(num>0){
            shortVideoMapper.deleteLove(params);
        }else{
            shortVideoMapper.love(params);
        }
    }

    @Override
    public PageInfo<Map<String, Object>> getGuanZhuVideoPage(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=shortVideoMapper.getGuanZhuVideoPage(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> getToncVideoPage(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=shortVideoMapper.getToncVideoPage(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> getBqVideoList(Map<String, Object> params) {
        PageHelper.offsetPage((Integer.valueOf(String.valueOf(params.get("page")))-1)*Integer.valueOf(String.valueOf(params.get("limit"))),Integer.valueOf(String.valueOf(params.get("limit"))));
        PageInfo<Map<String,Object>> pageInfo=null;
        List<Map<String,Object>> resList=shortVideoMapper.getBqVideoList(params);
        pageInfo=new PageInfo<>(resList);
        return pageInfo;
    }
}
