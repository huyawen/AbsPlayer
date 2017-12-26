package com.meiaomei.absadplayerrotation;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.meiaomei.absadplayerrotation.utils.LogToFile;

/**
 * Created by meiaomei on 2017/3/1.
 */
public class AbsPlbsApplication extends Application{

    public static AbsPlbsApplication application;
    public static Context context;
    public static WindowManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application=getApplication();
        LogToFile.init(context);
        manager= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }


    public static AbsPlbsApplication getApplication() {
        return application;
    }



    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getAppContext() {
        return context;
    }

}
