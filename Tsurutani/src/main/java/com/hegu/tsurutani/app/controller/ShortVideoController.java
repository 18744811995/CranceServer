package com.hegu.tsurutani.app.controller;

import ch.qos.logback.core.util.FileUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.hegu.tsurutani.app.serviceimpl.ShortVideoServiceImpl;
import com.hegu.tsurutani.callable.VideoCallable;
import com.hegu.tsurutani.entity.Video;
import com.hegu.tsurutani.handler.NonStaticResourceHttpRequestHandler;
import com.hegu.tsurutani.utils.ExcelUtil;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author FYHup
 * 短视频控制器
 */
@Controller
@RequestMapping("/api/video")
public class ShortVideoController {
    private Logger logger = LoggerFactory.getLogger(ShortVideoController.class);
    @Autowired
    private ShortVideoServiceImpl ShortVideoService;
    @Value("${server.video.path}")
    private String videoPath;
    @Value("${server.video.img.path}")
    private String videoImgPath;
    @Value("${server.video.rtmp.path}")
    private String rtmpPath;
    @Value("${server.req.sources.path}")
    private String reqSourcesPath;

    /**
     * 查询当前页视频
     *
     * @param page
     * @param pageSize
     * @param vType     视频列表类型 1：默认推荐、2：关注、3.同城
     * @return
     */
    @GetMapping("/getVideoPage")
    @ResponseBody
    public Map<String, Object> getVideoPage(Integer page, Integer pageSize, String vType, String uId, String city) throws Exception {
        if (null == page) {
            page = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        if (StringUtils.isEmpty(vType)) {
            logger.info("=============>视频类型为空");
            throw new Exception("视频类型不能为空");
        }
        if (vType.equals("2")) {
            if (StringUtils.isEmpty(uId)) {
                logger.info("=============>当前用户ID为空");
                throw new Exception("当前用户ID不能为空");
            }
        } else if (vType.equals("3")) {
            if (StringUtils.isEmpty(city)) {
                logger.info("=============>用户所在城市为空");
                throw new Exception("用户所在城市不能为空");
            }
        }
        PageInfo<Map<String, Object>> pageInfo = ShortVideoService.getVideoPage(page, pageSize, vType, uId, city);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("pageIndex", page);
        resMap.put("pageSize", pageSize);
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }
    /**
     * 查询混合视频列表
     */
    @ResponseBody
    @RequestMapping(value = "/getVideoAll",method = RequestMethod.GET)
    public Map<String,Object> getVideoAll(Integer page,Integer limit,String title,String uckName,String city,String uId){
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }
        Map<String,Object> resMap=new HashMap<>();
        Integer gzlimit=limit/2;//关注视频条数
        Integer tclimit=limit/2;//同城视频条数
        Map<String,Object> params=new HashMap<>();
        params.put("page",page);
        params.put("limit",gzlimit);
        params.put("title",title);
        params.put("uckName",uckName);
        params.put("city",city);
        params.put("uId",uId);
        PageInfo<Map<String,Object>> gzpageInfo=ShortVideoService.getGuanZhuVideoPage(params);//查询关注视频列表
        params.put("limit",tclimit);
        PageInfo<Map<String,Object>> tcpageInfo=ShortVideoService.getToncVideoPage(params);//查询同城视屏列表
        List<Map<String,Object>> resList=new ArrayList<>();
        for(int i=0;i<gzpageInfo.getList().size();i++){
            Map<String,Object> map=gzpageInfo.getList().get(i);
            resList.add(map);
        }
        for(int i=0;i<tcpageInfo.getList().size();i++){
            Map<String,Object> map=tcpageInfo.getList().get(i);
            resList.add(map);
        }
        //自动补全
        Integer num=gzpageInfo.getList().size()+tcpageInfo.getList().size();
        Integer bqlimit=10-num;
        params.put("limit",bqlimit);
        PageInfo<Map<String,Object>> bqpageInfo=ShortVideoService.getBqVideoList(params);
        List<Map<String,Object>> bdList=resList;
        for(int i=0;i<bqpageInfo.getList().size();i++){
            Map<String,Object> map=bqpageInfo.getList().get(i);
            boolean flag=true;
            for(int j=0;j<bdList.size();j++){
                if(String.valueOf(bdList.get(j).get("vId")).equals(String.valueOf(map.get("vId")))){
                    flag=false;
                }
            }
            if (flag){
                resList.add(map);
            }
        }
        resMap.put("pageIndex", page);
        resMap.put("pageSize", limit);
        resMap.put("totalpage", tcpageInfo.getPages());
        resMap.put("dataList", resList);
        return resMap;
    }
    /**
     * 上传短视屏
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadFile(MultipartFile file, Video video) throws IOException {
        Map<String, Object> resMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            savePath = videoPath + tmpPath;
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
            String savePath2 = reqSourcesPath+"video/"+tmpPath;
            String tmpVideoPath=savePath;
            resMap.put("code", "000000");
            resMap.put("msg", "上传成功");
            resMap.put("data", savePath2);
            //异步入库
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String videoImg = createVideoImg(tmpVideoPath, file.getOriginalFilename());
                    String vId=UUID.randomUUID().toString().replace("-","");
                    video.setStatus("0");
                    video.setvId(vId);
                    video.setVpath(savePath2);
                    video.setUploadtime(format1.format(new Date()));
                    video.setVideoImgPath(videoImg);
                    ShortVideoService.saveShortVideoInfo(video);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "000005");
            resMap.put("msg", "上传文件异常：" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return resMap;
    }

    /**
     * 截取视频的帧，形成视频的预览图片
     *
     * @param videoName 视频名
     */
    public String createVideoImg(String videoUrl, String videoName) {
        FFmpegFrameGrabber ff = null;
        Random random = new Random();
        String temPath= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + String.valueOf(random.nextInt(999)) + "_" + videoName.substring(0, videoName.lastIndexOf(".")) + ".jpg";
        String newImgName = videoImgPath+temPath;
        String resPath=reqSourcesPath+"videoimg/"+temPath;
        File targetFile = new File(newImgName);
        try {
            File parent = targetFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            ff = new FFmpegFrameGrabber(new File(videoUrl));
            ff.start();
            int lenght = ff.getLengthInFrames();
            int i = 0;
            Frame f = null;
            while (i < lenght) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                f = ff.grabFrame();
                if ((i > 6) && (f.image != null)) {
                    break;
                }
                i++;
            }
            // 截取的帧图片
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(f);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();
            // 对截图进行等比例缩放(缩略图)
            int width = 480;
            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(thumbnailImage, "jpg", targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ff != null) {
                    ff.stop();
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return resPath;
    }

    /**
     * 播放短视屏
     * 上传媒体流到FFmpeg返回rtmp地址
     *
     * @param vId
     * @throws IOException
     * @throws ServletException
     */
    @ResponseBody
    @RequestMapping("/getrtmppath")
    public Map<String, Object> downVideo(String vId, String filePath) throws
            IOException, ServletException, ExecutionException, InterruptedException {
        Map<String, Object> resMap = new HashMap<>();
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                logger.info("===============>视频文件不存在！！！");
                throw new Exception("视频文件不存在！！！");
            }
            String resrtmppath = rtmpPath + vId;
            logger.info("=================>vId:"+vId);
            logger.info("=================>rtmpPath:"+rtmpPath);
            logger.info("=================>resrtmppath:"+resrtmppath);
            ExecutorService pool = Executors.newFixedThreadPool(10);
            //VideoCallable callable=new VideoCallable(vId, filePath, rtmpPath);
            //FutureTask callableTask=new FutureTask(callable);
            Future<String> future = pool.submit(new VideoCallable(vId, filePath, rtmpPath));
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    //修改播放量
                    Map<String, Object> videoInfo = ShortVideoService.getVideoInfoByVId(vId);
                    if (videoInfo != null && videoInfo.size() > 0) {
                        Integer num = 0;
                        if (StringUtils.isEmpty(videoInfo.get("playcount"))) {
                            num = 1;
                        } else {
                            num = Integer.valueOf(String.valueOf(videoInfo.get("playcount"))) + 1;
                        }
                        ShortVideoService.updateVideoPlaycount(vId,num);
                    }
                }
            });
            pool.shutdown();
            resMap.put("code", "success");
            resMap.put("msg", "调用成功");
            resMap.put("data", resrtmppath);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "调用失败,error:" + e.getMessage());
            resMap.put("data", "");
        }
        return resMap;
    }

    /**
     * 查询短视频弹幕列表
     *
     * @param vId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBarrageList",method = RequestMethod.GET)
    public Map<String, Object> getBarrageList(String vId, Integer pageIndex, Integer pageSize) {
        Map<String, Object> resMap = new HashMap<>();
        try {
            if (null == pageIndex) {
                pageIndex = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Map<String, Object>> pageInfo = ShortVideoService.getBarrageList(vId, pageIndex, pageSize);
            resMap.put("code", "success");
            resMap.put("msg", "调用成功");
            resMap.put("totalpage", pageInfo.getPages());
            resMap.put("list", pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "调用失败,error:" + e.getMessage());
            resMap.put("totalpage", 0);
            resMap.put("list", new ArrayList<Map<String, Object>>());
        }
        return resMap;
    }

    /**
     * 修改视屏收藏数量
     *
     * @param vId         视频Id
     * @param collectType 收藏类型  1：收藏，2：取消收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateVideoCollect",method = RequestMethod.GET)
    public Map<String, Object> updateVideoCollect(String vId, String collectType) {
        Map<String, Object> resMap = new HashMap<>();
        try {
            if (StringUtils.isEmpty(vId)) {
                logger.info("==========>视频Id为空");
                throw new Exception("视频Id不能为空");
            }
            if (StringUtils.isEmpty(collectType)) {
                logger.info("==========>收藏类型为空");
                throw new Exception("收藏类型不能为空");
            }
            Map<String, Object> videoInfo = ShortVideoService.getVideoInfoByVId(vId);
            if (videoInfo != null && videoInfo.size() > 0) {
                Integer num = 0;
                if (StringUtils.isEmpty(videoInfo.get("clickcount"))) {
                    num = 1;
                } else {
                    if (collectType.equals("1")) {
                        num = Integer.valueOf(String.valueOf(videoInfo.get("clickcount"))) + 1;
                    } else if (collectType.equals("2")) {
                        num = Integer.valueOf(String.valueOf(videoInfo.get("clickcount"))) - 1;
                    }
                }
                ShortVideoService.updateVideoCollect(vId, num);
                resMap.put("code", "success");
                resMap.put("msg", "调用成功");
                resMap.put("data", "");
            } else {
                logger.info("vId对应视频不存在");
                throw new Exception("vId对应视频不存在！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "调用失败，error:" + e.getMessage());
            resMap.put("data", "");
        }
        return resMap;
    }

    /**
     * 新增弹幕信息
     *
     * @param vId
     * @param uId
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addVideocomment",method = RequestMethod.GET)
    public Map<String, Object> addVideocomment(String vId, String uId, String content,String pvcId,String imgpath) {
        Map<String, Object> resMap = new HashMap<>();
        try {
            if (StringUtils.isEmpty(vId)) {
                logger.info("==========>视频Id为空");
                throw new Exception("视频Id不能为空");
            }
            if (StringUtils.isEmpty(uId)) {
                logger.info("==========>评论用户Id为空");
                throw new Exception("评论用户Id不能为空");
            }
            if (StringUtils.isEmpty(content)) {
                logger.info("==========>评论内容为空");
                throw new Exception("评论内容不能为空");
            }
            if(StringUtils.isEmpty(pvcId)){
                logger.info("==========>被评论Id不能为空");
                throw new Exception("被评论Id不能为空");
            }
            if (StringUtils.isEmpty(imgpath)){
                logger.info("==========>用户头像不能为空");
                throw new Exception("用户头像不能为空");
            }
            String vcId = UUID.randomUUID().toString().replace("-", "");
            String updatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ShortVideoService.addVideocomment(vcId, vId, uId, content, updatetime,pvcId,imgpath);
            resMap.put("code", "success");
            resMap.put("msg", "调用成功");
            resMap.put("data", "");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code", "error");
            resMap.put("msg", "调用失败，error:" + e.getMessage());
            resMap.put("data", "");
        }
        return resMap;
    }
    /**
     * 查询评论列表
     */
    @ResponseBody
    @RequestMapping(value = "/getvideocomment",method = RequestMethod.GET)
    public Map<String,Object> getvideocomment(Integer page,Integer limit,String vId){
        Map<String,Object> resMap=new HashMap<>();
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("page",page);
        paramMap.put("limit",limit);
        paramMap.put("vId",vId);
        PageInfo<Map<String,Object>> pageInfo=ShortVideoService.getvideocomment(paramMap);
        List<Map<String,Object>> resList=pageInfo.getList();
        for(int i=0;i<resList.size();i++){
            resList.get(i).put("childer",getChilderComment(1,3,String.valueOf(resList.get(i).get("vcId"))).get("dataList"));
            resList.get(i).put("childercount",getChilderCommentCount(String.valueOf(resList.get(i).get("vcId"))));
        }
        resMap.put("pageIndex", page);
        resMap.put("pageSize", limit);
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", resList);
        return resMap;
    }
    private Map<String,Object> getChilderComment(Integer page,Integer limit,String pvcId){
        Map<String,Object> resMap=new HashMap<>();
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        params.put("pvcId",pvcId);
        PageInfo<Map<String,Object>> pageInfo=ShortVideoService.getChilderComment(params);
        resMap.put("pageIndex", page);
        resMap.put("pageSize", limit);
        resMap.put("totalpage", pageInfo.getPages());
        resMap.put("dataList", pageInfo.getList());
        return resMap;
    }
    private Integer getChilderCommentCount(String pvcId){
        Integer num=ShortVideoService.getChilderCommentCount(pvcId);
        return num;
    }
    /**
     * 评论点赞\踩
     */
    @ResponseBody
    @RequestMapping(value = "/commentdz",method = RequestMethod.GET)
    public Map<String,Object> commentdz(String cId,String uId,String type){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> paramsMap=new HashMap<>();
            paramsMap.put("cId",cId);
            paramsMap.put("type",1);
            Integer dzcount=ShortVideoService.getdzcount(paramsMap);//查询点赞总数
            paramsMap.put("type",2);
            Integer ccount=ShortVideoService.getdzcount(paramsMap);//查询踩总数

            paramsMap.put("uId",uId);
            paramsMap.put("type",1);
            Integer user_dzcount=ShortVideoService.getdzcount(paramsMap);//查询当前用户是否点赞过
            paramsMap.put("type",2);
            Integer user_ccount=ShortVideoService.getdzcount(paramsMap);//查询当前用户是否踩过
            if(type.equals("1")){
                //点赞
                if(user_dzcount>0){
                    //当前用户已经点赞过，删除，点赞数量减一
                    ShortVideoService.deletedzbyuIdandcId(paramsMap);
                    dzcount=dzcount-1;
                }else if(user_ccount>0){
                    //踩过，修改类型为点赞，点赞数量加一，踩减一
                    paramsMap.put("type",1);
                    ShortVideoService.updatedzbyuIdandcId(paramsMap);
                    dzcount=dzcount+1;
                    ccount=ccount-1;
                }else if(user_dzcount<=0&&user_ccount<=0){
                    //点赞，增加，点赞数量加一
                    paramsMap.put("type",1);
                    paramsMap.put("dzId",UUID.randomUUID().toString().replace("-",""));
                    ShortVideoService.adddzbyuIdandcId(paramsMap);
                    dzcount=dzcount+1;
                }
            }else if(type.equals("2")){
                //踩
                if(dzcount>0){
                    //当前用户已经点赞过，修改，点赞数量减一，踩加一
                    paramsMap.put("type",2);
                    ShortVideoService.updatedzbyuIdandcId(paramsMap);
                    dzcount=dzcount-1;
                    ccount=ccount+1;
                }else if(ccount>0){
                    //踩过，删除，踩减一
                    ShortVideoService.deletedzbyuIdandcId(paramsMap);
                    ccount=dzcount-1;
                }else if(dzcount<=0&&ccount<=0){
                    //踩，增加，踩加一
                    paramsMap.put("type",2);
                    paramsMap.put("dzId",UUID.randomUUID().toString().replace("-",""));
                    ShortVideoService.adddzbyuIdandcId(paramsMap);
                    ccount=ccount+1;
                }
            }
            resMap.put("code","success");
            resMap.put("msg","操作成功");
            resMap.put("ccount",ccount);
            resMap.put("dzcount",dzcount);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败,error:"+e.getMessage());
            resMap.put("ccount",0);
            resMap.put("dzcount",0);
        }
        return resMap;
    }
    /**
     * 分享视频
     */
    @ResponseBody
    @RequestMapping(value = "/videofx",method = RequestMethod.GET)
    public Map<String,Object> videofx(String uId,String vId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("fxId",UUID.randomUUID().toString().replace("-",""));
            params.put("uId",uId);
            params.put("vId",vId);
            params.put("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ShortVideoService.videofx(params);//新增一条分享信息
            Integer num=ShortVideoService.getVideofenxCount(vId);
            resMap.put("code","success");
            resMap.put("msg","操作成功");
            resMap.put("fxcount",num);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败");
            resMap.put("fxcount",0);
        }
        return resMap;
    }
    /**
     * 关注操作
     */
    @ResponseBody
    @RequestMapping(value = "/follow",method = RequestMethod.GET)
    public Map<String,Object> follow(String uId,String fuId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("uId",uId);
            params.put("fuId",fuId);
            params.put("ftime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ShortVideoService.addFollow(params);
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败，error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 打赏
     */
    @ResponseBody
    @RequestMapping(value = "/videogift",method = RequestMethod.GET)
    public Map<String,Object> videogift(String vId,String uId,String count){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("vId",vId);
            params.put("uId",uId);
            params.put("count",count);
            params.put("vgId",UUID.randomUUID().toString().replace("-",""));
            params.put("gtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ShortVideoService.addVideogGift(params);
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败，error:"+e.getMessage());
        }
        return resMap;
    }
    /**
     * 喜欢
     */
    @ResponseBody
    @RequestMapping(value = "/love",method = RequestMethod.GET)
    public Map<String,Object> love(String vId,String uId){
        Map<String,Object> resMap=new HashMap<>();
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("vId",vId);
            params.put("uId",uId);
            ShortVideoService.love(params);
            resMap.put("code","success");
            resMap.put("msg","操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("code","error");
            resMap.put("msg","操作失败");
        }
        return resMap;
    }
}
