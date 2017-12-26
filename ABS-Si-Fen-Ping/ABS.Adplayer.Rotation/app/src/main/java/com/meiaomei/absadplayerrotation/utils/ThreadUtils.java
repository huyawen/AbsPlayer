package com.meiaomei.absadplayerrotation.utils;

/**
 * Created by zoucf on 2016/3/19.
 */
public class ThreadUtils {

    public static void ThreadSleep(Thread thread,int n){
        try {
            thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
