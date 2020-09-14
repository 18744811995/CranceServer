package com.video;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.bytedeco.javacpp.avcodec.av_packet_unref;


public class VideoTest {

    public static void main(String[]ages) throws FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("D:/upload/loca.mp4");
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.start();
            System.out.println("开始");
            Frame frame = grabber.grabFrame();
            if (frame != null) {
                /*File outFile = new File("D:/upload/loca.mp4");
                if (!outFile.isFile()) {
                    try {
                        outFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
                /*int width = frame.imageWidth;
                int height = frame.imageHeight;
                System.out.println("宽："+width);
                System.out.println("高："+height);*/
                recorder = new FFmpegFrameRecorder("rtmp://127.0.0.1:1935/live/home",  1280, 720,1);
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 直播流格式
                recorder.setFormat("flv");// 录制的视频格式
                recorder.setFrameRate(25);// 帧数
                //百度翻译的比特率，默认400000，但是我400000贼模糊，调成800000比较合适
                recorder.setVideoBitrate(800000);
                recorder.start();
                while ((frame != null)) {
                    recorder.record(frame);// 录制
                    frame = grabber.grabFrame();// 获取下一帧
                }
                recorder.record(frame);
                // 停止录制
                recorder.stop();
                grabber.stop();
            }
            System.out.println("结束");
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }finally {
            if (null != grabber) {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
