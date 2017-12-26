package com.meiaomei.absadplayerrotation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.meiaomei.absadplayerrotation.R;
import com.meiaomei.absadplayerrotation.dbutility.AbsPlbsDB;
import com.meiaomei.absadplayerrotation.manager.ParseManager;
import com.meiaomei.absadplayerrotation.model.ExtraMessage_SQL;
import com.meiaomei.absadplayerrotation.model.Model_SQL;
import com.meiaomei.absadplayerrotation.model.bean.Models;
import com.meiaomei.absadplayerrotation.model.bean.Program;
import com.meiaomei.absadplayerrotation.model.event.StringModel;
import com.meiaomei.absadplayerrotation.service.AutoService;
import com.meiaomei.absadplayerrotation.utils.DateUtils;
import com.meiaomei.absadplayerrotation.utils.FileUtils;
import com.meiaomei.absadplayerrotation.utils.JudgeMediaTypeUtils;
import com.meiaomei.absadplayerrotation.utils.MySort;
import com.meiaomei.absadplayerrotation.utils.ScreenUtils;
import com.meiaomei.absadplayerrotation.utils.SharedPrefsUtil;
import com.meiaomei.absadplayerrotation.utils.UnitUtils;
import com.meiaomei.absadplayerrotation.view.AutoTextView;
import com.meiaomei.absadplayerrotation.view.PlayVideoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.thoughtworks.xstream.XStream;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @ViewInject(R.id.rl_play)
    RelativeLayout rl_play;

    AutoTextView tv_auto;
    ImageView img_one;
    ImageView img_two;
    ImageView img_three;
    ImageView img_four;

    PlayVideoView play_one;
    PlayVideoView play_two;
    PlayVideoView play_three;
    PlayVideoView play_four;

    WebView wv_one;
    WebView wv_two;
    WebView wv_three;
    WebView wv_four;

    ParseManager parseManager;
    RelativeLayout tLayout;//包裹即时消息的rl
    int type = 0;
    String path_full = "";
    String path_second_area1 = "";
    String path_second_area2 = "";
    String path_three_area1 = "";
    String path_three_area2 = "";
    String path_three_area3 = "";
    String path_four_area1 = "";
    String path_four_area2 = "";
    String path_four_area3 = "";
    String path_four_area4 = "";

    boolean model_full_h_default_lock = true;//1 m

    boolean model_full_h_in_lock = true;//1  c
    boolean model_second_h_in_lock = true;//2  c
    boolean model_three_h_in_lock = true;//3 c
    boolean model_four_h_in_lock = true;//4  c

    boolean model_full_h_common_lock = true;//1 p
    boolean model_second_h_common_lock = true;//2 p
    boolean model_three_h_common_lock = true;//3 p
    boolean model_four_h_common_lock = true;//4 p

    boolean onlyPlayNotTimeAndHaveTime = true;

    boolean model_full_h_notime_lock = true;
    boolean model_second_h_notime_lock = true;
    boolean model_three_h_notime_lock = true;

    boolean secondOneNoTimeBackLockFromPlayTwo = false;//2.1 守护 2.2
    boolean threeOneNoTimeBackLockFromPlayTwo = false;//3.1 守护 3.2
    boolean fourOneNoTimeBackLockFromPlayTwo = false;//4.1 守护 4.2

    boolean isStickOne = true;//跳过有问题的视频 只跳一次

    int index_full_one_c = 0;         //h  v 1.1 插播的角标
    int index_full_one_p = 0;         //h  v 1.1 普通的角标
    int index_full_one_m = 0;         //h  v 1.1 默认的角标

    int index_second_area1_c = 0;     //h  v 2.1 插播的角标
    int index_second_area1_p = 0;     //h  v 2.1 普通的的角标
    int index_second_area2_c = 0;     //h  v 2.2 插播的角标
    int index_second_area2_p = 0;     //h  v 2.2 普通的的角标

    int index_three_area1_c = 0;      //h   3.1 插播的角标
    int index_three_area1_p = 0;      //h   3.1 普通的的角标
    int index_three_area2_c = 0;      //h   3.2 插播的角标
    int index_three_area2_p = 0;      //h   3.2 普通的的角标
    int index_three_area3_c = 0;      //h   3.3 插播的角标
    int index_three_area3_p = 0;      //h   3.3 普通的的角标

    int index_four_area1_c = 0;      //h   4.1 插播的角标
    int index_four_area1_p = 0;      //h   4.1 普通的的角标
    int index_four_area2_c = 0;      //h   4.2 插播的角标
    int index_four_area2_p = 0;      //h   4.2 普通的的角标
    int index_four_area3_c = 0;      //h   4.3 插播的角标
    int index_four_area3_p = 0;      //h   4.3 普通的的角标
    int index_four_area4_c = 0;      //h   4.4 插播的角标
    int index_four_area4_p = 0;      //h   4.4 普通的的角标

    Bitmap b;
    MediaMetadataRetriever mmr;
    JudgeMediaTypeUtils mediaType;
    Configuration mConfiguration;//设置的配置信息
    int ori;//屏幕的方向

    DbUtils dbUtils;
    String model_full_h_pls_stdTime = "";// 1-p     (pls为节目单的时间)
    String model_full_h_pls_edTime = "";
    String model_second_h_pls_stdTime = "";//2-1-p
    String model_second_h_pls_edTime = "";
    String model_three_h_pls_stdTime = "";//3-1-p
    String model_three_h_pls_edTime = "";
    String model_four_h_pls_stdTime = "";//4-1-p
    String model_four_h_pls_edTime = "";

    String model_full_h_in_pls_stdTime = "";// 1-c(插播节目单的时间)
    String model_full_h_in_pls_edTime = "";
    String model_second_h_in_pls_stdTime = "";//2-1-c
    String model_second_h_in_pls_edTime = "";
    String model_three_h_in_pls_stdTime = "";//3-1-c
    String model_three_h_in_pls_edTime = "";
    String model_four_h_in_pls_stdTime = "";//4-1-c
    String model_four_h_in_pls_edTime = "";

    int imgTime = 6;//默认为图片显示时长
    int imgCnt = 1;//默认图片的播放频次
    int playcnt = 0;//滚动字幕的播放次数
    boolean stopThread = false;//停止线程的标志
    HashMap<String, String> resIdMap = new HashMap<>();
    HashMap<String, String> msgMap = null;
    ArrayList<String> model_full_h_common = new ArrayList<>();
    ArrayList<String> model_full_h_default = new ArrayList<>();
    ArrayList<String> model_full_h_in = new ArrayList<>();
    ArrayList<String> model_second_h_area1_common = new ArrayList<>();
    ArrayList<String> model_second_h_area1_in = new ArrayList<>();
    ArrayList<String> model_second_h_area2_in = new ArrayList<>();
    ArrayList<String> model_second_h_area2_common = new ArrayList<>();
    ArrayList<String> model_three_h_area1_common = new ArrayList<>();
    ArrayList<String> model_three_h_area1_in = new ArrayList<>();
    ArrayList<String> model_three_h_area2_common = new ArrayList<>();
    ArrayList<String> model_three_h_area2_in = new ArrayList<>();
    ArrayList<String> model_three_h_area3_common = new ArrayList<>();
    ArrayList<String> model_three_h_area3_in = new ArrayList<>();

    ArrayList<String> model_four_h_area1_common = new ArrayList<>();
    ArrayList<String> model_four_h_area1_in = new ArrayList<>();
    ArrayList<String> model_four_h_area2_common = new ArrayList<>();
    ArrayList<String> model_four_h_area2_in = new ArrayList<>();
    ArrayList<String> model_four_h_area3_common = new ArrayList<>();
    ArrayList<String> model_four_h_area3_in = new ArrayList<>();
    ArrayList<String> model_four_h_area4_common = new ArrayList<>();
    ArrayList<String> model_four_h_area4_in = new ArrayList<>();

    FromInsertTimerTask fromInsertTimer;//有时间的时间结束后，让普通播放的播放
    Timer timer;//延时等待最后一个加载完进入口
    RequestTimerTask task;//普通播放最后一轮的定时任务
    MsgTimerTask msgTimerTask;//即时消息消息时长结束后的定时任务
    MsgStartTimerTask msgStartTimerTask;

    boolean isUpLoadProgram = false;
    boolean isUpLoadResource = false;

    Runnable runnable11 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(11, "model_full").sendToTarget();
        }
    };

    Runnable runnable22 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(22, "model_second_area2").sendToTarget();
        }
    };

    Runnable runnable32 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(32, "model_three_h_area2").sendToTarget();
        }
    };

    Runnable runnable33 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(33, "model_three_h_area3").sendToTarget();
        }
    };

    Runnable runnable42 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(42, "model_four_h_area2").sendToTarget();
        }
    };

    Runnable runnable43 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(43, "model_four_h_area3").sendToTarget();
        }
    };

    Runnable runnable44 = new Runnable() {
        @Override
        public void run() {
            imgHandler.obtainMessage(44, "model_four_h_area4").sendToTarget();
        }
    };

    //扫描带时间插播和普通播的handler
    Handler handler = new Handler() {
    };
    //从子线程发过来的路径的handler
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {//子线程得到的任务发到主线程
                case 1111://1.1带时间的插播或者普通播
                    String path11 = (String) msg.obj;
                    commonSelectPathOne(path11);
                    break;

                case 2211://2.1带时间的插播或者普通播
                    String path21 = (String) msg.obj;
                    commonSelectPathSecondOne(path21);
                    break;

                case 3311://3.1带时间的插播或者普通播
                    String path31 = (String) msg.obj;
                    commonSelectPathThreeOne(path31);
                    break;

                case 2222://2.2带时间的插播或者普通播
                    String path22 = (String) msg.obj;
                    commonSelectPathSecondTwo(path22);
                    break;

                case 3322://3.2带时间的插播或者普通播
                    String path32 = (String) msg.obj;
                    commonSelectPathThreeTwo(path32);
                    break;

                case 3333://3.3带时间的插播或者普通播
                    String path33 = (String) msg.obj;
                    commonSelectPathThreeThree(path33);
                    break;

                case 4411:
                    String path41 = (String) msg.obj;
                    commonSelectPathFourOne(path41);
                    break;

                case 4422:
                    String path42 = (String) msg.obj;
                    commonSelectPathFourTwo(path42);
                    break;

                case 4433:
                    String path43 = (String) msg.obj;
                    commonSelectPathFourThree(path43);
                    break;

                case 4444:
                    String path44 = (String) msg.obj;
                    commonSelectPathFourFour(path44);
                    break;

                case 0://进入无时间的普通播放的入口
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeList();
                    }
                    break;
                case 1://即时消息时间到后 隐藏
                    tLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    initMessage(msgMap);
                    break;
            }
        }
    };

    //扫描图片播完的handler
    Handler imgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    playBack_model_full();
                    if (onlyPlayNotTimeAndHaveTime) {//没有时间的回调
                        playNotTiemtBack();
                    }
                    break;
                case 22:
                    playBack_mode_second_two();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeSecondtwo();
                    }
                    break;
                case 32:
                    playBack_mode_three_two();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeThreetwo();
                    }
                    break;
                case 33:
                    playBack_mode_three_three();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeThreethree();
                    }
                    break;

                case 42:
                    playBack_mode_four_two();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeFourTwo();
                    }
                    break;

                case 43:
                    playBack_mode_four_three();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeFourThree();
                    }
                    break;
                case 44:
                    playBack_mode_four_four();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeFourFour();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        LogUtils.e("Mainactivety---onCreate");
        parseManager = new ParseManager(this);
        dbUtils = AbsPlbsDB.getDbUtils();
        mmr = new MediaMetadataRetriever();

        Intent intent = new Intent(MainActivity.this, AutoService.class); //开启同步心跳服务
        startService(intent);

        try {//每次的model要为最新的
            dbUtils.dropTable(Model_SQL.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream();
        xStream.processAnnotations(Models.class);
        if (FileUtils.isFileExist("xml/model.xml")) {//存在去解析
            Models models = (Models) xStream.fromXML(new File(FileUtils.SDPATH + "xml/" + "model.xml"));
            parseManager.parseModels(models);//把所有的models存库
        }

        EventBus.getDefault().register(this);//注册eventbus (fragmet是给自己注册)
        InitVar();//初始化变量
        initView();
        initProgram();//没有时间的先走
        playListInit();//有时间的后走
        initVideo_one();
        initVideo_two();
        initVideo_three();
        iniVideo_four();
    }

    private void initProgram() {//第一次进去查数据库
        ExtraMessage_SQL extra = null;
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNowStr = sdf.format(d);
            boolean noInsetToday = true;
            if (SharedPrefsUtil.getValue(MainActivity.this, "date", "yyyy-MM-dd").equals(dateNowStr)) {//是今天的插播
                String timeStart = SharedPrefsUtil.getValue(MainActivity.this, "pls_stdTime", "");
                String timeEnd = SharedPrefsUtil.getValue(MainActivity.this, "pls_edTime", "");
                if (DateUtils.isIntime(timeStart, timeEnd)) {
                    noInsetToday = false;//插播播放了 普通不播放
                    String playMode=SharedPrefsUtil.getValue(MainActivity.this, "playMode", "");
                    String pgFileName=SharedPrefsUtil.getValue(MainActivity.this, "programName", "");
                    initData("1", pgFileName);
                }

            }

            extra = dbUtils.findFirst(ExtraMessage_SQL.class);
            if (extra != null && noInsetToday) {
                String playMode = extra.getPlayMode() == null ? "" : extra.getPlayMode();
                String pgFileName = extra.getPgFileName() == null ? "" : extra.getPgFileName();
                initData(playMode, pgFileName);
            } else if (noInsetToday) {//没网情况下extra为null则单机播放
                initData("1", "/program.xml");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();//从setactivity中传过来的
        imgTime = Integer.parseInt(intent.getStringExtra("imgTime"));
        imgCnt = Integer.parseInt(intent.getStringExtra("imgCnt"));
    }

    private void InitVar() {
        mediaType = new JudgeMediaTypeUtils();
        mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        ori = mConfiguration.orientation; //获取屏幕方向
    }

    private void initView() {
        play_one = new PlayVideoView(this);
        play_one.setId(R.id.play_one);//动态设置id 值在ids.xml中
        play_two = new PlayVideoView(this);
        play_two.setId(R.id.play_two);
        play_three = new PlayVideoView(this);
        play_three.setId(R.id.play_three);
        play_four = new PlayVideoView(this);
        play_four.setId(R.id.play_four);
        rl_play.addView(play_one);
        rl_play.addView(play_two);
        rl_play.addView(play_three);
        rl_play.addView(play_four);

        img_one = new ImageView(this);
        img_one.setId(R.id.img_one);
        img_two = new ImageView(this);
        img_two.setId(R.id.img_two);
        img_three = new ImageView(this);
        img_three.setId(R.id.img_three);
        img_four = new ImageView(this);
        img_four.setId(R.id.play_four);
        rl_play.addView(img_three);
        rl_play.addView(img_two);
        rl_play.addView(img_one);
        rl_play.addView(img_four);

        wv_one = new WebView(this);
        wv_one.setId(R.id.wv_one);
        wv_two = new WebView(this);
        wv_two.setId(R.id.wv_two);
        wv_three = new WebView(this);
        wv_three.setId(R.id.wv_three);
        wv_four = new WebView(this);
        wv_four.setId(R.id.wv_four);
        rl_play.addView(wv_one);
        rl_play.addView(wv_two);
        rl_play.addView(wv_three);
        rl_play.addView(wv_four);
        setH5WebView(wv_one);
        setH5WebView(wv_two);
        setH5WebView(wv_three);
        setH5WebView(wv_four);

        tv_auto = new AutoTextView(MainActivity.this);
        tLayout = new RelativeLayout(this);
        tLayout.addView(tv_auto);//把tv加在new的相对布局上
        rl_play.addView(tLayout);
    }

    public void initMessage(HashMap<String, String> msgMap) {
        String textSize = TextUtils.isEmpty(msgMap.get("fontSize")) ? "18" : msgMap.get("fontSize");
        String position = TextUtils.isEmpty(msgMap.get("position")) ? "top" : msgMap.get("position");
        RelativeLayout.LayoutParams relative = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UnitUtils.px2dip(Float.valueOf(textSize) * 2 - 5));
        if ("top".equals(position)) {
            relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            relative.setMargins(0, 0, 0, 0);

        } else if ("under".equals(position)) {
            relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            relative.setMargins(0, 0, 0, 0);
        }
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tv_auto.setText(msgMap.get("message") == null ? "" : msgMap.get("message"));
        tv_auto.setTextColor(Color.parseColor(TextUtils.isEmpty(msgMap.get("fontColor")) ? "#FFFFFF" : msgMap.get("fontColor")));
        tv_auto.setTextSize(Float.valueOf(textSize));//防止字体溢出
        tv_auto.setBackgroundColor(Color.parseColor(TextUtils.isEmpty(msgMap.get("bgColor")) ? "#89beb2" : msgMap.get("bgColor")));
        tv_auto.setScrollMode(Integer.valueOf(TextUtils.isEmpty(msgMap.get("speed")) ? "1" : msgMap.get("speed")));
        tv_auto.setGravity(Gravity.CENTER_VERTICAL);
        p.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tv_auto.setLayoutParams(p);//控件设置自己的相对布局
        tLayout.setLayoutParams(relative);//内层相对布局设置根相对布局
        playcnt = Integer.valueOf(TextUtils.isEmpty(msgMap.get("count")) ? "0" : msgMap.get("count"));
        tv_auto.setPlayCntListener(new AutoTextView.PlayCntListener() {
            @Override
            public void getPlayCnt(int cnt) {
                if (playcnt == cnt) {//即时消息 次数一到就隐藏
                    tLayout.setVisibility(View.GONE);
                }
            }
        });

        String timeLength = msgMap.get("timeLength");
        if (!TextUtils.isEmpty(timeLength)) {//字幕有播放时长时,时长生效(实际环境中没时长)
            timer = new Timer();
            msgTimerTask = new MsgTimerTask();
            timer.schedule(msgTimerTask, Long.valueOf(timeLength));
        }
    }

    class MsgTimerTask extends TimerTask {//关闭字幕时长的定时器任务

        @Override
        public void run() {
            mhandler.obtainMessage(1, "msg").sendToTarget();
            timer.cancel();
            msgTimerTask.cancel();
        }
    }

    //    处理eventbus传递来的消息
    @Subscribe(threadMode = ThreadMode.MAIN)//主线程中更新ui
    public void getMessage(StringModel stringModel) {
        ExtraMessage_SQL extra = null;
        String programFileName = "";//心跳得到的节目单名称

        try {
            extra = dbUtils.findFirst(ExtraMessage_SQL.class);
            if (extra == null) {
                extra = new ExtraMessage_SQL();
            }
            String key = stringModel.getKey();
            switch (key) {
                case "program"://在program文件下载完成后调用
                    String playMode = stringModel.getMsg();
                    programFileName = stringModel.getFileName();
                    if ("1".equals(playMode)) {//心跳同步到节目后,普通节目存数据库
                        extra.setPlayMode(playMode);
                        extra.setPgFileName(programFileName);
                        //save已存在保存不了。update库内无该记录。更新不了。aveOrUpdate 保存或者更新。 如果数据库已经存在了，则更新操作。如果数据库还没这条记录，则保存该记录。
                        dbUtils.saveOrUpdate(extra);
                    } else if ("0".equals(playMode)) {//紧急节目存sp里
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dateNowStr = sdf.format(d);
                        Log.e("------>", "格式化后的日期：" + dateNowStr);
                        SharedPrefsUtil.putValue(MainActivity.this, "date", dateNowStr);
                        SharedPrefsUtil.putValue(MainActivity.this, "playMode", playMode);
                        SharedPrefsUtil.putValue(MainActivity.this, "programName", programFileName);
                    }
                    isUpLoadProgram = true;
                    checkIsAblePlay(playMode, programFileName);
                    break;

                case "realtimemsg"://心跳接到即时消息后显示
                    msgMap = stringModel.getHashMap();
                    tLayout.setVisibility(View.VISIBLE);
                    String startTime = TextUtils.isEmpty(msgMap.get("startTime")) ? "null" : msgMap.get("startTime");//即时消息开始显示的时间
                    if ("null".equals(startTime)) {
                        initMessage(msgMap);
                    } else {
                        timer = new Timer();
                        msgStartTimerTask = new MsgStartTimerTask();
                        Date when = DateUtils.getDateByTime(startTime);
                        timer.schedule(msgStartTimerTask, when);
                    }
                    break;

                case "cancelrealtimemsg"://取消即时消息
                    tLayout.setVisibility(View.GONE);
                    break;

                case "monitorreport"://通知在播内容上报
                    String macAdress = stringModel.getMsg();
                    String ipAndPort = stringModel.getFileName();
                    ScreenUtils.screenShot(FileUtils.SDPATH + "old" + ".png");
                    Bitmap oldBp = BitmapFactory.decodeFile(FileUtils.SDPATH + "old" + ".png");
                    String base64 = ScreenUtils.bitmapToBase64(oldBp);
                    programFileName = programFileName.equals("") ? extra.getPgFileName() : programFileName;
                    ArrayList<String> resIdList = new ArrayList<>();
                    resIdList.add(resIdMap.get(path_full));
                    resIdList.add(resIdMap.get(path_second_area1));
                    resIdList.add(resIdMap.get(path_second_area2));
                    resIdList.add(resIdMap.get(path_three_area1));
                    resIdList.add(resIdMap.get(path_three_area2));
                    resIdList.add(resIdMap.get(path_three_area3));
                    resIdList.add(resIdMap.get(path_four_area1));
                    resIdList.add(resIdMap.get(path_four_area2));
                    resIdList.add(resIdMap.get(path_four_area3));
                    resIdList.add(resIdMap.get(path_four_area4));
                    parseManager.monitorReport(programFileName, base64, macAdress, ipAndPort, resIdList);
                    break;

                case "resource"://
                    String flag = stringModel.getMsg();
                    if (flag.equals("true")) {//资源下载成功,紧急的从sp里取
                        isUpLoadResource = true;
                        String programName = SharedPrefsUtil.getValue(MainActivity.this, "programName", "");
                        String mode = SharedPrefsUtil.getValue(MainActivity.this, "playMode", "");
                        checkIsAblePlay(mode, programName);
                    } else {//资源下载失败,不播放了
                    }
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    class MsgStartTimerTask extends TimerTask {// 即时消息 几点开始执行的定时任务

        @Override
        public void run() {
            mhandler.obtainMessage(2, "msgStartTime").sendToTarget();
            timer.cancel();
            msgStartTimerTask.cancel();
        }
    }

    private void checkIsAblePlay(String playMode, String programName) {
        if (isUpLoadProgram && isUpLoadResource) {//检查节目和资源都下载完后播放
            initData(playMode, programName);
            isUpLoadResource = false;
            isUpLoadProgram = false;
        }
    }

    private void initData(String playMode, String fileName) {//当有新的节目单被荡下来后,初始化集合和角标
        if (playMode.equals("0")) {
            index_full_one_c = 0;         //h  v 1.1 插播的角标
            index_second_area1_c = 0;     //h  v 2.1 插播的角标
            index_second_area2_c = 0;     //h  v 2.2 插播的角标
            index_three_area1_c = 0;      //h  v 3.1 插播的角标
            index_three_area2_c = 0;      //h  v 3.2 插播的角标
            index_three_area3_c = 0;      //h  v 3.3 插播的角标
            index_four_area1_c = 0;         //  4.1
            index_four_area2_c = 0;         //  4.2
            index_four_area3_c = 0;         //  4.3
            index_four_area4_c = 0;         //  4.4

            model_full_h_in.clear();
            model_second_h_area1_in.clear();
            model_second_h_area2_in.clear();
            model_three_h_area1_in.clear();
            model_three_h_area2_in.clear();
            model_three_h_area3_in.clear();
            model_four_h_area1_in.clear();
            model_four_h_area2_in.clear();
            model_four_h_area3_in.clear();
            model_four_h_area4_in.clear();

            model_full_h_in_lock = true;//1的插播
            model_second_h_in_lock = true;//2插播
            model_three_h_in_lock = true;//3插播
            model_four_h_in_lock = true;//4的插播

            model_full_h_default_lock = true;

        } else if (playMode.equals("1")) {
            index_full_one_p = 0;         //h  v 1.1 普通的角标
            index_full_one_m = 0;         //h  v 1.1 默认的角标
            index_second_area1_p = 0;     //h  v 2.1 普通的的角标
            index_second_area2_p = 0;     //h  v 2.2 普通的的角标
            index_three_area1_p = 0;      //h  v 3.1 普通的的角标
            index_three_area2_p = 0;      //h  v 3.2 普通的的角标
            index_three_area3_p = 0;      //h  v 3.3 普通的的角标
            index_four_area1_p = 0;         //  4.1
            index_four_area2_p = 0;         //  4.2
            index_four_area3_p = 0;         //  4.3
            index_four_area4_p = 0;         //  4.4

            model_full_h_default.clear();
            model_full_h_common.clear();
            model_second_h_area1_common.clear();
            model_second_h_area2_common.clear();
            model_three_h_area1_common.clear();
            model_three_h_area2_common.clear();
            model_three_h_area3_common.clear();
            model_four_h_area1_common.clear();
            model_four_h_area2_common.clear();
            model_four_h_area3_common.clear();
            model_four_h_area4_common.clear();

            model_full_h_common_lock = true;//1的普通
            model_second_h_common_lock = true;//2普通
            model_three_h_common_lock = true;//3普通
            model_four_h_common_lock = true;//4普通
        }

        if ("".equals(playMode) || "".equals(fileName)) {//播放模式或者节目单有空值不解析
            return;
        } else {
            String dirName = "xml";
            Program program = null;
            File workDir = getDir(dirName, Context.MODE_PRIVATE); //(在内存中创建app_xml文件夹)
            XStream xStream = new XStream();
            xStream.processAnnotations(Program.class);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有内存卡读内存卡
                if (FileUtils.isFileExist(dirName + fileName)) {
                    program = (Program) xStream.fromXML(new File(FileUtils.SDPATH + dirName + fileName));
                } else {
                    return;
                }
            } else {//否则读手机内存
                program = (Program) xStream.fromXML(new File(workDir.getAbsolutePath() + fileName));
            }

            ParseManager manager = new ParseManager(this);
            ArrayList<ArrayList> lists = manager.parseProgram(program, playMode);
            for (int i = 0; i < lists.size(); i++) {
                ArrayList<HashMap<String, String>> arrayList = lists.get(i);
                Collections.sort(arrayList, new MySort(true, "area", false));//按照area升续
                for (int j = 0; j < arrayList.size(); j++) {
                    HashMap<String, String> map = arrayList.get(j);
                    String area = map.get("area");
                    String areaType = map.get("areaType");//非空
                    try {
                        List<Model_SQL> modelList = dbUtils.findAll(Selector.from(Model_SQL.class).where("AreaType", "=", areaType));
                        if (modelList != null && modelList.size() == 1) {
                            if (map.get("normal") != null) {//正常播放
                                model_full_h_pls_stdTime = map.get("pls_stdTime");
                                model_full_h_pls_edTime = map.get("pls_edTime");
                                String link = map.get("path");
                                resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                model_full_h_common.add(link.substring(0, link.indexOf("|")));
                            }

                            if (map.get("insert") != null) {//播放模式为插播
                                model_full_h_in_pls_stdTime = map.get("pls_stdTime");
                                model_full_h_in_pls_edTime = map.get("pls_edTime");
                                SharedPrefsUtil.putValue(MainActivity.this, "pls_stdTime", model_full_h_in_pls_stdTime);//插播时间存入sp
                                SharedPrefsUtil.putValue(MainActivity.this, "pls_edTime", model_full_h_in_pls_edTime);
                                String link = map.get("path");
                                resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                model_full_h_in.add(link.substring(0, link.indexOf("|")));
                            }

                            if (map.get("default") != null) {//默认的视频路径
                                String link = map.get("path");
                                resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                model_full_h_default.add(link.substring(0, link.indexOf("|")));
                            }
                        } else if (modelList != null && modelList.size() == 2) {
                            if (modelList.get(0).getArea().equals(area)) {//1分区
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_second_h_area1_common.add(link.substring(0, link.indexOf("|")));
                                    model_second_h_pls_stdTime = map.get("pls_stdTime");
                                    model_second_h_pls_edTime = map.get("pls_edTime");
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    model_second_h_in_pls_stdTime = map.get("pls_stdTime");
                                    model_second_h_in_pls_edTime = map.get("pls_edTime");
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_stdTime", model_second_h_in_pls_stdTime);//插播时间存入sp
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_edTime", model_second_h_in_pls_edTime);
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_second_h_area1_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(1).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_second_h_area2_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_second_h_area2_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                        } else if (modelList != null && modelList.size() == 3) {
                            if (modelList.get(0).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    model_three_h_pls_stdTime = map.get("pls_stdTime");
                                    model_three_h_pls_edTime = map.get("pls_edTime");
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area1_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    model_three_h_in_pls_stdTime = map.get("pls_stdTime");
                                    model_three_h_in_pls_edTime = map.get("pls_edTime");
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_stdTime", model_three_h_in_pls_stdTime);//插播时间存入sp
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_edTime", model_three_h_in_pls_edTime);
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area1_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(1).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area2_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area2_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(2).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area3_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_three_h_area3_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }
                        } else if (modelList != null && modelList.size() == 4) {
                            if (modelList.get(0).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    model_four_h_pls_stdTime = map.get("pls_stdTime");
                                    model_four_h_pls_edTime = map.get("pls_edTime");
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area1_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    model_four_h_in_pls_stdTime = map.get("pls_stdTime");
                                    model_four_h_in_pls_edTime = map.get("pls_edTime");
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_stdTime", model_four_h_in_pls_stdTime);//插播时间存入sp
                                    SharedPrefsUtil.putValue(MainActivity.this, "pls_edTime", model_four_h_in_pls_edTime);
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area1_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(1).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area2_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area2_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(2).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area3_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area3_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }

                            if (modelList.get(3).getArea().equals(area)) {
                                if (map.get("normal") != null) {//正常播放
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area4_common.add(link.substring(0, link.indexOf("|")));
                                }

                                if (map.get("insert") != null) {//播放模式为插播
                                    String link = map.get("path");
                                    resIdMap.put(link.substring(0, link.indexOf("|")), link.substring(link.lastIndexOf("|") + 1, link.length()));
                                    model_four_h_area4_in.add(link.substring(0, link.indexOf("|")));
                                }
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (playMode.equals("1")) {
                playNotTimeList();//没有时间的入口
            }
        }
    }

    private void againIntiView() {//切换屏幕后初始化数据
        imgHandler.removeCallbacks(runnable11);//切换屏幕后全部初始化
        imgHandler.removeCallbacks(runnable22);
        imgHandler.removeCallbacks(runnable32);
        imgHandler.removeCallbacks(runnable33);
        imgHandler.removeCallbacks(runnable42);
        imgHandler.removeCallbacks(runnable43);
        imgHandler.removeCallbacks(runnable44);
        path_full = "";
        path_second_area1 = "";
        path_second_area2 = "";
        path_three_area1 = "";
        path_three_area2 = "";
        path_three_area3 = "";
        path_four_area1 = "";
        path_four_area2 = "";
        path_four_area3 = "";
        path_four_area4 = "";
        play_one.pause();
        play_one.stopPlayback();
        play_one.suspend();
        play_two.pause();
        play_two.stopPlayback();
        play_two.suspend();
        play_three.pause();
        play_three.stopPlayback();
        play_three.suspend();
        play_four.pause();
        play_four.stopPlayback();
        play_four.suspend();
    }

    private void playNotTimeList() {//入口:处理没有时间的普通播放(不能放线程里)
        if (model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("") && model_full_h_common_lock) {//1普通播放
            againIntiView();
            path_full = model_full_h_common.get(0);
            img_two.setVisibility(View.GONE);
            img_three.setVisibility(View.GONE);
            commonSelectPathOne(path_full);
            model_full_h_common_lock = false;
        } else if (model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_second_h_common_lock) {//2普通播放
            againIntiView();
            path_second_area1 = model_second_h_area1_common.get(0);
            img_three.setVisibility(View.GONE);
            commonSelectPathSecondOne(path_second_area1);
            if (model_second_h_area2_common.size() > 0) {//2.2普播
                path_second_area2 = model_second_h_area2_common.get(0);
                commonSelectPathSecondTwo(path_second_area2);
            }
            secondOneNoTimeBackLockFromPlayTwo = true;
            threeOneNoTimeBackLockFromPlayTwo = false;
            fourOneNoTimeBackLockFromPlayTwo = false;
            model_second_h_common_lock = false;
        } else if (model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("") && model_three_h_common_lock) {//3
            againIntiView();
            path_three_area1 = model_three_h_area1_common.get(0);
            commonSelectPathThreeOne(path_three_area1);
            if (model_three_h_area2_common.size() > 0) {//3.2
                path_three_area2 = model_three_h_area2_common.get(0);
                commonSelectPathThreeTwo(path_three_area2);
            }

            if (model_three_h_area3_common.size() > 0) {//3.3普通播放
                path_three_area3 = model_three_h_area3_common.get(0);//播放顺序播放里的第一个
                commonSelectPathThreeThree(path_three_area3);
            }
            threeOneNoTimeBackLockFromPlayTwo = true;
            secondOneNoTimeBackLockFromPlayTwo = false;
            fourOneNoTimeBackLockFromPlayTwo = false;
            model_three_h_common_lock = false;
        } else if (model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("") && model_four_h_common_lock) {//4
            againIntiView();
            path_four_area1 = model_four_h_area1_common.get(0);
            commonSelectPathFourOne(path_four_area1);
            if (model_four_h_area2_common.size() > 0) {//4.2
                path_four_area2 = model_four_h_area2_common.get(0);
                commonSelectPathFourTwo(path_four_area2);
            }

            if (model_four_h_area3_common.size() > 0) {//4.3普通播放
                path_four_area3 = model_four_h_area3_common.get(0);//播放顺序播放里的第一个
                commonSelectPathFourThree(path_four_area3);
            }
            if (model_four_h_area4_common.size() > 0) {//4.4
                path_four_area4 = model_four_h_area4_common.get(0);
                commonSelectPathFourFour(path_four_area4);
            }

            fourOneNoTimeBackLockFromPlayTwo = true;
            threeOneNoTimeBackLockFromPlayTwo = false;
            secondOneNoTimeBackLockFromPlayTwo = false;
            model_four_h_common_lock = false;
        }
    }

    private void playNotTiemtBack() {//1.1  2.1  3.1  4.1                                                   //只有一个pls保证从入口进入      //控制进入下一个pls
        if (model_full_h_pls_stdTime.equals("") && !model_full_h_common_lock && model_full_h_notime_lock) {//1 普播
            if (model_full_h_common.size() > 0) {
                if (model_full_h_common.size() == 1) {//只有一个model_full_h_common.size==1,只有一个pls节目单
                    if (model_second_h_area1_common.size() == 0 && model_three_h_area1_common.size() == 0 && model_four_h_area1_common.size() == 0) {
                        path_full = model_full_h_common.get(0);
                        commonSelectPathOne(path_full);
                    }

                    if (model_second_h_area1_common.size() > 0 && model_three_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_three_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//1234
                            || model_second_h_area1_common.size() > 0 && model_three_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_three_h_pls_stdTime.equals("")//123 有pls
                            || model_second_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//124 有pls
                            || model_three_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//134 有pls
                            || model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") //12
                            || model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("") //13
                            || model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//14

                        model_full_h_notime_lock = false;
                        playNotTimeList();//(只有一个pls的时候,入口已经播放过0了无需在播放第二次)
                    }
                } else {
                    path_full = model_full_h_common.get(index_full_one_p + 1);
                    commonSelectPathOne(path_full);
                    if (index_full_one_p == model_full_h_common.size() - 2) {//此时为播完一轮
                        index_full_one_p = -2;// 只有自己一个pls的时候 从头开播
                        playNoTimeOneOneEntry(path_full);
                    }
                    index_full_one_p++;
                }
            }
        } else if (model_second_h_pls_stdTime.equals("") && !model_second_h_common_lock && model_second_h_notime_lock) {//2.1普播
            if (model_second_h_area1_common.size() > 0) {
                if (model_second_h_area1_common.size() == 1) {//只有2 pls
                    if (model_full_h_common.size() == 0 && model_three_h_area1_common.size() == 0 && model_four_h_area1_common.size() == 0) {
                        path_second_area1 = model_second_h_area1_common.get(0);//size为1 pls为1
                        commonSelectPathSecondOne(path_second_area1);
                    }

                    if (model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("")) {//23
                        model_second_h_notime_lock = false;
                        playNotTimeList();
                    } else if (model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//24
                        model_second_h_notime_lock = false;
                        playNotTimeList();
                    } else if (model_three_h_area1_common.size() == 0 && model_four_h_area1_common.size() == 0) {//21
                        model_full_h_common_lock = true;
                        model_second_h_common_lock = true;

                        model_full_h_notime_lock = true;
                        playNotTimeList();
                    }
                } else {
                    path_second_area1 = model_second_h_area1_common.get(index_second_area1_p + 1);
                    commonSelectPathSecondOne(path_second_area1);
                    if (index_second_area1_p == model_second_h_area1_common.size() - 2) {//最后一轮
                        index_second_area1_p = -2;
                        playNoTimeTwoOneEntry(path_second_area1);
                    }
                    index_second_area1_p++;
                }
            }
        } else if (model_three_h_pls_stdTime.equals("") && !model_three_h_common_lock && model_three_h_notime_lock) {//3 普播
            if (model_three_h_area1_common.size() > 0) {
                if (model_three_h_area1_common.size() == 1) {//就一个res
                    if (model_full_h_common.size() == 0 && model_second_h_area1_common.size() == 0 && model_four_h_area1_common.size() == 0) {//就一个pls
                        path_three_area1 = model_three_h_area1_common.get(0);//pls为1 size为1
                        commonSelectPathThreeOne(path_three_area1);
                    }

                    if (model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//34
                        model_three_h_notime_lock = false;
                        playNotTimeList();
                    } else if (model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("")) {//31
                        model_full_h_common_lock = true;
                        model_three_h_common_lock = true;
                        model_full_h_notime_lock = true;
                        playNotTimeList();
                    } else if (model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("")) {//32
                        model_second_h_common_lock = true;
                        model_three_h_common_lock = true;
                        model_second_h_notime_lock = true;
                        playNotTimeList();
                    }
                } else {
                    path_three_area1 = model_three_h_area1_common.get(index_three_area1_p + 1);
                    commonSelectPathThreeOne(path_three_area1);//根据这个路径得到播放的时间
                    if (index_three_area1_p == model_three_h_area1_common.size() - 2) {
                        index_three_area1_p = -2;
                        playNoTimeThreeOneEntry(path_three_area1);
                    }
                    index_three_area1_p++;
                }
            }
        } else if (model_four_h_pls_stdTime.equals("") && !model_four_h_common_lock) {//普播4.1
            if (model_four_h_area1_common.size() > 0) {
                if (model_four_h_area1_common.size() == 1) {//就一个res
                    if (model_full_h_common.size() == 0 && model_second_h_area1_common.size() == 0 && model_three_h_area1_common.size() == 0) {//就一个pls
                        path_four_area1 = model_four_h_area1_common.get(0);//pls为1 size为1
                        commonSelectPathFourOne(path_four_area1);
                    }

                    if (model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("")//4.1
                            || model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("")//4.2
                            || model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("")) {//4.3
                        model_full_h_common_lock = true;
                        model_second_h_common_lock = true;
                        model_three_h_common_lock = true;
                        model_full_h_notime_lock = true;
                        model_second_h_notime_lock = true;
                        model_three_h_notime_lock = true;
                        playNotTimeList();
                    }
                } else {
                    path_four_area1 = model_four_h_area1_common.get(index_four_area1_p + 1);
                    commonSelectPathFourOne(path_four_area1);//根据这个路径得到播放的时间
                    if (index_four_area1_p == model_four_h_area1_common.size() - 2) {
                        index_four_area1_p = -2;
                        playNoTimeFourOneEntry(path_four_area1);
                    }
                    index_four_area1_p++;
                }
            }
        }
    }

    //普通播放没有时间的最后一轮定时器
    class RequestTimerTask extends TimerTask {
        public void run() {
            onlyPlayNotTimeAndHaveTime = true;
            mhandler.obtainMessage(0, "playNotTimeList").sendToTarget();
            timer.cancel();
            task.cancel();
        }
    }

    //1.1无时间最后一轮播放完毕的回调
    private void playNoTimeOneOneEntry(String path) {
        String duration = getLastPlayTime(path);
        if (model_second_h_area1_common.size() > 0 && model_three_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_three_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//1234 有pls
                || model_second_h_area1_common.size() > 0 && model_three_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_three_h_pls_stdTime.equals("")//123 有pls
                || model_second_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//124 有pls
                || model_three_h_area1_common.size() > 0 && model_four_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("") && model_four_h_pls_stdTime.equals("")//134 有pls
                || model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("") //12　有pls
                || model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("") //13　有pls
                || model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//14　有pls

            index_full_one_p = -1;//回播1的时候，0去入口播 回调处播1
            model_full_h_notime_lock = false;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1000" : duration) + 3000);
        }
    }

    private void playNoTimeTwoOneEntry(String path) {
        String duration = getLastPlayTime(path);
        if (model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("")//23
                || model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//24
            index_second_area1_p = -1;//从入口进的要置为-1
            model_second_h_notime_lock = false;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        } else if (model_three_h_area1_common.size() == 0 && model_four_h_area1_common.size() == 0 && model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("")) {//21
            index_second_area1_p = -1;
            model_full_h_common_lock = true;
            model_second_h_common_lock = true;
            model_full_h_notime_lock = true;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        }
    }

    private void playNoTimeThreeOneEntry(String path) {
        String duration = getLastPlayTime(path);
        if (model_four_h_area1_common.size() > 0 && model_four_h_pls_stdTime.equals("")) {//34
            model_three_h_notime_lock = false;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        } else if (model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("")) {//31
            model_full_h_common_lock = true;
            model_three_h_common_lock = true;
            model_full_h_notime_lock = true;
            index_three_area1_p = -1;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        } else if (model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("")) {//32
            model_second_h_common_lock = true;
            model_three_h_common_lock = true;
            model_second_h_notime_lock = true;
            index_three_area1_p = -1;
            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        }
    }

    private void playNoTimeFourOneEntry(String path) {
        String duration = getLastPlayTime(path);
        if (model_full_h_common.size() > 0 && model_full_h_pls_stdTime.equals("")//4.1
                || model_second_h_area1_common.size() > 0 && model_second_h_pls_stdTime.equals("")//4.2
                || model_three_h_area1_common.size() > 0 && model_three_h_pls_stdTime.equals("")) {//4.3

            model_full_h_common_lock = true;
            model_second_h_common_lock = true;
            model_three_h_common_lock = true;
            model_four_h_common_lock = true;

            model_full_h_notime_lock = true;
            model_second_h_notime_lock = true;
            model_three_h_notime_lock = true;

            index_three_area1_p = -1;

            timer = new Timer();
            task = new RequestTimerTask();
            timer.schedule(task, Long.valueOf(duration == null ? "1" : duration) + 3000);
        }
    }

    private void playNotTimeSecondtwo() {//2.2 back
        if (model_second_h_pls_stdTime.equals("")) {//2.2普播
            if (model_second_h_area2_common.size() > 0) {
                if (model_second_h_area2_common.size() == 1) {
                    path_second_area2 = model_second_h_area2_common.get(0);
                    commonSelectPathSecondTwo(path_second_area2);
                } else {
                    path_second_area2 = model_second_h_area2_common.get(index_second_area2_p + 1);
                    commonSelectPathSecondTwo(path_second_area2);
                    if (index_second_area2_p == model_second_h_area2_common.size() - 2) {
                        index_second_area2_p = -2;
                    }
                    index_second_area2_p++;
                }
            }
        }
    }

    private void playNotTimeThreetwo() {//3.2 back
        if (model_three_h_pls_stdTime.equals("")) {//3.2普播
            if (model_three_h_area2_common.size() > 0) {
                if (model_three_h_area2_common.size() == 1) {
                    path_three_area2 = model_three_h_area2_common.get(0);
                    commonSelectPathThreeTwo(path_three_area2);
                } else {
                    path_three_area2 = model_three_h_area2_common.get(index_three_area2_p + 1);
                    commonSelectPathThreeTwo(path_three_area2);
                    if (index_three_area2_p == model_three_h_area2_common.size() - 2) {
                        index_three_area2_p = -2;
                    }
                    index_three_area2_p++;
                }
            }
        }
    }

    private void playNotTimeThreethree() {// 3.3back
        if (model_three_h_pls_stdTime.equals("")) {
            if (model_three_h_area3_common.size() > 0) {//3.3普播
                if (model_three_h_area3_common.size() == 1) {
                    commonSelectPathThreeThree(path_three_area3);
                } else {
                    path_three_area3 = model_three_h_area3_common.get(index_three_area3_p + 1);
                    commonSelectPathThreeThree(path_three_area3);
                    if (index_three_area3_p == model_three_h_area3_common.size() - 2) {
                        index_three_area3_p = -2;
                    }
                    index_three_area3_p++;
                }
            }
        }
    }

    private void playNotTimeFourTwo() {//4.2back
        if (model_four_h_pls_stdTime.equals("")) {
            if (model_four_h_area2_common.size() > 0) {
                if (model_four_h_area2_common.size() == 1) {
                    path_four_area2 = model_four_h_area2_common.get(0);
                    commonSelectPathFourTwo(path_four_area2);
                } else {
                    path_four_area2 = model_four_h_area2_common.get(index_four_area2_p + 1);
                    commonSelectPathFourTwo(path_four_area2);
                    if (index_four_area2_p == model_four_h_area2_common.size() - 2) {
                        index_four_area2_p = -2;
                    }
                    index_four_area2_p++;
                }
            }
        }
    }

    private void playNotTimeFourThree() {//4.3
        if (model_four_h_pls_stdTime.equals("")) {
            if (model_four_h_area3_common.size() > 0) {
                if (model_four_h_area3_common.size() == 1) {
                    path_four_area3 = model_four_h_area3_common.get(0);
                    commonSelectPathFourThree(path_four_area3);
                } else {
                    path_four_area3 = model_four_h_area3_common.get(index_four_area3_p + 1);
                    commonSelectPathFourThree(path_four_area3);
                    if (index_four_area3_p == model_four_h_area3_common.size() - 2) {
                        index_four_area3_p = -2;
                    }
                    index_four_area3_p++;
                }
            }
        }
    }

    private void playNotTimeFourFour() {//4.4
        if (model_four_h_pls_stdTime.equals("")) {
            if (model_four_h_area4_common.size() > 0) {
                if (model_four_h_area4_common.size() == 1) {
                    path_four_area4 = model_four_h_area4_common.get(0);
                    commonSelectPathFourFour(path_four_area4);
                } else {
                    path_four_area4 = model_four_h_area4_common.get(index_four_area4_p + 1);
                    commonSelectPathFourFour(path_four_area4);
                    if (index_four_area4_p == model_four_h_area4_common.size() - 2) {
                        index_four_area4_p = -2;
                    }
                    index_four_area4_p++;
                }
            }
        }
    }

    //带时间的【扫描线程】
    private void playListInit() {
        Thread threadplay = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!stopThread) {
                    play_mode_full();
                }
                handler.postDelayed(this, 30000);//每隔30秒检查一遍时间是否到了
            }
        });
        threadplay.start();
    }

    private void unLockNoTimeOrdinary(String dateTime, long duration) {//(插播和没有时间的普通播放)解开(没时间的普通播放)
        try {//时间一到解开让没时间的pls播放
            Date when = null;
            if (!dateTime.equals("")) {
                when = DateUtils.getDateByTime(dateTime);
            }
            timer = new Timer();
            fromInsertTimer = new FromInsertTimerTask();
            if (duration == -1) {
                timer.schedule(fromInsertTimer, when);
            } else {
                timer.schedule(fromInsertTimer, duration);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class FromInsertTimerTask extends TimerTask {//【插播/有时间的普通播】--->无时间的普通播放【重新初始化】

        public void run() {
            model_full_h_common_lock = true;
            model_second_h_common_lock = true;
            model_three_h_common_lock = true;
            model_four_h_common_lock = true;

            model_full_h_notime_lock = true;
            model_second_h_notime_lock = true;
            model_three_h_notime_lock = true;

            index_full_one_p = 0;
            index_second_area1_p = 0;
            index_second_area2_p = 0;
            index_three_area1_p = 0;
            index_three_area2_p = 0;
            index_three_area3_p = 0;
            index_four_area1_p = 0;
            index_four_area2_p = 0;
            index_four_area3_p = 0;
            index_four_area4_p = 0;

            onlyPlayNotTimeAndHaveTime = true;
            mhandler.obtainMessage(0, "playNotTimeList").sendToTarget();
            timer.cancel();
            fromInsertTimer.cancel();
        }
    }

    //1  2  3  4共同的入口
    private void play_mode_full() {
        if (model_full_h_in.size() > 0 && DateUtils.isInsertPlayIntime(model_full_h_in_pls_stdTime, model_full_h_in_pls_edTime) && model_full_h_in_lock) {//1 插播 只有一个pls (都是轮播)
            againIntiView();//再次初始化控件
            path_full = model_full_h_in.get(0);
            mhandler.obtainMessage(1111, path_full).sendToTarget();
            onlyPlayNotTimeAndHaveTime = false;
            if (!model_full_h_in_pls_edTime.equals("")) {
                unLockNoTimeOrdinary(model_full_h_in_pls_edTime, -1);
            }
            model_full_h_in_lock = false;
        } else if (model_second_h_area1_in.size() > 0 && DateUtils.isInsertPlayIntime(model_second_h_in_pls_stdTime, model_second_h_in_pls_edTime) && model_second_h_in_lock) {//2 的插播
            againIntiView();//再次初始化控件
            path_second_area1 = model_second_h_area1_in.get(0);
            mhandler.obtainMessage(2211, path_second_area1).sendToTarget();
            if (model_second_h_area2_in.size() > 0) {//2.2插播
                path_second_area2 = model_second_h_area2_in.get(0);
                mhandler.obtainMessage(2222, path_second_area2).sendToTarget();
            }
            onlyPlayNotTimeAndHaveTime = false;
            if (!model_second_h_in_pls_edTime.equals("")) {
                unLockNoTimeOrdinary(model_second_h_in_pls_edTime, -1);
            }
            model_second_h_in_lock = false;//2 插入锁 共用
        } else if (model_three_h_area1_in.size() > 0 && DateUtils.isInsertPlayIntime(model_three_h_in_pls_stdTime, model_three_h_in_pls_edTime) && model_three_h_in_lock) {//3 插播
            againIntiView();//再次初始化控件
            path_three_area1 = model_three_h_area1_in.get(0);
            mhandler.obtainMessage(3311, path_three_area1).sendToTarget();

            if (model_three_h_area2_in.size() > 0) {//3.2插播
                path_three_area2 = model_three_h_area2_in.get(0);
                mhandler.obtainMessage(3322, path_three_area2).sendToTarget();
            }
            if (model_three_h_area3_in.size() > 0) {//3.3插播
                path_three_area3 = model_three_h_area3_in.get(0);
                mhandler.obtainMessage(3333, path_three_area3).sendToTarget();
            }

            onlyPlayNotTimeAndHaveTime = false;
            if (!model_three_h_in_pls_edTime.equals("")) {
                unLockNoTimeOrdinary(model_three_h_in_pls_edTime, -1);
            }
            model_three_h_in_lock = false;
        } else if (model_four_h_area1_in.size() > 0 && DateUtils.isInsertPlayIntime(model_four_h_in_pls_stdTime, model_four_h_in_pls_edTime) && model_four_h_in_lock) { // 4 插播
            againIntiView();
            path_four_area1 = model_four_h_area1_in.get(0);
            mhandler.obtainMessage(4411, path_four_area1).sendToTarget();
            if (model_four_h_area2_in.size() > 0) {
                path_four_area2 = model_four_h_area2_in.get(0);
                mhandler.obtainMessage(4422, path_four_area2).sendToTarget();
            }
            if (model_four_h_area3_in.size() > 0) {
                path_four_area3 = model_four_h_area3_in.get(0);
                mhandler.obtainMessage(4433, path_four_area3).sendToTarget();
            }
            if (model_four_h_area4_in.size() > 0) {
                path_four_area4 = model_four_h_area4_in.get(0);
                mhandler.obtainMessage(4444, path_four_area4).sendToTarget();
            }
            onlyPlayNotTimeAndHaveTime = false;
            if (!model_four_h_in_pls_edTime.equals("")) {
                unLockNoTimeOrdinary(model_four_h_in_pls_edTime, -1);
            }
            model_four_h_in_lock = false;

        } else if (model_full_h_common.size() > 0 && DateUtils.isIntime(model_full_h_pls_stdTime, model_full_h_pls_edTime) && model_full_h_common_lock && onlyPlayNotTimeAndHaveTime) {//1 普通播放
            //pls的时间为""进不去
            againIntiView();//再次初始化控件
            path_full = model_full_h_common.get(0);
            mhandler.obtainMessage(1111, path_full).sendToTarget();
            onlyPlayNotTimeAndHaveTime = false;
            unLockNoTimeOrdinary(model_full_h_pls_edTime, -1);
            model_full_h_common_lock = false;
        } else if (model_second_h_area1_common.size() > 0 && DateUtils.isIntime(model_second_h_pls_stdTime, model_second_h_pls_edTime) && model_second_h_common_lock && onlyPlayNotTimeAndHaveTime) {//2 普通播放
            againIntiView();//再次初始化控件
            path_second_area1 = model_second_h_area1_common.get(0);
            mhandler.obtainMessage(2211, path_second_area1).sendToTarget();
            if (model_second_h_area2_common.size() > 0) {//2.2普播
                path_second_area2 = model_second_h_area2_common.get(0);
                mhandler.obtainMessage(2222, path_second_area2).sendToTarget();
            }
            onlyPlayNotTimeAndHaveTime = false;
            unLockNoTimeOrdinary(model_second_h_pls_edTime, -1);
            model_second_h_common_lock = false;//2 普通锁 共用
        } else if (model_three_h_area1_common.size() > 0 && DateUtils.isIntime(model_three_h_pls_stdTime, model_three_h_pls_edTime) && model_three_h_common_lock && onlyPlayNotTimeAndHaveTime) { //3 普通播放
            againIntiView();//再次初始化控件
            path_three_area1 = model_three_h_area1_common.get(0);
            mhandler.obtainMessage(3311, path_three_area1).sendToTarget();
            if (model_three_h_area2_common.size() > 0) {//3.2普通
                path_three_area2 = model_three_h_area2_common.get(0);
                mhandler.obtainMessage(3322, path_three_area2).sendToTarget();
            }

            if (model_three_h_area3_common.size() > 0) {//3.3普通播放
                path_three_area3 = model_three_h_area3_common.get(0);//播放顺序播放里的第一个
                mhandler.obtainMessage(3333, path_three_area3).sendToTarget();
            }
            unLockNoTimeOrdinary(model_three_h_pls_edTime, -1);
            onlyPlayNotTimeAndHaveTime = false;
            model_three_h_common_lock = false;//3 的普通锁 共用
        } else if (model_four_h_area1_common.size() > 0 && DateUtils.isIntime(model_four_h_pls_stdTime, model_four_h_pls_edTime) && model_four_h_common_lock) {
            againIntiView();
            path_four_area1 = model_four_h_area1_common.get(0);
            mhandler.obtainMessage(4411, path_four_area1).sendToTarget();
            if (model_four_h_area2_common.size() > 0) {
                path_four_area2 = model_four_h_area2_common.get(0);
                mhandler.obtainMessage(4422, path_four_area2).sendToTarget();
            }
            if (model_four_h_area3_common.size() > 0) {
                path_four_area3 = model_four_h_area3_common.get(0);
                mhandler.obtainMessage(4433, path_four_area3).sendToTarget();
            }
            if (model_four_h_area4_common.size() > 0) {
                path_four_area4 = model_four_h_area4_common.get(0);
                mhandler.obtainMessage(4444, path_four_area4).sendToTarget();
            }
            onlyPlayNotTimeAndHaveTime = false;
            if (!model_four_h_pls_edTime.equals("")) {
                unLockNoTimeOrdinary(model_four_h_pls_edTime, -1);
            }
            model_four_h_common_lock = false;
        }/* else if (model_full_h_default.size() > 0 && model_full_h_default_lock) {//1 【默认播放暂时被替代了】
            path_full = model_full_h_default.get(0);
            Log.e("play_mode_full----", path_full);
            mhandler.obtainMessage(1111, path_full).sendToTarget();
            model_full_h_default_lock = false;
        }*/

    }

    public String getLastPlayTime(String path) {
        type = mediaType.getMediaFileType(path);//path_full 改变 type就要改
        String duration = null;
        if (type == 1) {
            duration = String.valueOf(imgCnt * imgTime * 1000);
        } else if (type == 2) {
            try {
                mmr.setDataSource(path);//视频路径错误,捕获异常跳过
            } catch (Exception e) {
            }
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        } else if (type == 3) {
            duration = String.valueOf("8000");//网页
        }
        return duration == null ? "20000" : duration;//如果没有识别出来图片还是视频，两秒后回调
    }

    //1.1 2.1 3.1 4.1的回调(主线程)
    private void playBack_model_full() {
        if (DateUtils.isInsertPlayIntime(model_full_h_in_pls_stdTime, model_full_h_in_pls_edTime) && model_full_h_in.size() > 0 && !model_full_h_in_lock) {//1.1 插播加此锁 限制为从入口进去
            if (model_full_h_in.size() == 1) {
                if (model_full_h_in_pls_stdTime.equals("") && model_full_h_in_pls_edTime.equals("")) {//只有一个且没有时间
                    model_full_h_in.clear();
                    unLockNoTimeOrdinary("", 1000);//调不带时间的普通pls(入口已播放过,直接进没时间的普通播放)
                } else {
                    path_full = model_full_h_in.get(0);
                    commonSelectPathOne(path_full);
                }
            } else {
                path_full = model_full_h_in.get(index_full_one_c + 1);//自己增加
                commonSelectPathOne(path_full);
                if (index_full_one_c == model_full_h_in.size() - 2) {
                    index_full_one_c = -2;
                    if (model_full_h_in_pls_stdTime.equals("") && model_full_h_in_pls_edTime.equals("")) {//插播不带时间,只播一次
                        model_full_h_in.clear();
                        index_full_one_c = -1;
                        String duration = getLastPlayTime(path_full);
                        unLockNoTimeOrdinary("", Long.valueOf(duration) + 5000);//给5秒缓冲
                    }
                }
                index_full_one_c++;
            }
        } else if (DateUtils.isInsertPlayIntime(model_second_h_in_pls_stdTime, model_second_h_in_pls_edTime) && model_second_h_area1_in.size() > 0 && !model_second_h_in_lock) {//2.1 插播
            if (model_second_h_area1_in.size() == 1) {
                if (model_second_h_in_pls_stdTime.equals("") && model_second_h_in_pls_edTime.equals("")) {
                    model_second_h_area1_in.clear();
                    unLockNoTimeOrdinary("", 1000);
                } else {
                    path_second_area1 = model_second_h_area1_in.get(0);
                    commonSelectPathSecondOne(path_second_area1);
                }
            } else {
                path_second_area1 = model_second_h_area1_in.get(index_second_area1_c + 1);
                commonSelectPathSecondOne(path_second_area1);
                if (index_second_area1_c == model_second_h_area1_in.size() - 2) {
                    index_second_area1_c = -2;
                    if (model_second_h_in_pls_stdTime.equals("") && model_second_h_in_pls_edTime.equals("")) {//插播不带时间,只播一次
                        model_second_h_area1_in.clear();
                        onlyPlayNotTimeAndHaveTime = true;
                        index_second_area1_c = -1;
                        String duration = getLastPlayTime(path_second_area1);
                        unLockNoTimeOrdinary("", Long.valueOf(duration) + 5000);
                    }
                }
                index_second_area1_c++;
            }

        } else if (DateUtils.isInsertPlayIntime(model_three_h_in_pls_stdTime, model_three_h_in_pls_edTime) && model_three_h_area1_in.size() > 0 && !model_three_h_in_lock) {//3.1插
            if (model_three_h_area1_in.size() == 1) {
                if (model_three_h_in_pls_stdTime.equals("") && model_three_h_in_pls_edTime.equals("")) {
                    model_three_h_area1_in.clear();
                    unLockNoTimeOrdinary("", 1000);
                } else {
                    path_three_area1 = model_three_h_area1_in.get(0);
                    commonSelectPathThreeOne(path_three_area1);
                }
            } else {
                path_three_area1 = model_three_h_area1_in.get(index_three_area1_c + 1);
                commonSelectPathThreeOne(path_three_area1);
                if (index_three_area1_c == model_three_h_area1_in.size() - 2) {
                    index_three_area1_c = -2;
                    if (model_three_h_in_pls_stdTime.equals("") && model_three_h_in_pls_edTime.equals("")) {//插播不带时间,只播一次
                        model_three_h_area1_in.clear();
                        index_three_area1_c = -1;
                        String duration = getLastPlayTime(path_three_area1);
                        unLockNoTimeOrdinary("", Long.valueOf(duration) + 5000);
                    }
                }
                index_three_area1_c++;
            }
        } else if (DateUtils.isInsertPlayIntime(model_four_h_in_pls_stdTime, model_four_h_in_pls_edTime) && model_four_h_area1_in.size() > 0 && !model_four_h_in_lock) {//4.1插播
            if (model_four_h_area1_in.size() == 1) {
                if (model_four_h_in_pls_stdTime.equals("") && model_four_h_in_pls_edTime.equals("")) {
                    model_four_h_area1_in.clear();
                    unLockNoTimeOrdinary("", 1000);
                } else {
                    path_four_area1 = model_four_h_area1_in.get(0);
                    commonSelectPathFourOne(path_four_area1);
                }
            } else {
                path_four_area1 = model_four_h_area1_in.get(index_four_area1_c + 1);
                commonSelectPathFourOne(path_four_area1);
                if (index_four_area1_c == model_four_h_area1_in.size() - 2) {
                    index_four_area1_c = -2;
                    if (model_four_h_in_pls_stdTime.equals("") && model_four_h_in_pls_edTime.equals("")) {//插播不带时间,只播一次
                        model_four_h_area1_in.clear();
                        index_four_area1_c = -1;
                        String duration = getLastPlayTime(path_four_area1);
                        unLockNoTimeOrdinary("", Long.valueOf(duration) + 5000);
                    }
                }
                index_four_area1_c++;
            }

        } else if (DateUtils.isIntime(model_full_h_pls_stdTime, model_full_h_pls_edTime) && !model_full_h_common_lock) {//1.1 普播
            if (model_full_h_common.size() > 0) {
                if (model_full_h_common.size() == 1) {
                    path_full = model_full_h_common.get(0);
                    commonSelectPathOne(path_full);
                } else {
                    path_full = model_full_h_common.get(index_full_one_p + 1);
                    commonSelectPathOne(path_full);
                    if (index_full_one_p == model_full_h_common.size() - 2) {
                        index_full_one_p = -2;//角标为3时  已经把4播放了
                    }
                    index_full_one_p++;
                }
            }
        } else if (DateUtils.isIntime(model_second_h_pls_stdTime, model_second_h_pls_edTime) && !model_second_h_common_lock) {//2.1普播
            if (model_second_h_area1_common.size() > 0) {
                if (model_second_h_area1_common.size() == 1) {
                    path_second_area1 = model_second_h_area1_common.get(0);
                    commonSelectPathSecondOne(path_second_area1);
                } else {
                    path_second_area1 = model_second_h_area1_common.get(index_second_area1_p + 1);
                    commonSelectPathSecondOne(path_second_area1);
                    if (index_second_area1_p == model_second_h_area1_common.size() - 2) {
                        index_second_area1_p = -2;
                    }
                    index_second_area1_p++;
                }
            }
        } else if (DateUtils.isIntime(model_three_h_pls_stdTime, model_three_h_pls_edTime) && !model_three_h_common_lock) {//3.1普播
            if (model_three_h_area1_common.size() > 0) {
                if (model_three_h_area1_common.size() == 1) {
                    path_three_area1 = model_three_h_area1_common.get(0);
                    commonSelectPathThreeOne(path_three_area1);
                } else {
                    path_three_area1 = model_three_h_area1_common.get(index_three_area1_p + 1);
                    commonSelectPathThreeOne(path_three_area1);
                    if (index_three_area1_p == model_three_h_area1_common.size() - 2) {
                        index_three_area1_p = -2;
                    }
                    index_three_area1_p++;
                }
            }
        } else if (DateUtils.isIntime(model_four_h_pls_stdTime, model_four_h_pls_edTime) && !model_four_h_common_lock) {//4.1普播
            if (model_four_h_area1_common.size() > 0) {
                if (model_four_h_area1_common.size() == 1) {
                    path_four_area1 = model_four_h_area1_common.get(0);
                    commonSelectPathFourOne(path_four_area1);
                } else {
                    path_four_area1 = model_four_h_area1_common.get(index_four_area1_p + 1);
                    commonSelectPathFourOne(path_four_area1);
                    if (index_four_area1_p == model_four_h_area1_common.size() - 2) {
                        index_four_area1_p = -2;
                    }
                    index_four_area1_p++;
                }
            }
        } else if (model_full_h_default.size() > 0 && !model_full_h_default_lock) {//（1.1默认播）
            if (model_full_h_default.size() == 1) {//only one
                path_full = model_full_h_default.get(0);
                commonSelectPathOne(path_full);//path_full
            } else {
                path_full = model_full_h_default.get(index_full_one_m + 1);//自己增加
                commonSelectPathOne(path_full);
                if (index_full_one_m == model_full_h_default.size() - 2) {
                    index_full_one_m = -2;
                }
                index_full_one_m++;
            }
        }
    }

    //2.2的插播 和带时间的普通播放的回调 （2.1控制）
    private void playBack_mode_second_two() {
        if (DateUtils.isInsertPlayIntime(model_second_h_in_pls_stdTime, model_second_h_in_pls_edTime) && model_second_h_area1_in.size()>0 && model_second_h_area2_in.size() > 0 && !model_second_h_in_lock) {//2插
            if (model_second_h_area2_in.size() == 1) {
                path_second_area2 = model_second_h_area2_in.get(0);
                commonSelectPathSecondTwo(path_second_area2);
            } else {
                path_second_area2 = model_second_h_area2_in.get(index_second_area2_c + 1);
                commonSelectPathSecondTwo(path_second_area2);
                if (index_second_area2_c == model_second_h_area2_in.size() - 2) {
                    index_second_area2_c = -2;
                    if (model_second_h_in_pls_stdTime.equals("") && model_second_h_in_pls_edTime.equals("")) {
                        index_second_area2_c = -1;
                        model_second_h_area2_in.clear();
                    }
                }
                index_second_area2_c++;
            }
        } else if (model_second_h_area2_common.size() > 0 && DateUtils.isIntime(model_second_h_pls_stdTime, model_second_h_pls_edTime) && !model_second_h_common_lock) {//2.2普播
            if (model_second_h_area2_common.size() == 1) {
                path_second_area2 = model_second_h_area2_common.get(0);
                commonSelectPathSecondTwo(path_second_area2);
            } else {
                path_second_area2 = model_second_h_area2_common.get(index_second_area2_p + 1);
                commonSelectPathSecondTwo(path_second_area2);
                if (index_second_area2_p == model_second_h_area2_common.size() - 2) {
                    index_second_area2_p = -2;
                }
                index_second_area2_p++;
            }
        }
    }

    //3.2的插播 和带时间的普通播放的回调 （3.1控制）
    private void playBack_mode_three_two() {//3.2的回调
        if (DateUtils.isInsertPlayIntime(model_three_h_in_pls_stdTime, model_three_h_in_pls_edTime) && model_three_h_area1_in.size()>0 && model_three_h_area2_in.size() > 0 && !model_three_h_in_lock) {//3.2插播
            if (model_three_h_area2_in.size() == 1) {
                path_three_area2 = model_three_h_area2_in.get(0);
                commonSelectPathThreeTwo(path_three_area2);
            } else {
                path_three_area2 = model_three_h_area2_in.get(index_three_area2_c + 1);
                commonSelectPathThreeTwo(path_three_area2);
                if (index_three_area2_c == model_three_h_area2_in.size() - 2) {
                    index_three_area2_c = -2;
                    if (model_three_h_in_pls_stdTime.equals("") && model_three_h_in_pls_edTime.equals("")) {
                        index_three_area2_c = -1;
                        model_three_h_area2_in.clear();
                    }
                }
                index_three_area2_c++;
            }
        } else if (model_three_h_area2_common.size() > 0 && DateUtils.isIntime(model_three_h_pls_stdTime, model_three_h_pls_edTime) && !model_three_h_common_lock) {//3.2普播
            if (model_three_h_area2_common.size() == 1) {
                path_three_area2 = model_three_h_area2_common.get(0);
                commonSelectPathThreeTwo(path_three_area2);
            } else {
                path_three_area2 = model_three_h_area2_common.get(index_three_area2_p + 1);
                commonSelectPathThreeTwo(path_three_area2);
                if (index_three_area2_p == model_three_h_area2_common.size() - 2) {
                    index_three_area2_p = -2;
                }
                index_three_area2_p++;
            }
        }
    }

    //3.3的插播 和带时间的普通播放的回调 （3.1控制）
    private void playBack_mode_three_three() {
        if (DateUtils.isInsertPlayIntime(model_three_h_in_pls_stdTime, model_three_h_in_pls_edTime) && model_three_h_area1_in.size() > 0 && model_three_h_area3_in.size() > 0 && !model_three_h_in_lock) {//3.3插
            if (model_three_h_area3_in.size() == 1) {
                path_three_area3 = model_three_h_area3_in.get(0);
                commonSelectPathThreeThree(path_three_area3);
            } else {
                path_three_area3 = model_three_h_area3_in.get(index_three_area3_c + 1);
                commonSelectPathThreeThree(path_three_area3);
                if (index_three_area3_c == model_three_h_area3_in.size() - 2) {
                    index_three_area3_c = -2;
                    if (model_three_h_in_pls_stdTime.equals("") && model_three_h_in_pls_edTime.equals("")) {
                        index_three_area3_c = -1;
                        model_three_h_area3_in.clear();
                    }
                }
                index_three_area3_c++;
            }

        } else if (model_three_h_area3_common.size() > 0 && DateUtils.isIntime(model_three_h_pls_stdTime, model_three_h_pls_edTime) && !model_three_h_common_lock) {//3.3普通
            if (model_three_h_area3_common.size() == 1) {
                commonSelectPathThreeThree(path_three_area3);
            } else {
                path_three_area3 = model_three_h_area3_common.get(index_three_area3_p + 1);
                commonSelectPathThreeThree(path_three_area3);
                if (index_three_area3_p == model_three_h_area3_common.size() - 2) {
                    index_three_area3_p = -2;
                }
                index_three_area3_p++;
            }
        }
    }
    //4.2的插播 和带时间的普通播放的回调 （4.1的大小控制）
    private void playBack_mode_four_two() {
        if (DateUtils.isInsertPlayIntime(model_four_h_in_pls_stdTime, model_four_h_in_pls_edTime) && model_four_h_area1_in.size() > 0 && model_four_h_area2_in.size() > 0 && !model_four_h_in_lock) {//4.2插
            if (model_four_h_area2_in.size() == 1) {
                path_four_area2 = model_four_h_area2_in.get(0);
                commonSelectPathFourTwo(path_four_area2);
            } else {
                path_four_area2 = model_four_h_area2_in.get(index_four_area2_c + 1);
                commonSelectPathFourTwo(path_four_area2);
                if (index_four_area2_c == model_four_h_area2_in.size() - 2) {
                    index_four_area2_c = -2;
                    if (model_four_h_in_pls_stdTime.equals("") && model_four_h_in_pls_edTime.equals("")) {
                        index_four_area2_c = -1;
                        model_four_h_area2_in.clear();
                    }
                }
                index_four_area2_c++;
            }
        } else if (model_four_h_area2_common.size() > 0 && DateUtils.isIntime(model_four_h_pls_stdTime, model_four_h_pls_edTime) && !model_four_h_common_lock) {//4.2普播
            if (model_four_h_area2_common.size() == 1) {
                path_four_area2 = model_four_h_area2_common.get(0);
                commonSelectPathFourTwo(path_four_area2);
            } else {
                path_four_area2 = model_four_h_area2_common.get(index_four_area2_p + 1);
                commonSelectPathFourTwo(path_four_area2);
                if (index_four_area2_p == model_four_h_area2_common.size() - 2) {
                    index_four_area2_p = -2;
                }
                index_four_area2_p++;
            }
        }
    }

    //4.3的插播 和带时间的普通播放的回调 （4.1的大小控制）
    private void playBack_mode_four_three() {
        if (DateUtils.isInsertPlayIntime(model_four_h_in_pls_stdTime, model_four_h_in_pls_edTime) && model_four_h_area1_in.size() > 0 && model_four_h_area3_in.size() > 0 && !model_four_h_in_lock) {//4.3插
            if (model_four_h_area3_in.size() == 1) {
                path_four_area3 = model_four_h_area3_in.get(0);
                commonSelectPathFourThree(path_four_area3);
            } else {
                path_four_area3 = model_four_h_area3_in.get(index_four_area3_c + 1);
                commonSelectPathFourThree(path_four_area3);
                if (index_four_area3_c == model_four_h_area3_in.size() - 2) {
                    index_four_area3_c = -2;
                    if (model_four_h_in_pls_stdTime.equals("") && model_four_h_in_pls_edTime.equals("")) {
                        index_four_area3_c = -1;
                        model_four_h_area3_in.clear();
                    }
                }
                index_four_area3_c++;
            }
        } else if (model_four_h_area3_common.size() > 0 && DateUtils.isIntime(model_four_h_pls_stdTime, model_four_h_pls_edTime) && !model_four_h_common_lock) {//4.3普通
            if (model_four_h_area3_common.size() == 1) {
                commonSelectPathFourThree(path_four_area3);
            } else {
                path_four_area3 = model_four_h_area3_common.get(index_four_area3_p + 1);
                commonSelectPathFourThree(path_four_area3);
                if (index_four_area3_p == model_four_h_area3_common.size() - 2) {
                    index_four_area3_p = -2;
                }
                index_four_area3_p++;
            }
        }
    }

    //4.4的插播和带时间的普通播的回调（4.1控制）
    private void playBack_mode_four_four() {
        if (DateUtils.isInsertPlayIntime(model_four_h_in_pls_stdTime, model_four_h_in_pls_edTime) && model_four_h_area1_in.size() > 0 && model_four_h_area4_in.size() > 0 && !model_four_h_in_lock) {//4.4插
            if (model_four_h_area4_in.size() == 1) {
                path_four_area4 = model_four_h_area4_in.get(0);
                commonSelectPathFourFour(path_four_area4);
            } else {
                path_four_area4 = model_four_h_area4_in.get(index_four_area4_c + 1);
                commonSelectPathFourFour(path_four_area4);
                if (index_four_area4_c == model_four_h_area4_in.size() - 2) {
                    index_four_area4_c = -2;
                    if (model_four_h_in_pls_stdTime.equals("") && model_four_h_in_pls_edTime.equals("")) {
                        index_four_area4_c = -1;
                        model_four_h_area4_in.clear();
                    }
                }
                index_four_area4_c++;
            }
        } else if (model_four_h_area4_common.size() > 0 && DateUtils.isIntime(model_four_h_pls_stdTime, model_four_h_pls_edTime) && !model_four_h_common_lock) {//4.3普通
            if (model_four_h_area4_common.size() == 1) {
                commonSelectPathFourFour(path_four_area4);
            } else {
                path_four_area4 = model_four_h_area4_common.get(index_four_area4_p + 1);
                commonSelectPathFourFour(path_four_area4);
                if (index_four_area4_p == model_four_h_area4_common.size() - 2) {
                    index_four_area4_p = -2;
                }
                index_four_area4_p++;
            }
        }
    }

    //1.1公共的选择图片或者视频
    private void commonSelectPathOne(String path) {
        type = mediaType.getMediaFileType(path);//path_full 改变 type就要改
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_full_h", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_full_h", path);
            } else if (type == 3) {
                addWebViewLayout("model_full_h", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_full_v", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_full_v", path);
            } else if (type == 3) {
                addWebViewLayout("model_full_v", path);
            }
        }
    }

    //2.1的共同选择的方法
    private void commonSelectPathSecondOne(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_second_h_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_second_h_area1", path);
            } else if (type == 3) {
                addWebViewLayout("model_second_h_area1", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_second_v_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_second_v_area1", path);
            } else if (type == 3) {
                addWebViewLayout("model_second_v_area1", path);
            }
        }
    }

    //2.2的共同方法
    private void commonSelectPathSecondTwo(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_second_h_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_second_h_area2", path);
            } else if (type == 3) {
                addWebViewLayout("model_second_h_area2", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_second_v_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_second_v_area2", path);
            } else if (type == 3) {
                addWebViewLayout("model_second_v_area2", path);
            }
        }

    }

    //3.1的共同选择
    private void commonSelectPathThreeOne(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_h_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_h_area1", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_h_area1", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_v_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_v_area1", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_v_area1", path);
            }
        }
    }

    //3.2的共同方法
    private void commonSelectPathThreeTwo(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_h_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_h_area2", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_h_area2", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_v_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_v_area2", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_v_area2", path);
            }
        }
    }

    //3.3共同的方法
    private void commonSelectPathThreeThree(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_h_area3", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_h_area3", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_h_area3", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_three_v_area3", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_three_v_area3", path);
            } else if (type == 3) {
                addWebViewLayout("model_three_v_area3", path);
            }
        }
    }

    //4.1共同的方法
    private void commonSelectPathFourOne(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_h_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_h_area1", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_h_area1", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_v_area1", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_v_area1", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_v_area1", path);
            }
        }
    }

    //4.2共同的方法
    private void commonSelectPathFourTwo(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_h_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_h_area2", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_h_area2", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_v_area2", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_v_area2", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_v_area2", path);
            }
        }
    }

    //4.3共同的方法
    private void commonSelectPathFourThree(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_h_area3", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_h_area3", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_h_area3", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_v_area3", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_v_area3", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_v_area3", path);
            }
        }
    }

    //4.4共同的方法
    private void commonSelectPathFourFour(String path) {
        type = mediaType.getMediaFileType(path);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_h_area4", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_h_area4", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_h_area4", path);
            }
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            if (type == 1) {//图片
                playImageViewLayout("model_four_v_area4", path);
            } else if (type == 2) {//视屏
                changeVideoLayout("model_four_v_area4", path);
            } else if (type == 3) {//网页
                addWebViewLayout("model_four_v_area4", path);
            }
        }
    }

    private void addWebViewLayout(String screeType, String playPath) {//加载网页的布局
        RelativeLayout.LayoutParams webRelative = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        switch (screeType) {
            case "model_full_h":
                play_one.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.width = ScreenUtils.getScreenWidth(MainActivity.this);
                webRelative.height = ScreenUtils.getScreenHeight(MainActivity.this) + ScreenUtils.getNavigationBarHeight(this);
                wv_one.setLayoutParams(webRelative);
                Log.e("model_full_h-web-->", playPath + "");
                wv_one.loadUrl(playPath);//加载网页
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);
                }
                break;
            case "model_full_v":
                play_one.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                webRelative.width = ScreenUtils.getScreenWidth(this);//
                wv_one.setLayoutParams(webRelative);
                Log.e("model_full_v-web->", playPath + "");
                wv_one.loadUrl(playPath);//加载网页
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);
                }
                break;

            case "model_second_h_area1":
                play_one.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                webRelative.width = ScreenUtils.getScreenWidth(this) / 2;
                webRelative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                wv_one.setLayoutParams(webRelative);
                Log.e("second_h_area1-web-->", playPath + "");
                wv_one.loadUrl(playPath);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);
                }
                break;

            case "model_second_h_area2":
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                webRelative.width = ScreenUtils.getScreenWidth(this) / 2;
                webRelative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                wv_two.setLayoutParams(webRelative);
                wv_two.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("second_h_area2-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable22, 80000);
                }
                break;

            case "model_second_v_area1":
                play_one.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                webRelative.width = ScreenUtils.getScreenWidth(this);
                webRelative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3;
                wv_one.setLayoutParams(webRelative);
                wv_one.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("second_v_area1-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);
                }
                break;

            case "model_second_v_area2":
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                if (img_one.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.img_one);
                } else if (play_one.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.play_one);
                } else {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                }

                webRelative.width = ScreenUtils.getScreenWidth(this);
                webRelative.height = ((ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                wv_two.setLayoutParams(webRelative);
                wv_two.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("second_v_area2-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable22, 80000);
                }
                break;

            case "model_three_h_area1":
                play_one.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                webRelative.width = ScreenUtils.getScreenWidth(this) / 3;
                webRelative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                wv_one.setLayoutParams(webRelative);
                wv_one.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("three_h_area1-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);
                }
                break;

            case "model_three_h_area2":
                play_two.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (img_one.isShown()) {//判断图片是否显示了
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                } else if (play_one.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                }
                webRelative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                webRelative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                wv_two.setLayoutParams(webRelative);
                Log.e("three_h_area2-web-->", playPath + "");
                wv_two.loadUrl(playPath);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable32, 80000);
                }
                break;

            case "model_three_h_area3":
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                webRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                if (img_one.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                } else if (play_one.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                }

                if (img_two.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.img_two);
                } else if (play_two.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.play_two);
                } else {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                }

                webRelative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                webRelative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                wv_three.setLayoutParams(webRelative);
                wv_three.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("three_h_area3-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable33, 80000);
                }
                break;

            case "model_four_h_area1":
                play_one.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                wv_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                webRelative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                webRelative.height = ((ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                wv_one.setLayoutParams(webRelative);
                wv_one.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("four_h_area1-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, 80000);//4 1 可以先发1
                }
                break;

            case "model_four_h_area2":
                play_two.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                wv_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (img_one.isShown()) {//判断图片是否显示了
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                } else if (play_one.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                }
                webRelative.width = (ScreenUtils.getScreenWidth(this) / 3);
                webRelative.height = ((ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                wv_two.setLayoutParams(webRelative);
                Log.e("four_h_area2-web-->", playPath + "");
                wv_two.loadUrl(playPath);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable42, 80000);
                }
                break;
            case "model_four_h_area3":
                play_three.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                wv_three.setVisibility(View.VISIBLE);//--------->显示隐藏的控件
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                if (img_one.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.img_one);
                } else if (play_one.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.play_one);
                } else {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                }
                webRelative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                webRelative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3;
                wv_three.setLayoutParams(webRelative);
                wv_three.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("four_h_area3-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable43, 80000);
                }
                break;
            case "model_four_h_area4":
                play_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.VISIBLE);//--------->显示隐藏的控件
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                webRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                if (img_two.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.img_two);
                } else if (play_two.isShown()) {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.play_two);
                } else {
                    webRelative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                }

                if (img_three.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.img_three);
                } else if (play_three.isShown()) {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.play_three);
                } else {
                    webRelative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_three);
                }
                webRelative.width = ScreenUtils.getScreenWidth(this) / 3;
                webRelative.height = (ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3;
                wv_four.setLayoutParams(webRelative);
                wv_four.loadUrl(playPath);
                rl_play.invalidate();
                Log.e("four_h_area4-web-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable44, 80000);
                }
                break;
        }
    }

    //如果是图片的话,设置图片的大小并展示
    private void playImageViewLayout(String screeType, String playPath) {
        RelativeLayout.LayoutParams relative = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        switch (screeType) {
            case "model_full_h":
                play_one.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.width = ScreenUtils.getScreenWidth(MainActivity.this); //给ImageView动态设置大小  是它所在的跟布局去设置它的大小
                relative.height = ScreenUtils.getScreenHeight(MainActivity.this) + ScreenUtils.getNavigationBarHeight(this);
                img_one.setLayoutParams(relative);
                Log.e("model_full_h-img-->", playPath + "");
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);//【注意:必须让控件先充满屏幕】        //noPlaceholder 加载第二个图的之前显示第一个图 图片平滑的过度
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one); //noFade没有动画效果
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }

                break;

            case "model_full_v":
                play_one.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                relative.width = ScreenUtils.getScreenWidth(this);//
                img_one.setLayoutParams(relative);
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);
                Log.e("model_full_v-img->", playPath + "");
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }
                break;

            case "model_second_h_area1":
                play_one.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 2;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                img_one.setLayoutParams(relative);
                Log.e("second_h_area1-img-->", playPath + "");
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }
                break;

            case "model_second_h_area2":
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 2;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                img_two.setLayoutParams(relative);
                img_two.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_two);
                rl_play.invalidate();
                Log.e("second_h_area2-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable22, imgTime * imgCnt * 1000);
                }
                break;

            case "model_second_v_area1":
                play_one.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                relative.width = ScreenUtils.getScreenWidth(this);
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3;//设置视频的宽为铺满。高为动态缩放
                img_one.setLayoutParams(relative);
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one);
                rl_play.invalidate();
                Log.e("second_v_area1-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }
                break;

            case "model_second_v_area2":
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_one);
                }

                relative.width = ScreenUtils.getScreenWidth(this);
                relative.height = ((ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                img_two.setLayoutParams(relative);
                img_two.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_two);
                rl_play.invalidate();
                Log.e("second_v_area2-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable22, imgTime * imgCnt * 1000);
                }
                break;

            case "model_three_h_area1":
                play_one.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 3;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                img_one.setLayoutParams(relative);
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one);
                rl_play.invalidate();
                Log.e("three_h_area1-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }
                break;

            case "model_three_h_area2":
                play_two.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_two.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {//判断图片是否显示了
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                }
                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                img_two.setLayoutParams(relative);
                img_two.setScaleType(ImageView.ScaleType.FIT_XY);
                Log.e("three_h_area2-img-->", playPath + "");
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_two);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable32, imgTime * imgCnt * 1000);
                }
                break;

            case "model_three_h_area3":
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                img_three.setVisibility(View.VISIBLE);//--------->显示隐藏的控件

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                }

                if (wv_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                } else if (play_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_two);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_two);
                }

                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                img_three.setLayoutParams(relative);
                img_three.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_three);
                rl_play.invalidate();
                Log.e("three_h_area3-img-->", playPath + "");

                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable33, imgTime * imgCnt * 1000);
                }
                break;

            case "model_four_h_area1":
                play_one.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                img_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = ((ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                img_one.setLayoutParams(relative);
                img_one.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_one);
                rl_play.invalidate();
                Log.e("four_h_area1-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable11, imgTime * imgCnt * 1000);
                }
                break;

            case "model_four_h_area2":
                play_two.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                img_two.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (wv_one.isShown()) {//判断图片是否显示了
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                }
                relative.width = (ScreenUtils.getScreenWidth(this) / 3);
                relative.height = ((ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                img_two.setLayoutParams(relative);
                Log.e("four_h_area2-img-->", playPath + "");
                img_two.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_two);
                rl_play.invalidate();
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable42, imgTime * imgCnt * 1000);
                }
                break;
            case "model_four_h_area3":
                play_three.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                img_three.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_one);
                }
                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3;
                img_three.setLayoutParams(relative);
                img_three.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_three);
                rl_play.invalidate();
                Log.e("four_h_area3-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable43, imgTime * imgCnt * 1000);
                }
                break;
            case "model_four_h_area4":
                play_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_four.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                if (wv_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                } else if (play_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_two);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_two);
                }

                if (wv_three.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_three);
                } else if (play_three.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_three);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_three);
                }
                relative.width = ScreenUtils.getScreenWidth(this) / 3;
                relative.height = (ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3;
                img_four.setLayoutParams(relative);
                img_four.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load("file://" + playPath).memoryPolicy(MemoryPolicy.NO_CACHE).noPlaceholder().into(img_four);
                rl_play.invalidate();
                Log.e("four_h_area4-img-->", playPath + "");
                if (!stopThread) {//activity结束后停止线程
                    imgHandler.postDelayed(runnable44, imgTime * imgCnt * 1000);
                }
                break;
        }
    }

    private void changeVideoLayout(String screeType, String playPath) {
        RelativeLayout.LayoutParams relative = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        switch (screeType) {
            case "model_full_h":
                wv_one.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.width = ScreenUtils.getScreenWidth(this); //给videoview动态设置大小  是它所在的跟布局去设置它的大小
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                play_one.setLayoutParams(relative);
                Log.e("model_full_h_video", playPath + "|" + play_one.getWidth() + "-" + play_one.getHeight());
                rl_play.invalidate();
                play_one.setVideoPath(playPath);
                play_one.start();
                break;

            case "model_full_v":
                wv_one.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_one.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                play_two.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                relative.width = ScreenUtils.getScreenWidth(this);
                play_one.setLayoutParams(relative);
                rl_play.invalidate();
                play_one.setVideoPath(playPath);
                Log.e("model_full_v_video", playPath + "|" + play_one.getWidth() + "-" + play_one.getHeight());
                play_one.start();
                break;

            case "model_second_h_area1":
                img_one.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 2;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);//设置视频的宽为铺满。高为动态缩放
                play_one.setLayoutParams(relative);

                rl_play.invalidate();
                play_one.setVideoPath(playPath);
                play_one.start();
                Log.e("second_h_area1_video", playPath + "|" + play_one.getWidth() + "-" + play_one.getHeight());
                break;

            case "model_second_h_area2":
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_two.setVisibility(View.VISIBLE);//两个都为视频时

                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 2;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                play_two.setLayoutParams(relative);
                rl_play.invalidate();
                play_two.setVideoPath(playPath);
                Log.e("second_h_area2_video", playPath + "" + "|" + play_two.getWidth() + "--" + play_two.getHeight());
                play_two.start();
                break;

            case "model_second_v_area1":
                img_one.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                relative.width = ScreenUtils.getScreenWidth(this);
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3;//设置视频的宽为铺满。高为动态缩放
                play_one.setLayoutParams(relative);
                rl_play.invalidate();
                play_one.setVideoPath(playPath);
                Log.e("second_v_area1_video", playPath + "" + "|" + play_one.getWidth() + "--" + play_one.getHeight());
                play_one.start();
                break;

            case "model_second_v_area2":
                wv_two.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                play_three.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_two.setVisibility(View.VISIBLE);//两个都为视频时

                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_one);
                }

                relative.width = ScreenUtils.getScreenWidth(this);
                relative.height = ((ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                play_two.setLayoutParams(relative);
                rl_play.invalidate();
                play_two.setVideoPath(playPath);
                Log.e("second_v_area2_video", playPath + "" + "|" + play_two.getWidth() + "--" + play_two.getHeight());
                play_two.start();
                break;

            case "model_three_h_area1":
                img_one.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = ScreenUtils.getScreenWidth(this) / 3;
                relative.height = ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this);
                play_one.setLayoutParams(relative);
                rl_play.invalidate();
                play_one.setVideoPath(playPath);
                Log.e("three_h_area1_video", playPath + "" + "|" + play_one.getWidth() + "--" + play_one.getHeight());
                play_one.start();
                break;

            case "model_three_h_area2":
                wv_two.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_two.setVisibility(View.GONE);
                img_four.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_two.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                }

                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                play_two.setLayoutParams(relative);
                rl_play.invalidate();
                play_two.setVideoPath(playPath);
                Log.e("three_h_area2_video", playPath + "" + "|" + play_two.getWidth() + "--" + play_two.getHeight());
                play_two.start();
                break;

            case "model_three_h_area3":
                wv_three.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                img_three.setVisibility(View.GONE);//视频---图片---视屏 的问题
                img_four.setVisibility(View.GONE);
                play_four.setVisibility(View.GONE);
                play_three.setVisibility(View.VISIBLE);//两个都为视频时

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (play_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                }

                if (wv_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                } else if (play_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_two);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_two);
                }

                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 2;
                play_three.setLayoutParams(relative);
                rl_play.invalidate();
                play_three.setVideoPath(playPath);
                Log.e("three_h_area3_video", playPath + "" + "|" + play_three.getWidth() + "--" + play_three.getHeight());
                play_three.start();
                break;

            case "model_four_h_area1":
                img_one.setVisibility(View.GONE);
                wv_one.setVisibility(View.GONE);
                play_one.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = ((ScreenUtils.getScreenHeight(this) + ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                play_one.setLayoutParams(relative);
                play_one.setVideoPath(playPath);
                rl_play.invalidate();
                play_one.start();
                Log.e("four_h_area1-video-->", playPath + "");
                break;

            case "model_four_h_area2":
                img_two.setVisibility(View.GONE);
                wv_two.setVisibility(View.GONE);
                play_two.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (wv_one.isShown()) {//判断图片是否显示了
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_one);
                } else if (img_one.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_one);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_one);
                }
                relative.width = (ScreenUtils.getScreenWidth(this) / 3);
                relative.height = ((ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3) * 2;
                play_two.setLayoutParams(relative);
                play_two.setVideoPath(playPath);
                rl_play.invalidate();
                play_two.start();
                Log.e("four_h_area2-img-->", playPath + "");
                break;

            case "model_four_h_area3":
                img_three.setVisibility(View.GONE);
                wv_three.setVisibility(View.GONE);
                play_three.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                if (wv_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_one);
                } else if (img_one.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_one);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_one);
                }
                relative.width = (ScreenUtils.getScreenWidth(this) / 3) * 2;
                relative.height = (ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3;
                play_three.setLayoutParams(relative);
                play_three.setVideoPath(playPath);
                rl_play.invalidate();
                play_three.start();
                Log.e("four_h_area3-video-->", playPath + "");
                break;

            case "model_four_h_area4":
                img_four.setVisibility(View.GONE);
                wv_four.setVisibility(View.GONE);
                play_four.setVisibility(View.VISIBLE);

                relative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                relative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (wv_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.wv_two);
                } else if (img_two.isShown()) {
                    relative.addRule(RelativeLayout.BELOW, R.id.img_two);
                } else {
                    relative.addRule(RelativeLayout.BELOW, R.id.play_two);
                }

                if (wv_three.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.wv_three);
                } else if (img_three.isShown()) {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.img_three);
                } else {
                    relative.addRule(RelativeLayout.RIGHT_OF, R.id.play_three);
                }
                relative.width = ScreenUtils.getScreenWidth(this) / 3;
                relative.height = (ScreenUtils.getScreenHeight(this)+ ScreenUtils.getNavigationBarHeight(this)) / 3;
                play_four.setLayoutParams(relative);
                play_four.setVideoPath(playPath);
                rl_play.invalidate();
                play_four.start();
                Log.e("four_h_area4-video-->", playPath + "");
                break;
        }
    }

    private void initVideo_one() {
        //视频加载完毕的回调
        play_one.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playBack_model_full();
                if (onlyPlayNotTimeAndHaveTime) {//没时间的进去
                    playNotTiemtBack();
                }
            }
        });

        play_one.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {   //媒体服务器挂掉了
                    playBack_model_full();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTiemtBack();
                    }
                } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    playBack_model_full();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTiemtBack();
                    }
                }
                return true;
            }
        });
    }

    private void initVideo_two() {
        //视频加载完毕的回调
        play_two.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playBack_mode_second_two();//2.2
                playBack_mode_three_two();//3.2
                playBack_mode_four_two();//4.2
                if (onlyPlayNotTimeAndHaveTime) {//没时间的进去了
                    if (secondOneNoTimeBackLockFromPlayTwo) {//2.1进去了
                        playNotTimeSecondtwo();
                    }

                    if (threeOneNoTimeBackLockFromPlayTwo) {//3.1进去了
                        playNotTimeThreetwo();
                    }

                    if (fourOneNoTimeBackLockFromPlayTwo) {//4.1
                        playNotTimeFourTwo();
                    }
                }
            }
        });

        play_two.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    playBack_mode_second_two();//2.2
                    playBack_mode_three_two();//3.2
                    playBack_mode_four_two();//4.2
                    if (onlyPlayNotTimeAndHaveTime) {
                        if (secondOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeSecondtwo();
                        }
                        if (threeOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeThreetwo();
                        }
                        if (fourOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeFourTwo();
                        }
                    }
                } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    playBack_mode_second_two();//2.2
                    playBack_mode_three_two();//3.2
                    playBack_mode_four_two();//4.2
                    if (onlyPlayNotTimeAndHaveTime) {
                        if (secondOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeSecondtwo();
                        }
                        if (threeOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeThreetwo();
                        }
                        if (fourOneNoTimeBackLockFromPlayTwo) {
                            playNotTimeFourTwo();
                        }
                    }
                }
                return true;
            }
        });
    }

    private void initVideo_three() {
        //视频加载完毕的回调
        play_three.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//3.3
            @Override
            public void onCompletion(MediaPlayer mp) {
                playBack_mode_three_three();//3.3 插播或者有时间的普通播放
                playBack_mode_four_three();//4.3
                if (onlyPlayNotTimeAndHaveTime) {//没时间的进去了
                    if (threeOneNoTimeBackLockFromPlayTwo) {
                        playNotTimeThreethree();
                    }

                    if (fourOneNoTimeBackLockFromPlayTwo) {
                        playNotTimeFourThree();
                    }
                }
            }
        });

        play_three.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    playBack_mode_three_three();//3.3
                    playBack_mode_four_three();//4.3
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeThreethree();
                    }
                    if (fourOneNoTimeBackLockFromPlayTwo) {
                        playNotTimeFourThree();
                    }
                } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    playBack_mode_three_three();//3.3
                    playBack_mode_four_three();//4.3
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeThreethree();
                    }
                    if (fourOneNoTimeBackLockFromPlayTwo) {
                        playNotTimeFourThree();
                    }
                }
                return true;
            }
        });
    }

    private void iniVideo_four() {
        play_four.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//4.4
            @Override
            public void onCompletion(MediaPlayer mp) {  //视频加载完毕的回调
                playBack_mode_four_four();
                if (onlyPlayNotTimeAndHaveTime) {//没时间的进去了
                    playNotTimeFourFour();
                }
            }
        });

        play_four.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    playBack_mode_four_four();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeFourFour();
                    }
                } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    playBack_mode_four_four();
                    if (onlyPlayNotTimeAndHaveTime) {
                        playNotTimeFourFour();
                    }
                }
                return true;
            }
        });
    }

    private void setH5WebView(final WebView webView) {//设置h5页面的方法
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); //支持js
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片

        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setSupportZoom(true);////支持缩放
        settings.setBuiltInZoomControls(false);//设置支持缩放

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//只是加载网页 不缓存
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//加载的网页在本地显示
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");//影藏广告
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopThread = true;
        againIntiView();
        finish();
        LogUtils.e("Mainactivety---onDestroy");
    }
}
