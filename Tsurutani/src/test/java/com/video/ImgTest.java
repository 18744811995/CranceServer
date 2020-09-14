package com.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImgTest {
    public static void main(String [] ages){
        FFmpegFrameGrabber ff = null;
        Random random = new Random();
        String temPath= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + String.valueOf(random.nextInt(999)) + "_test.jpg";
        String newImgName = "/home/duansp/upload/videoimg/"+temPath;
        //String resPath=reqSourcesPath+"videoimg/"+temPath;
        File targetFile = new File(newImgName);
        try {
            File parent = targetFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            ff = new FFmpegFrameGrabber(new File("D:\\home\\duansp\\upload\\video\\2020-08-05\\530_鹤谷物流.mp4"));
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
    }
}
