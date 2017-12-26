package com.meiaomei.absadplayerrotation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meiaomei.absadplayerrotation.dbutility.AbsPlbsDB;
import com.meiaomei.absadplayerrotation.manager.CommonHttpRequest;
import com.meiaomei.absadplayerrotation.manager.HttpHelper;
import com.meiaomei.absadplayerrotation.manager.OkHttpManager;
import com.meiaomei.absadplayerrotation.manager.ParseManager;
import com.meiaomei.absadplayerrotation.model.SystemSetting_SQL;
import com.thoughtworks.xstream.XStream;


/**
 * Created by huyawen on 2017/4/18.
 * <p/>
 * 根据心跳时长定时去服务器荡数据
 */
public class AutoService extends Service {

    Thread thread;
    Context context;
    long heartBeat = 60000;//默认心跳
    HttpHelper httpHelper;
    String macAdress = "";
    String ipAndPort = "";
    OkHttpManager manager = new OkHttpManager();
    XStream xStream = new XStream();
    ParseManager pManager = new ParseManager(AutoService.this);
    DbUtils dbUtils;
    boolean isOne = true;
    CommonHttpRequest request;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        httpHelper = new HttpHelper();
        dbUtils = AbsPlbsDB.getDbUtils();
        request = new CommonHttpRequest(context);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//开启服务
        try {//onStartCommand里保证每次都会查到最新的mac地址
            SystemSetting_SQL systemSetting_sql = dbUtils.findFirst(SystemSetting_SQL.class);
            if (systemSetting_sql != null) {
                heartBeat = Integer.parseInt(systemSetting_sql.getHearts());
                macAdress = systemSetting_sql.getMac();
                String ipValue = systemSetting_sql.getIp();
                String portValue = systemSetting_sql.getPort();
                ipAndPort = ipValue + ":" + portValue;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        thread = new Thread(new Runnable() {//心跳线程
            @Override
            public void run() {
                while (true) {
                    if (isNetworkAvailable(context)) {//有网才走进得去心跳
                        String url = "http://" + ipAndPort + "/zhyh/PlayDev";//真实的心跳轮循接口
                        manager.postHead(url, "getTask", macAdress, "", new OkHttpManager.HttpCallBack() {
                            @Override
                            public void onSusscess(String data) {
                                Log.e("心跳得到的任务xml====", data);
                                pManager.parseCommand(data, ipAndPort, macAdress);
                            }

                            @Override
                            public void onError(String meg) {
                                super.onError(meg);
                            }
                        });

                        try {
                            thread.sleep(heartBeat * 1000);//默认心跳为20s
//                          pManager.parseCommand("", ipAndPort, macAdress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread.start();
//        return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;//系统会自动重启该服务，并将Intent的值传入
    }

    @Override
    public IBinder onBind(Intent intent) {//绑定服务
        return null;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }























    /*try {
                            //webservice的网络请求压缩包
                            WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getPlayPlanName", "", new WebServiceUtils.WebServiceCallBack() {
                                @Override
                                public void callBack(SoapObject result) {
                                    if (result != null) {
                                        Log.e("AutoService---====", "result" + result);
                                        int count = result.getPropertyCount();
                                        if (count > 0) {
                                            for (int i = 0; i < count; i++) {
                                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//内存卡
                                                    String path = "file";
                                                    String mfileName = result.getProperty(i).toString() + ".zip";
                                                    int flag = httpHelper.downlaodFile(HttpHelper.SERVER_URL + "Download/" + mfileName, path, "/" + mfileName);
                                                    if (flag == 0) {
                                                        Log.e("下载成功！", "ddddddddd");
                                                        ZipUtils.upZipFile(new File(FileUtils.SDPATH + path + "/" + mfileName), FileUtils.SDPATH + path, true);
                                                    } else if (flag == -1) {
                                                        Log.e("下载失败！", "bbbbbbbbb");
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.e("==无需请求==", "AutoService-----没有新的压缩文件");
                                        }
                                    } else {
                                        Log.e("====", "调用失败--AutoService----------getPlayPlanName");
                                    }
                                }
                            });

                            WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getPlayPlan1", "F4:4D:30:99:37:F0", new WebServiceUtils.WebServiceCallBack() {
                                @Override
                                public void callBack(SoapObject result) {
                                    if (result != null) {
                                        Log.e("getPlayPlan1---====", "result" + result);
                                        int outCount = result.getPropertyCount();
                                        for (int i=0;i<outCount;i++){
                                            SoapObject o = (SoapObject) result.getProperty(i);
                                            PlayPlan_SQL pl = new PlayPlan_SQL();
                                            pl.setAddTime(DateUtils.string2Date(o.getProperty("addTime").toString()));
                                            pl.setAddUser(o.getProperty("addUsername").toString());
                                            pl.setId(o.getProperty("materialId").toString());
                                            pl.setName(o.getProperty("materialName").toString());
                                            pl.setSize(Long.valueOf(o.getProperty("materialSize").toString()));
                                            pl.setPlayDuration(Long.valueOf(o.getProperty("playduration").toString()));
                                            pl.setPlayOrder(Integer.valueOf(o.getProperty("playorder").toString()));
                                            pl.setProPackage(FileUtils.SDPATH + "file/" + o.getProperty("proPackage").toString());
                                            pl.setType(o.getProperty("type").toString());
                                            boolean isSave = busi.savePlayPlan("", pl);//保存播放节目
                                            Log.e("isSave===", "=====" + isSave);
                                        }

                                    } else {
                                        Log.e("====", "调用失败--AutoService----------getPlayPlan1");
                                    }
                                }
                            });
                            iString.getService("aaaaaaa");
                            thread.sleep(heartBeat);//默认心跳为20s
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

}
