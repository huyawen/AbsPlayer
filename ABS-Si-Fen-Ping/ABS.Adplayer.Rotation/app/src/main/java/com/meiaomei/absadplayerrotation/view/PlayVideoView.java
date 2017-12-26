package com.meiaomei.absadplayerrotation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by huyawen on 2017/3/9.
 * //VideoView用了自定义是因为当布局改变的时候，要让VideoView重新获取布局位置
 */

public class PlayVideoView extends android.widget.VideoView {


    public PlayVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PlayVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayVideoView(Context context) {
        super(context);
    }


    //重写onmesure方法  跳过原声的代码根据分辨率去改变视频的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//代码里设置了param参数相当于模式为 exactly 确切的
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
//        Log.e("x---y","——"+width+"-"+height);
        setMeasuredDimension(width, height);
    }
}
