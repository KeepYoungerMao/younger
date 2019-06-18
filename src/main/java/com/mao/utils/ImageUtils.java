package com.mao.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.UUID;

/**
 * 图片类工具
 * 1.图片下载
 * 2.图片处理：使用thumbnailator工具处理
 * 图片处理工具类参见GitHub：https://github.com/coobird/thumbnailator
 * @author mao by 11:12 2019/6/18
 */
public class ImageUtils {

    /**
     * 通过图片地址下载图片到本地
     * 目前只希望接收：jpg、png、gif、jpeg、bmp，其它不接收
     * 图片名称使用UUID字符串，保存成功后返回图片名称。
     * 不符合条件、保存失败，返回null
     * @param url 图片地址
     * @param path 本地路径
     * @return 图片名称
     */
    public static String downloadImage(String url, String path){
        if(SU.isEmpty(url) || SU.isEmpty(path)) return null;
        if (!path.endsWith("\\")) path += "\\";
        String imageFix = null;
        if(url.endsWith(".jpg")) imageFix = ".jpg";
        else if(url.endsWith(".png")) imageFix = ".png";
        else if (url.endsWith(".gif")) imageFix = ".gif";
        else if (url.endsWith(".jpeg")) imageFix = ".jpeg";
        else if (url.endsWith(".bmp")) imageFix = ".bmp";
        if(null == imageFix) return null;
        String imagePre = UUID.randomUUID().toString().replaceAll("-","");
        String image = imagePre+imageFix;

        URL Url;
        try{
            Url = new URL(url);
            DataInputStream dataInputStream = new DataInputStream(Url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path+image));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while((length = dataInputStream.read(buffer)) > 0){
                outputStream.write(buffer,0,length);
            }
            fileOutputStream.write(outputStream.toByteArray());

            dataInputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return image;
    }

    /**
     * 图片处理:缩放
     * 模式1：指定宽高，但不改变比例，
     * 此模式缩放，宽或高谁先缩放到指定大小，其他的大小便缩放到此不在缩小
     * 模式2：制定宽高，不按照原来高宽比例（此模式照片可能会变形）
     * @param srcPath 图片来源地址
     * @param toPath 图片保存地址
     * @param width 宽度
     * @param height 高度
     * @return 1：成功，0：失败
     */
    public static int scalingImage(String srcPath, String toPath, int width, int height, boolean same){
        try {
            Thumbnails.of(srcPath).size(width,height).keepAspectRatio(same).toFile(toPath);
            return 1;
        }catch (IOException e){
            return 0;
        }
    }

    /**
     * 图片处理:缩放
     * 模式：等比例缩放
     * 指定一个缩放指数（0-1）之间，成倍数缩小
     * 指定一个质量指数（0-1）之间，越接近1图片质量越好（细粒度）,但体积就越大
     * @param srcPath 图片来源地址
     * @param toPath 图片保存地址
     * @param scale 比例 0-1 之间
     * @param quality 图片质量
     * @return 1：成功，0：失败
     */
    public static int scalingImage(String srcPath, String toPath, double scale, double quality){
        try {
            Thumbnails.of(srcPath).scale(scale).outputQuality(quality).toFile(toPath);
            return 1;
        }catch (IOException e){
            return 0;
        }
    }

    /**
     * 图片处理:旋转
     * 模式：等比例缩放
     * @param srcPath 图片来源地址
     * @param toPath 图片保存地址
     * @param rotate 旋转角度 -180，-90，90，180
     * @return 1：成功，0：失败
     */
    public static int scalingImage(String srcPath, String toPath, int rotate){
        try {
            Thumbnails.of(srcPath).rotate(rotate).toFile(toPath);
            return 1;
        }catch (IOException e){
            return 0;
        }
    }

    /**
     * 图片处理：添加水印
     * @param srcPath  图片来源地址
     * @param toPath 图片保存地址
     * @param waterMarkPath 水印图片地址
     * @param waterMarkOpacity 水印透明度 0-1之间
     * @return 1：成功，0：失败
     */
    public static int scalingImage(String srcPath, String toPath, String waterMarkPath, float waterMarkOpacity){
        try {
            Thumbnails.of(srcPath)
                    .scale(1)
                    .watermark(Positions.CENTER, ImageIO.read(new File(waterMarkPath)),waterMarkOpacity)
                    .toFile(toPath);
            return 1;
        }catch (IOException e){
            return 0;
        }
    }

}