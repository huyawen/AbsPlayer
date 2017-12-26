package com.meiaomei.absadplayerrotation.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.meiaomei.absadplayerrotation.AbsPlbsApplication;
import com.meiaomei.absadplayerrotation.R;
import com.meiaomei.absadplayerrotation.dbutility.AbsPlbsDB;
import com.meiaomei.absadplayerrotation.dbutility.dao.CommonDao;
import com.meiaomei.absadplayerrotation.dbutility.dao.impl.CommonDaoImpl;
import com.meiaomei.absadplayerrotation.impl.systemset.SystemSetBusi;
import com.meiaomei.absadplayerrotation.impl.systemset.impl.SystemBusiImpl;
import com.meiaomei.absadplayerrotation.manager.CommonHttpRequest;
import com.meiaomei.absadplayerrotation.manager.ParseManager;
import com.meiaomei.absadplayerrotation.model.Config_SQL;
import com.meiaomei.absadplayerrotation.model.SystemSetting_SQL;
import com.meiaomei.absadplayerrotation.model.event.StringModel;
import com.meiaomei.absadplayerrotation.service.IntoAppService;
import com.meiaomei.absadplayerrotation.utils.DeviceInfoUtils;
import com.meiaomei.absadplayerrotation.utils.FileUtils;
import com.meiaomei.absadplayerrotation.view.ZToast;
import com.meiaomei.absadplayerrotation.view.dialog.MyProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SetActivity extends Activity {

    @ViewInject(R.id.ed_ipValue)
    EditText ed_ipValue;
    @ViewInject(R.id.ed_portValue)
    EditText ed_portValue;
    @ViewInject(R.id.ed_durationValue)
    EditText ed_durationValue;
    @ViewInject(R.id.ed_macAddress)
    EditText ed_macAddress;
    @ViewInject(R.id.ed_img_time)
    EditText ed_img_time;
    @ViewInject(R.id.ed_img_cnt)
    EditText ed_img_cnt;

    @ViewInject(R.id.tv_show_ip)
    TextView tv_show_ip;
    @ViewInject(R.id.tv_show_port)
    TextView tv_show_port;
    @ViewInject(R.id.tv_show_heart)
    TextView tv_show_heart;
    @ViewInject(R.id.tv_show_mac)
    TextView tv_show_mac;
    @ViewInject(R.id.tv_show_img)
    TextView tv_show_img;
    @ViewInject(R.id.tv_img_cnt_show)
    TextView tv_img_cnt_show;

    @ViewInject(R.id.bt_done)
    TextView bt_done;
    @ViewInject(R.id.bt_next)
    TextView bt_next;

    SystemSetBusi busi;
    DbUtils dbUtils;
    CommonDao dao;//查询数据库的操作
    ParseManager parseManager;
    CommonHttpRequest request;
    Dialog progressDialog;
    SystemSetting_SQL systemSetting_sql;
    FileUtils fileUtils;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                checkSystemSetting();//ui线程更新ui
            } else if (msg.what == 1) {
                ZToast.getInstance(SetActivity.this).show("连接服务器失败!", Toast.LENGTH_LONG);
            } else if (msg.what == 2) {
                ZToast.getInstance(SetActivity.this).show("没有同步资源!", Toast.LENGTH_LONG);
            } else if (msg.what == 3) {
                ZToast.getInstance(SetActivity.this).show("资源下载失败!", Toast.LENGTH_LONG);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ViewUtils.inject(this);//xUtils的注册

        if (!DeviceInfoUtils.isServiceWork(this, "com.meiaomei.absadplayerrotation.service.IntoAppService")) {//服务没启动就开启它
            //开启不在桌面5分钟之后自动进入程序
            Intent intent = new Intent(SetActivity.this, IntoAppService.class);
            startService(intent);
        }

        EventBus.getDefault().register(this);//注册eventbus
        initVar();
        progressDialog.show();
        bt_done.setOnClickListener(new MyClickListener());
        bt_next.setOnClickListener(new MyClickListener());

        //子线程查数据库，跳转activity
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
//                setShutDownTime();//子线程查数据库，设置关机(不用做)
            }
        }).start();
    }

    private void checkSystemSetting() {//如果有配置信息  直接跳到播放界面
        try {
            systemSetting_sql = dbUtils.findFirst(SystemSetting_SQL.class);
            if (systemSetting_sql == null) {
                ed_ipValue.setText("192.168.2.148");
                ed_portValue.setText("8090");
                String allMac=DeviceInfoUtils.getMacAddress();//必须打开wifi或者连接到以太网线
                String mac=DeviceInfoUtils.getMacFromCallCmd();
                String wifiMac=DeviceInfoUtils.getMacAddressNew();//wifi必须打开wifi开关 才能获取到mac
                ed_macAddress.setText(TextUtils.isEmpty(allMac) ? "" : allMac.replaceAll(":", "-"));
                ed_durationValue.setText("60");/*"60"*/
                ed_img_time.setText("20");/*"20"*/
                ed_img_cnt.setText("1");
                progressDialog.dismiss();
                return;
            } else {
                tv_show_ip.setText(systemSetting_sql.getIp());
                tv_show_port.setText(systemSetting_sql.getPort());
                tv_show_mac.setText(systemSetting_sql.getMac());
                tv_show_heart.setText(systemSetting_sql.getHearts());
                tv_show_img.setText(systemSetting_sql.getImgTime());
                tv_img_cnt_show.setText(systemSetting_sql.getImgCnt());

                progressDialog.setCancelable(false);
                request.timeSync(systemSetting_sql.getMac(), systemSetting_sql.getIp() + ":" + systemSetting_sql.getPort());
                request.syncProgram(systemSetting_sql.getMac(), systemSetting_sql.getIp() + ":" + systemSetting_sql.getPort());
                request.syncResource(systemSetting_sql.getMac(), systemSetting_sql.getIp() + ":" + systemSetting_sql.getPort());//同步服务器时间和资源
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initVar() {
        fileUtils = new FileUtils();
        progressDialog = new MyProgressDialog().createLoadingDialog(SetActivity.this, " 正在同步服务器资源，请稍后... ");
        parseManager = new ParseManager(this);
        dbUtils = AbsPlbsDB.getDbUtils();
        dao = new CommonDaoImpl(AbsPlbsApplication.getAppContext());//查询数据库的操作
        request = new CommonHttpRequest(this, parseManager, dbUtils);
        busi = new SystemBusiImpl(SetActivity.this);
        AbsPlbsDB.initNewTable();//第一次登陆创建新表
        fileUtils.createDir("xml");//如果没联网,先创建文件夹,考入单机播
        fileUtils.createDir("files");
        fileUtils.createDir("updateFlag");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)//跳转页面
    public void getMessage(StringModel stringModel) {//处理eventbus传递来的消息
        String key = stringModel.getKey();
        switch (key) {
            case "syncresource"://
                progressDialog.dismiss();
                String flag = stringModel.getMsg();
                Log.e("SetActivity====", "SetActivity----------resource");
                if (flag.equals("noWeb")) {
                    handler.obtainMessage(1, "noWeb").sendToTarget();
                } else if (flag.equals("noSync")) {
                    handler.obtainMessage(2, "noSync").sendToTarget();
                } else if (flag.equals("true")) {
                    try {//第一次 进入后 systemSetting_sql为null，保存后在查一次
                        if (systemSetting_sql == null) {
                            systemSetting_sql = dbUtils.findFirst(SystemSetting_SQL.class);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(SetActivity.this, MainActivity.class);
                    intent.putExtra("imgTime", TextUtils.isEmpty(systemSetting_sql.getImgTime()) ? "20" : systemSetting_sql.getImgTime());
                    intent.putExtra("imgCnt", TextUtils.isEmpty(systemSetting_sql.getImgCnt()) ? "1" : systemSetting_sql.getImgCnt());
                    startActivity(intent);
                } else if (flag.equals("false")) {//有下载失败的继续同步资源
                    handler.obtainMessage(3, "false").sendToTarget();
                }
                break;
        }
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_done:
                    String ipValue = ed_ipValue.getText().toString();
                    String portValue = ed_portValue.getText().toString();
                    String durationValue = ed_durationValue.getText().toString();
                    String macAddress = ed_macAddress.getText().toString();
                    String img_cnt = ed_img_cnt.getText().toString();
                    String img_time = ed_img_time.getText().toString();
                    ipValue = ipValue.replaceAll(" ", "");//去掉所有的空格
                    portValue = portValue.replaceAll(" ", "");
                    durationValue = durationValue.replaceAll(" ", "");
                    macAddress = macAddress.replaceAll(" ", "");
                    img_cnt = img_cnt.replaceAll(" ", "");
                    img_time = img_time.replaceAll(" ", "");
                    boolean checkWeb = busi.checkSaveWeb(ipValue, portValue, durationValue, macAddress);
                    if (checkWeb) {
                        HashMap<String, String> infomap = new HashMap<>();
                        infomap.put("webIP", ipValue);
                        infomap.put("webPort", portValue);
                        infomap.put("heartbeatDuration", durationValue);
                        infomap.put("macAddress", macAddress);
                        infomap.put("imgTime", img_time);
                        infomap.put("imgCnt", img_cnt);
                        if (busi.saveSetting(infomap)) {
                            ZToast.getInstance(SetActivity.this).show("保存成功！", Toast.LENGTH_SHORT);
                            tv_show_ip.setText(ipValue);
                            tv_show_port.setText(portValue);
                            tv_show_heart.setText(durationValue);
                            tv_show_mac.setText(macAddress);
                            tv_show_img.setText(img_time);
                            tv_img_cnt_show.setText(img_cnt);
                        } else {
                            ZToast.getInstance(SetActivity.this).show("保存失败！", Toast.LENGTH_SHORT);
                        }
                    }
                    break;

                case R.id.bt_next:
                    if (tv_show_ip.getText().equals("") || tv_show_ip.getText() == null
                            && tv_show_port.getText().equals("") || tv_show_port.getText() == null
                            && tv_show_mac.getText().equals("") || tv_show_mac == null) {
                        ZToast.getInstance(SetActivity.this).show("请先配置完基本信息！", Toast.LENGTH_SHORT);
                    } else {
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        request.timeSync(tv_show_mac.getText().toString(), tv_show_ip.getText().toString().replace(" ", "") + ":" + tv_show_port.getText().toString().replace(" ", ""));
                        request.syncProgram(tv_show_mac.getText().toString(), tv_show_ip.getText().toString().replace(" ", "") + ":" + tv_show_port.getText().toString().replace(" ", ""));
                        request.syncResource(tv_show_mac.getText().toString(), tv_show_ip.getText().toString().replace(" ", "") + ":" + tv_show_port.getText().toString().replace(" ", ""));//同步服务器时间和资源
                    }
                    break;
            }
        }
    }


    //关机的方法
    public void shutDownAlarm(long time) {
        AlarmManager am;//闹钟的类
        PendingIntent pi;//带跳转意图的intent
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.abs.plbs");
        pi = PendingIntent.getBroadcast(SetActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, time, pi);//这个时间就是设置关机的时间
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//edittext得到焦点 隐藏
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        finish();
        LogUtils.e("Setactivety---onDestroy");
    }

    //每天设置关机时间
    private void setShutDownTime() {
        Config_SQL config_sql = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            config_sql = dbUtils.findFirst(Config_SQL.class);
            if (config_sql != null) {
                String shutdowntime = config_sql.getShutdowntime();
                if (TextUtils.isEmpty(shutdowntime)) {
                    shutdowntime = "18:30";
                }
                Calendar nowCalendar = Calendar.getInstance();
                int nowYear = nowCalendar.get(Calendar.YEAR);
                int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
                int nowDay = nowCalendar.get(Calendar.DATE);
                date = sdf.parse(nowYear + "-" + nowMonth + "-" + nowDay + " " + shutdowntime);
                shutDownAlarm(date.getTime());//设置关机闹钟
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }
}
