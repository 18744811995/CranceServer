package com.video;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RtmpTeest {

    public static  void main(String[]ages) throws Exception {
       // "D:/upload/loca.mp4"
       String outputFile= "rtmp://127.0.0.1:1935/live/home";
       InputStream is=new FileInputStream(new File("D:/upload/loca.mp4"));
       System.out.println("=============>传输开始");
       frameRecord(is, outputFile, 1);
        System.out.println("=============>传输结束");
    }



    /**
     * 按帧录制视频
     *
     * @param inputFile-该地址可以是网络直播/录播地址，也可以是远程/本地文件路径
     * @param outputFile
     *            -该地址只能是文件地址，如果使用该方法推送流媒体服务器会报错，原因是没有设置编码格式
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     */
    public static void frameRecord(InputStream inputFile, String outputFile, int audioChannel)
            throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        boolean isStart=true;//该变量建议设置为全局控制变量，用于控制录制结束
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
       // grabber.setOption("rtsp_transport", "tcp");
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        // 开始取视频源
        //recordByFrame(grabber, recorder, isStart);
        try {//建议在线程中使用该方法
            grabber.start();
            recorder.start();
            Frame frame =null ;
            int index=0;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
                index++;
                System.out.println("=========>"+index);
            }
            recorder.stop();
            grabber.stop();
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
    }

    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder, Boolean status)
            throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        try {//建议在线程中使用该方法
            recorder.start();
            recorder.start();
            Frame frame =null ;
            int index=0;
            while (status&& (frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
                index++;
                System.out.println("=========>"+index);
            }
            recorder.stop();
            grabber.stop();
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
    }
}
