package com.meiaomei.absadplayerrotation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.meiaomei.absadplayerrotation.activity.LoginActivity;
import com.meiaomei.absadplayerrotation.utils.DeviceInfoUtils;

/**
 * Created by huyawen
 * on 2017/6/17.
 * 程序进入桌面一段时间后 自动进入程序
 */
public class IntoAppService extends Service {

    Context context;

    Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean isAppOnForeground = DeviceInfoUtils.isAppOnForeground();
                    if (isAppOnForeground) {//在前台，无需处理

                    } else {//在后台  ,发送广播主动进入程序
                        Intent mainActivityIntent = new Intent(context, LoginActivity.class);  // 要启动的Activity
                        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainActivityIntent);
                    }
                    try {
                        thread.sleep(300 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
        return START_REDELIVER_INTENT;//系统会自动重启该服务，并将Intent的值传入
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
