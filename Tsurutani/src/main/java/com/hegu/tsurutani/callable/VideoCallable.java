package com.hegu.tsurutani.callable;

import com.hegu.tsurutani.app.controller.ShortVideoController;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class VideoCallable implements Callable<String> {
    private Logger logger = LoggerFactory.getLogger(VideoCallable.class);
    private String vId;
    private String locaFilePath;
    private String rtmpPath;

    public VideoCallable() {

    }

    public VideoCallable(String vId, String locaFilePath, String rtmpPath) {
        this.vId = vId;
        this.locaFilePath = locaFilePath;
        this.rtmpPath = rtmpPath;
    }

    @Override
    public String call() throws Exception {
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;
        rtmpPath = rtmpPath+vId;
        logger.info("===============>rtmp:" + rtmpPath);
        try {
            //异步上传媒体流
            grabber = new FFmpegFrameGrabber(locaFilePath);
            // grabber.setOption("rtsp_transport", "tcp");
            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            recorder = new FFmpegFrameRecorder(rtmpPath, 1280, 720, 1);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("flv");
            // 开始取视频源
            //recordByFrame(grabber, recorder, isStart);
            grabber.start();
            recorder.start();
            Frame frame = null;
            logger.info("=============>开始上传媒体流<===============");
            int num=0;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
                num++;
                logger.info("===========num："+num);
            }
            logger.info("=============>上传媒体流结束<===============");
            recorder.stop();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("===============>上传媒体流失败，error:" + e.getMessage());
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
        return rtmpPath;
    }
}
