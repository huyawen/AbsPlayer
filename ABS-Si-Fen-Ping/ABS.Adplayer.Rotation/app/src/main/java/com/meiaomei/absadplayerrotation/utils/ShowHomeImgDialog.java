package com.meiaomei.absadplayerrotation.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.meiaomei.absadplayerrotation.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by huyawen on 16/5/17.
 */
public class ShowHomeImgDialog {
    public static void ShowHomeImg(Context context, String path) {
        final Dialog dialog = new Dialog(context, R.style.AcitvityDialog);
        dialog.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.view_show_home_img, null);
        ImageView img = (ImageView) view.findViewById(R.id.img_big);

        Picasso.with(context).load("file://" + path).fit().memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img); //noFade没有动画效果
        dialog.setContentView(view);

        //返回当前的窗体对象
        Window win = dialog.getWindow();

        //设置距离屏幕的边距为0
        win.getDecorView().setPadding(0, 0, 0, 0);

        //获取窗体的所有属性
        WindowManager.LayoutParams lp = win.getAttributes();
        if (ScreenUtils.getScreenHeight(context) > ScreenUtils.getScreenWidth(context)) {//竖屏
            //设置宽和高
            lp.width = ScreenUtils.getScreenWidth(context) / 2+150;
            lp.height = ScreenUtils.getScreenWidth(context) / 2+150;
        } else {//横屏
            lp.width = ScreenUtils.getScreenHeight(context) / 2+150;
            lp.height = ScreenUtils.getScreenHeight(context) / 2+150;
        }
        //将修改的内容给当前窗体进行设置
        win.setAttributes(lp);
        win.setDimAmount(0.8f);//设置遮罩全暗

        //设置窗体弹出和退出的动画(用style描述)
        win.setWindowAnimations(R.style.DialogAnimation);
        dialog.show();
    }
}
