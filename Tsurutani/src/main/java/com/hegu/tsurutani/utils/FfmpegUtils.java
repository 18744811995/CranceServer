package com.hegu.tsurutani.utils;


import org.bytedeco.javacpp.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FfmpegUtils {
    /*public static void main(String[] args) throws InterruptedException, FrameRecorder.Exception, Exception
    {
        String serverPath="rtmp://127.0.0.1:1935/live/home";
        String locaFilePath="D:/upload/loca.mp4";
        recordLocaVideo(serverPath,locaFilePath);//这里将地址换成你的远程视频服务器地址例如recordCamera("rtmp://192.168.30.21/live/record1",25);  即可
        //uploadFileStramTest(serverPath,locaFilePath);
    }
    *//**
     * @param outputFile 服务器地址，我这里直接将视频输送到我本机目录
     * @param frameRate
     * @throws Exception
     * @throws InterruptedException
     *//*
    public static void recordCamera(String outputFile, double frameRate)
            throws Exception, InterruptedException, FrameRecorder.Exception {
        Loader.load(opencv_objdetect.class);
        FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
        grabber.start();//开启抓取器

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
        opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(frameRate);

        recorder.start();//开启录制器
        long startTime = 0;
        long videoTS = 0;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        Frame rotatedFrame = converter.convert(grabbedImage);//不知道为什么这里不做转换就不能推到rtmp
        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            rotatedFrame = converter.convert(grabbedImage);
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
    }

    *//**
     *
     * @param serverFilePath 媒体流存放地址
     * @param loacFilePath 本地文件地址
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     * @throws InterruptedException
     *//*
    public static void recordLocaVideo(String serverFilePath,String loacFilePath) throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
        int err_index = 0;///推流过程中出现错误的次数
        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(loacFilePath);
            grabber.setOption("stimeout", "2000000");
            grabber.setVideoOption("vcodec", "copy");
            grabber.setFormat("mjpeg");
            grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            // h264编/解码器
            grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            grabber.start();

            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(serverFilePath,
                    grabber.getImageWidth(), grabber.getImageHeight(), 0);
            recorder.setInterleaved(true);
            // 设置比特率
            recorder.setVideoBitrate(2500000);
            // h264编/解码器
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            // 封装flv格式
            recorder.setFormat("flv");
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
            recorder.setFrameRate(grabber.getFrameRate());
            // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
            recorder.setGopSize((int) grabber.getFrameRate() * 2);
            avformat.AVFormatContext fc = null;
            fc = grabber.getFormatContext();
            recorder.start(fc);
            // 清空探测时留下的缓存
            grabber.flush();

            avcodec.AVPacket pkt = null;
            long dts = 0;
            long pts = 0;

            System.out.println("===============>开始推流");
            for (int no_frame_index = 0; no_frame_index < 5 || err_index < 5;) {
                pkt = grabber.grabPacket();
                if (pkt == null || pkt.size() <= 0 || pkt.data() == null) {
                    // 空包记录次数跳过
                    no_frame_index++;
                    err_index++;
                    continue;
                }
                // 获取到的pkt的dts，pts异常，将此包丢弃掉。
                if (pkt.dts() == avutil.AV_NOPTS_VALUE && pkt.pts() == avutil.AV_NOPTS_VALUE || pkt.pts() < dts) {
                    err_index++;
                    av_packet_unref(pkt);
                    continue;
                }
                // 记录上一pkt的dts，pts
                dts = pkt.dts();
                pts = pkt.pts();
                // 推数据包
                err_index += (recorder.recordPacket(pkt) ? 0 : 1);
                // 将缓存空间的引用计数-1，并将Packet中的其他字段设为初始值。如果引用计数为0，自动的释放缓存空间。
                av_packet_unref(pkt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("=============>推流结束");
        }
    }

    public static void uploadFileStramTest(String serverFilePath,String loacFilePath){
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(serverFilePath);
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.start();
            Frame frame = grabber.grabFrame();
            if (frame != null) {
                File outFile = new File(loacFilePath);
                if (!outFile.isFile()) {
                    try {
                        outFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
                recorder = new FFmpegFrameRecorder(loacFilePath, 1080, 1440, 0);
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
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } finally {
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
    }*/
}
