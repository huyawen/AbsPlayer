package com.meiaomei.absadplayerrotation.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meiaomei.absadplayerrotation.activity.LoginActivity;
import com.meiaomei.absadplayerrotation.utils.LogToFile;

/**
 * Created by huyawen on 2017/6/17.
 * 更新apk
 * 卸载apk
 * 添加apk 都会发相应的广播
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //接受更新的apk的广播后 会自动进入setactivity
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            Log.e(LogToFile.TEST_LOG,"--接收更新的广播`--->已更新");
            Intent intentSet = new Intent(context, LoginActivity.class);
            intentSet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSet);
        }
        //接收安装广播`
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            Log.e(LogToFile.TEST_LOG,"--接收安装广播---->已卸载");
            String packageName = intent.getDataString();
            System.out.println("安装了:" + packageName + "包名的程序");
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            Log.e(LogToFile.TEST_LOG,"--接收卸载广播---->已卸载");
            String packageName = intent.getDataString();
            System.out.println("卸载了:" + packageName + "包名的程序");
        }
    }
}
