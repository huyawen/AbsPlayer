package com.meiaomei.absadplayerrotation.utils;

/**
 * Created by huyawen on 2017/3/30.
 * <p/>
 * 判断文件类型的工具类
 */
public class JudgeMediaTypeUtils {

    public int getMediaFileType(String path) {
        int type = 0;
        String name = path.substring(path.lastIndexOf("."));
        if (name.equalsIgnoreCase(".jpg")
                || name.equalsIgnoreCase(".png")
                || name.equalsIgnoreCase(".gif")
                || name.equalsIgnoreCase(".tif")
                || name.equalsIgnoreCase(".bmp")) {
            type = 1;//图片
        } else if (name.equalsIgnoreCase(".mp4")//实际环境有
                || name.equalsIgnoreCase(".3gp")
                || name.equalsIgnoreCase(".wmv")
                || name.equalsIgnoreCase(".ts")
                || name.equalsIgnoreCase(".rmvb")
                || name.equalsIgnoreCase(".mov")//实际环境有
                || name.equalsIgnoreCase(".m4v")
                || name.equalsIgnoreCase(".avi")//实际环境有
                || name.equalsIgnoreCase(".m3u8")
                || name.equalsIgnoreCase(".3gpp")
                || name.equalsIgnoreCase(".3gpp2")
                || name.equalsIgnoreCase(".mkv")
                || name.equalsIgnoreCase(".flv")
                || name.equalsIgnoreCase(".divx")
                || name.equalsIgnoreCase(".f4v")
                || name.equalsIgnoreCase(".rm")
                || name.equalsIgnoreCase(".asf")
                || name.equalsIgnoreCase(".ram")
                || name.equalsIgnoreCase(".mpg")
                || name.equalsIgnoreCase(".v8")
                || name.equalsIgnoreCase(".swf")
                || name.equalsIgnoreCase(".m2v")
                || name.equalsIgnoreCase(".asx")
                || name.equalsIgnoreCase(".ra")
                || name.equalsIgnoreCase(".ndivx")
                || name.equalsIgnoreCase(".xvid")) {
            type = 2;//视频
        }else {//不是图片,不是视频就是网页
            type = 3;
        }
        return type;
    }
}
