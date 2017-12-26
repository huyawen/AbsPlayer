package com.meiaomei.absadplayerrotation.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.meiaomei.absadplayerrotation.AbsPlbsApplication;
import com.meiaomei.absadplayerrotation.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huyawen on 2017/3/7.
 */


public class ZToast {

    private static ZToast instance; //单例的
    private View mToastView;//自定义toast view
    private TextView mTextView;
    private boolean mIsShow;//记录状态 是否在显示
    private Timer mTimer;//定时器

    private WindowManager.LayoutParams mParams;


    public synchronized static ZToast getInstance(Context context) {
        if (instance == null)
            instance = new ZToast(context);
        return instance;
    }

    private ZToast(Context context) {
        mIsShow = false;// 记录当前Toast的内容是否已经在显示

        //这里初始化toast view
        mToastView = LayoutInflater.from(context).inflate(R.layout.common_toast, null);

        //用来提示的文字
        mTextView = ((TextView) mToastView.findViewById(R.id.toast_text));

        //初始化计数器
        mTimer = new Timer();
        // 设置布局参数
        setParams();
    }


    private void setParams() {
        mParams = new WindowManager.LayoutParams();//初始化
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;   //宽
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.custom_animation_toast;// 设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.BOTTOM;        //对其方式
        mParams.y = 85;      //下间距
    }


    public void show(String text, int mShowTime) {
        if (mIsShow) {// 如果Toast已经在显示 就先给隐藏了
            if (AbsPlbsApplication.manager != null && mToastView != null)
                AbsPlbsApplication.manager.removeView(mToastView);
            // 取消计时器
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = new Timer();
            }
        }
        //设置显示内容
        mTextView.setText(text);
        //设置显示状态
        mIsShow = true;
        // 将其加载到windowManager上
        AbsPlbsApplication.manager.addView(mToastView, mParams);

        //设置计时器
        mTimer.schedule(new TimerTask() {//timertask就是实现了runnable接口的      展示时间一到就移除view
            @Override
            public void run() {
                AbsPlbsApplication.manager.removeView(mToastView);
                mIsShow = false;
            }
        }, (long) (mShowTime == Toast.LENGTH_LONG ? 2300 : 1200));
    }
}
