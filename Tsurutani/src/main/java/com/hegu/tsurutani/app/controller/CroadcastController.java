package com.hegu.tsurutani.app.controller;

import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.service.CroadcastService;
import com.hegu.tsurutani.entity.AnchorInfo;
import com.hegu.tsurutani.entity.Croadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author FYH
 * 直播控制器
 */
@Controller
@RequestMapping("/api/croadcast")
public class CroadcastController {
    private Logger logger= LoggerFactory.getLogger(CroadcastController.class);
    @Autowired
    private CroadcastService croadcastService;
    @Value("${server.file.prefix}")
    private String filePrefix;
    @Value("${server.req.sources.path}")
    private String reqSourcesPath;
    /***
     * 主播资质申请
     * @param anchorInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/regAnchor",method = RequestMethod.POST)
    public Map<String,Object> reAnchor(AnchorInfo anchorInfo, MultipartFile zmfile, MultipartFile bmfile){
        Map<String,Object> resMap=new HashMap<>();
        try {
            if(StringUtils.isEmpty(anchorInfo.getuId())){
                logger.info("==================>用户ID不能为空！！！");
                throw new Exception("用户ID不能为空！！！");
            }
            if(StringUtils.isEmpty(anchorInfo.getaName())){
                logger.info("==================>姓名不能为空！！！");
                throw new Exception("姓名不能为空！！！");
            }
            if(StringUtils.isEmpty(anchorInfo.getaCarId())){
                logger.info("==================>身份证号不能为空！！！");
                throw new Exception("身份证号不能为空！！！");
            }
            if(StringUtils.isEmpty(zmfile.getOriginalFilename())){
                logger.info("==================>身份证扫描件正面不能为空！！！");
                throw new Exception("身份证扫描件正面不能为空！！！");
            }
            String zmTmp="anchor/"+new SimpleDateFormat("yyyyMMdd").format(new Date())+ String.valueOf(new Random().nextInt(999))+"_"+zmfile.getOriginalFilename();
            String zmFilePath=filePrefix+zmTmp;
            uploadFile(zmFilePath,zmfile);

            if(StringUtils.isEmpty(bmfile.getOriginalFilename())){
                logger.info("==================>身份证扫描件背面不能为空！！！");
                throw new Exception("身份证扫描件背面不能为空！！！");
            }
            String bmTmp="anchor/"+new SimpleDateFormat("yyyyMMdd").format(new Date())+ String.valueOf(new Random().nextInt(999))+"_"+bmfile.getOriginalFilename();
            String bmFilePath=filePrefix+bmTmp;
            uploadFile(bmFilePath,bmfile);

            String aId=UUID.randomUUID().toString().replace("-","");
            anchorInfo.setaStatus("0");//0审核中、1审核通过、2审核不通过
            anchorInfo.setaId(aId);
            anchorInfo.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            anchorInfo.setFilezmpath(reqSourcesPath+zmTmp);
            anchorInfo.setFilefmpath(reqSourcesPath+bmTmp);
            //入库
            logger.info("==================>开始入库");
            croadcastService.addAnchorInfo(anchorInfo);
           /* Croadcast croadcast=new Croadcast();
            croadcast.setAnchorId(aId);
            croadcast.setStatus("0");//0：未开播，1：已开播
            croadcastService.addCroadcastInfo();*/
            logger.info("==================>开始结束");
            resMap.put("code","success");
            resMap.put("msg","申请成功");
            resMap.put("data",anchorInfo);
        }catch (Exception e){
            resMap.put("code","error");
            resMap.put("msg","申请主播资质失败；error："+e.getMessage());
            resMap.put("data","");
        }
        return resMap;
    }
    private void uploadFile(String filePath,MultipartFile file) throws Exception {
       // filePrefix
        InputStream is = null;
        FileOutputStream out = null;
        try {
            File file1=new File(filePath);
            File parent = file1.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file1.exists()) {
                file1.createNewFile();
            }
            is=file.getInputStream();
            out=new FileOutputStream(file1);
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            out.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("文件保存失败");
        }finally {
            if(out!=null){
                out.close();
            }
            if(is!=null){
                is.close();
            }
        }
    }
    /**
     * 主播开播
     */
    @ResponseBody
    @RequestMapping(value = "/startcroadcast",method = RequestMethod.GET)
    public Map<String,Object> startCroadcast(String roomId,String rtmpPath){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("roomId",roomId);
            params.put("rtmpPath",rtmpPath);
            params.put("status","1");
            croadcastService.updateSattusAndrtmpPath(params);
            //拉流存储到本地
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败");
        }
        return resMap;
    }
    /**
     * 关闭直播
     */
    @ResponseBody
    @RequestMapping(value = "/closecroadcast",method = RequestMethod.GET)
    public Map<String,Object> closecroadcast(String roomId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("roomId",roomId);
            params.put("status","0");
            croadcastService.updateSattusAndrtmpPath(params);
            //拉流存储到本地
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败");
        }
        return resMap;
    }
    /**
     * 查询直播间列表
     */
    @ResponseBody
    @RequestMapping(value = "/getcroadcastList",method = RequestMethod.GET)
    public Map<String,Object> getcroadcastList(Integer page,Integer limit,String title,String userCkName,String type){
        Map<String,Object> resMap=new HashMap<>();
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }

        Map<String,Object> params=new HashMap<>();
        params.put("title",title);
        params.put("userCkName",userCkName);
        params.put("page",page);
        params.put("limit",limit);
        PageInfo<Map<String,Object>> pageInfo=croadcastService.getcroadcastList(params);
        resMap.put("pageIndex", page);
        resMap.put("pageSize", limit);
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }
    /**
     * 修改直播间标题、描述
     */
    @ResponseBody
    @RequestMapping(value = "/updateCroadcastTitleAndContent",method = RequestMethod.GET)
    public Map<String,Object> updateCroadcastTitleAndContent(String roomId,String title,String content){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("roomId",roomId);
            params.put("title",title);
            params.put("content",content);
            croadcastService.updateCroadcastTitleAndContent(params);
            resMap.put("code","success");
            resMap.put("msg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","修改失败");
        }
        return resMap;
    }

    /**
     * 根据ID查询直播间信息
     */
    @ResponseBody
    @RequestMapping(value = "/getcroadcast",method = RequestMethod.GET)
    public Map<String,Object> getcroadcast(String roomId){
        Map<String,Object> resMap=croadcastService.getCroadcast(roomId);
        return resMap;
    }
    /********************************************************************以下还未测试
     * 订阅主播
     */
    @ResponseBody
    @RequestMapping(value = "/follow",method = RequestMethod.GET)
    public Map<String,Object> follow(String aId,String uId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("aId",aId);
            params.put("uId",uId);
            //查询当前用户是否已关注
            Integer num=croadcastService.getFollow(params);
            if(num>0){
                //已关注，取消关注
                croadcastService.deleteFollow(params);
            }else{
                //未关注，关注
                croadcastService.addFollow(params);
            }
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }

    /**
     * 查询房间在线用户
     */
    @ResponseBody
    @RequestMapping(value = "/getRoomUserList")
    public Map<String,Object> getRoomUserList(Integer page,Integer limit,String roomId){
        Map<String,Object> resMap=new HashMap<>();
        Map<String,Object> params=new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        params.put("roomId",roomId);
        PageInfo<Map<String,Object>> pageInfo=croadcastService.getRoomUserList(params);
        resMap.put("pageIndex",pageInfo.getPageNum());
        resMap.put("pageSize",pageInfo.getSize());
        resMap.put("total",pageInfo.getTotal());
        resMap.put("totalPage",pageInfo.getPages());
        resMap.put("dataList",pageInfo.getList());
        return resMap;
    }

    /**
     * 直播间分享
     */
    @ResponseBody
    @RequestMapping(value = "/croadcastfx",method = RequestMethod.GET)
    public Map<String,Object> croadcastfx(String roomId,String uId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("roomId",roomId);
            params.put("uId",uId);
            //查询直播间分享次数
            Integer num=croadcastService.getcroadcastfxCount(roomId);
            num++;
            //插入分享信息
            croadcastService.addcroadcastfxInfo(params);
            resMap.put("code","success");
            resMap.put("msg","操作成功");
            resMap.put("data",num);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
            resMap.put("data",0);
        }
        return resMap;
    }

    /**
     * 查询礼物信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/getGiftList",method = RequestMethod.GET)
    public List<Map<String,Object>> getGiftList(){
        Map<String,Object> params=new HashMap<>();
        List<Map<String,Object>> resList=croadcastService.getGiftList(params);
        return resList;
    }

    /**
     * 直播间用户送礼物
     */
    @ResponseBody
    @RequestMapping(value = "/sendGift",method = RequestMethod.GET)
    public Map<String,Object> sendGift(String gId,String count,String price,String sendUId,String uId,String userkey){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,String> params=new HashMap<>();
            params.put("sgId",UUID.randomUUID().toString().replace("-",""));
            params.put("gId",gId);
            params.put("count",count);
            params.put("price",price);
            params.put("sendUId",sendUId);
            params.put("uId",uId);
            params.put("createtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //保存赠送记录
            croadcastService.saveSendGift(params);
            //扣除赠送用户鹤谷币

            //增加接受礼物鹤谷币

            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","error:"+e.getMessage());
        }
        return resMap;
    }
}
