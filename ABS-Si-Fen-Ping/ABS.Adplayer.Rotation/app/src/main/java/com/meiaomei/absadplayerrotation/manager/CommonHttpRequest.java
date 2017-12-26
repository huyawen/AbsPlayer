package com.meiaomei.absadplayerrotation.manager;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meiaomei.absadplayerrotation.model.ExtraMessage_SQL;
import com.meiaomei.absadplayerrotation.model.bean.Command;
import com.meiaomei.absadplayerrotation.model.bean.Content;
import com.meiaomei.absadplayerrotation.model.bean.Detail;
import com.meiaomei.absadplayerrotation.model.bean.Model;
import com.meiaomei.absadplayerrotation.model.bean.Resource;
import com.meiaomei.absadplayerrotation.model.event.StringModel;
import com.meiaomei.absadplayerrotation.utils.DateUtils;
import com.meiaomei.absadplayerrotation.utils.FileUtils;
import com.meiaomei.absadplayerrotation.utils.JudgeMediaTypeUtils;
import com.meiaomei.absadplayerrotation.utils.LogToFile;
import com.meiaomei.absadplayerrotation.utils.MD5;
import com.meiaomei.absadplayerrotation.utils.MD5Utils;
import com.meiaomei.absadplayerrotation.utils.XmlUtils;
import com.thoughtworks.xstream.XStream;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by meiaomei on 2017/5/13.
 */
public class CommonHttpRequest {

    Context context;
    ParseManager parseManager;
    DbUtils dbUtils;
    String MateFileDir = "files";
    Build bd = new Build();
    Date date = new Date();
    String dateSt = DateUtils.formatDate(date, "yyyyMMdd");//下载日志的时间格式

    public CommonHttpRequest(Context context, ParseManager parseManager, DbUtils dbUtils) {
        this.context = context;
        this.parseManager = parseManager;
        this.dbUtils = dbUtils;
    }

    public CommonHttpRequest(Context context) {
        this.context = context;
    }

    XStream xStream = new XStream();
    OkHttpManager manager = new OkHttpManager();


    /**
     * 同步时间，修改本地时间为服务器时间
     */
    public void timeSync(final String macAdress, final String ipAndPort) {
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "timesync", macAdress, "", new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {
                    String time = command.getServertime();//得到服务器时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        LogToFile.e(LogToFile.LOG, time + "--同步服务器时间成功！");
                        Log.e(LogToFile.TEST_LOG, time + "--同步服务器时间成功！");
                        Date date = sdf.parse(time);
//                        SystemClock.setCurrentTimeMillis(date.getTime());///修改系统时间
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    parseManager.taskGetReport("", "timesync", macAdress, ipAndPort);//上报同步任务
                }

            }
        });
    }

    /**
     * 刚进入程序时自动同步服务器资源4.12
     *
     * @param macAdress
     * @param ipAndPort
     */
    public void syncResource(final String macAdress, final String ipAndPort) {
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "resourcesync", macAdress, "", new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {
                    final String md5 = command.getMd5();
                    final String rPath = command.getLink();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //下载资源文件
                            String dirName = "xml";//向内存设备中新建文件夹
                            String name = rPath.substring(rPath.lastIndexOf("/"), rPath.length());//资源文件的名称
                            HttpHelper httpHelper = new HttpHelper();
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                                int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + rPath, dirName, name, md5);
                                File file = new File(FileUtils.SDPATH + dirName + name);
                                if (flag == 0) {
                                    try {
                                        String localMd5 = MD5Utils.getFileMD5(file);
                                        if (md5.equals(localMd5)) {
                                            Log.e(LogToFile.TEST_LOG, name + "--文件下载成功！");
                                            LogToFile.e(LogToFile.LOG, name + "--文件下载成功！");
                                            parseManager.taskGetReport("", "resourcesync", macAdress, ipAndPort);//上报同步任务
                                            xStream.processAnnotations(Resource.class);//下载成功就解析
                                            Resource resource = (Resource) xStream.fromXML(file);
                                            String xmlResource = xStream.toXML(resource);
                                            parseResource(xmlResource, ipAndPort);
                                        } else {
                                            LogToFile.e(LogToFile.LOG, name + "--文件MD5校验失败！");
                                            Log.e(LogToFile.TEST_LOG, name + "--文件MD5校验失败！");
                                            parseManager.alarmReport(ParseManager.MD5_ERROR, "programsync.xml文件", macAdress, ipAndPort);
                                            EventBus.getDefault().post(new StringModel("syncresource", "true"));
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (flag == 1) {//是同一个文件，不下载也不判断，资源单相同，资源肯定都有
                                    parseManager.taskGetReport("", "resourcesync", macAdress, ipAndPort);//上报同步任务
                                    EventBus.getDefault().post(new StringModel("syncresource", "true"));
                                } else {
                                    LogToFile.e(LogToFile.LOG, name + "--文件下载失败！");
                                    Log.e(LogToFile.TEST_LOG, name + "--文件下载失败！");
                                    parseManager.alarmReport(ParseManager.DOWN_FAIL_STATE, "programsync.xml文件", macAdress, ipAndPort);
                                    EventBus.getDefault().post(new StringModel("syncresource", "true"));
                                }
                            } else {//(否则入手机内存)
                                File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //创建xml目录
                                File file = new File(workDir, name);//创建文件
                                try {
                                    InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + rPath);
                                    boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                                    if (isSave) {
                                        Log.e(LogToFile.TEST_LOG, name + " --文件下载成功！");
                                        String localMd5 = MD5Utils.getFileMD5(file);
                                        if (md5.equals(localMd5)) {
                                            parseManager.taskGetReport("", "resourcesync", macAdress, ipAndPort);//上报同步任务
                                            xStream.processAnnotations(Resource.class);//下载成功就解析
                                            Resource resource = (Resource) xStream.fromXML(file);
                                            String xmlResource = xStream.toXML(resource);
                                            parseResource(xmlResource, ipAndPort);
                                        } else {
                                            parseManager.alarmReport(ParseManager.MD5_ERROR, "programsync.xml文件", macAdress, ipAndPort);
                                            Log.e("syncresource.xml--md5！", "failed");
                                        }
                                    } else {
                                        parseManager.alarmReport(ParseManager.DOWN_FAIL_STATE, "programsync.xml文件", macAdress, ipAndPort);
                                        Log.e("syncresource.xml！", "failed");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                } else if ("1".equals(command.getResult())) {//没有同步资源，直接进入app播放昨天的普通节目
                    LogToFile.e(LogToFile.LOG, "--没有可同步的资源，继续播放昨天的资源！");
                    EventBus.getDefault().post(new StringModel("syncresource", "true"));//noSync
                }
            }

            @Override
            public void onError(String meg) {
                super.onError(meg);//没网络连接 请联网
                Log.e(LogToFile.TEST_LOG, "--网络连接错误，下载失败！");
                LogToFile.e(LogToFile.LOG, "--网络连接错误，下载失败！");
                EventBus.getDefault().post(new StringModel("syncresource", "true"));//noWeb
            }
        });
    }


    public void syncProgram(final String macAdress, final String ipAndPort) {
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "programsync", macAdress, "", new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {
                    final String md5 = command.getMd5();
                    final String progarmPath = command.getLink();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //下载资源文件
                            String dirName = "xml";//向内存设备中新建文件夹
                            String name = progarmPath.substring(progarmPath.lastIndexOf("/"), progarmPath.length());//资源文件的名称
                            HttpHelper httpHelper = new HttpHelper();

                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                                int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + progarmPath, dirName, name, md5);
                                File file = new File(FileUtils.SDPATH + dirName + name);
                                if (flag == 0) {
                                    Log.e(LogToFile.TEST_LOG, name + "--文件下载成功");
                                    LogToFile.e(LogToFile.LOG, name + "--文件下载成功！");
                                    try {
                                        if (md5.equals(MD5Utils.getFileMD5(file))) {
                                            Log.e(LogToFile.TEST_LOG, name + "--文件MD5校验成功");
                                            LogToFile.e(LogToFile.LOG, name + "--文件MD5校验成功");
                                            saveExtraMessage(name);//第一次同步，保存节目单名称和播放模式
                                            parseManager.taskGetReport("", "programsync", macAdress, ipAndPort);//上报同步任务
                                        } else {
                                            Log.e(LogToFile.TEST_LOG, name + "--文件MD5校验失败");
                                            parseManager.alarmReport(ParseManager.MD5_ERROR, "下载programsync.xml文件", macAdress, ipAndPort);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (flag == 1) {//已存在同文件
                                    saveExtraMessage(name);
                                    parseManager.taskGetReport("", "programsync", macAdress, ipAndPort);//上报同步任务
                                } else {//下载失败
                                    Log.e(LogToFile.TEST_LOG, name + "--文件下载失败");
                                    LogToFile.e(LogToFile.LOG, name + "--文件下载失败！");
                                    parseManager.alarmReport(ParseManager.DOWN_FAIL_STATE, "programsync.xml文件", macAdress, ipAndPort);
                                }
                            } else {//(否则入手机内存)
                                File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //创建xml目录
                                File file = new File(workDir, name);//创建文件
                                try {
                                    InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + progarmPath);
                                    boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                                    if (isSave) {
                                        if (md5.equals(MD5Utils.getFileMD5(file))) {
                                            saveExtraMessage(name);
                                            parseManager.taskGetReport("", "programsync", macAdress, ipAndPort);//上报同步任务
                                        } else {
                                            parseManager.alarmReport(ParseManager.MD5_ERROR, "下载programsync.xml文件", macAdress, ipAndPort);
                                            Log.e(LogToFile.TEST_LOG, name + "--文件MD5校验失败");
                                        }
                                    } else {
                                        parseManager.alarmReport(ParseManager.DOWN_FAIL_STATE, "programsync.xml文件", macAdress, ipAndPort);
                                        Log.e(LogToFile.TEST_LOG, name + "--文件下载失败");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                }
            }

            @Override
            public void onError(String meg) {
                super.onError(meg);
            }
        });
    }

    private void saveExtraMessage(String name) {//同步节目成功后保存
        try {
            ExtraMessage_SQL etx = dbUtils.findFirst(ExtraMessage_SQL.class);
            if (etx == null) {
                ExtraMessage_SQL extraMessage_sql = new ExtraMessage_SQL();
                extraMessage_sql.setPlayMode("1");
                extraMessage_sql.setPgFileName(name);
                dbUtils.save(extraMessage_sql);
                Log.e(LogToFile.TEST_LOG, "--普通播放节目单保存数据库成功！");
                LogToFile.e(LogToFile.LOG, "--普通播放节目单保存数据库成功！");
            } else {
                etx.setPlayMode("1");
                etx.setPgFileName(name);
                dbUtils.update(etx);
            }
        } catch (DbException e) {
            Log.e(LogToFile.TEST_LOG, "--普通播放节目单保存数据库失败！");
            LogToFile.e(LogToFile.LOG, "--普通播放节目单保存数据库失败！");
            e.printStackTrace();
        }
    }

    //解析Resource类
    public void parseResource(String resXml, String ipAndPort) {
        xStream.processAnnotations(Resource.class);
        Resource resource = (Resource) xStream.fromXML(resXml);
        if (resource != null) {
            Model model = resource.getModel();
            if (model != null) {
                String ftpAdd = model.getFtpAdd() == null ? "" : model.getFtpAdd();
                String href = model.getHref();//文件的相对下载路径
                String md5 = model.getMd5();
                String fileSize = model.getFileSize();
                downLoadXml(href, md5, ipAndPort);//去下载models.xml的xml文件
            }

            List<Resource.BgImg> bgImgList = resource.getBgImgList();
            if (bgImgList != null && bgImgList.size() > 0) {
                for (int i = 0; i < bgImgList.size(); i++) {
                    Resource.BgImg bgImg = bgImgList.get(i);
                    String bftp = bgImg.getFtpAdd() == null ? "" : bgImg.getFtpAdd();
                    String bhref = bgImg.getHref();
                    String bmd5 = bgImg.getMd5();
                    String bfileSize = bgImg.getFileSize();
                    downLoadSyncBgImage(bhref, bmd5, ipAndPort);//下载图片资源  就是为default准备的
                }
            }
//--------------------------------------------------------------------------------------------------------
            Command command = new Command();//封装4.3的集合
            ArrayList<Content> clist = new ArrayList<>();
            ArrayList<String> count = new ArrayList<>();
            Detail detail = new Detail();
//--------------------------------------------------------------------------------------------------------
            List<Resource.Rfile> rfileList = resource.getRfileList();
            if (rfileList.size() > 0) {
                for (int j = 0; j < rfileList.size(); j++) {
                    Resource.Rfile rfile = rfileList.get(j);
                    String rftpAdd = rfile.getFtpAdd() == null ? "" : rfile.getFtpAdd();
                    String resId = rfile.getResId();
                    String resName = rfile.getResName();
                    String rhref = rfile.getHref();
                    String rmd5 = rfile.getMd5();
                    String rfileSize = rfile.getFileSize();
                    JudgeMediaTypeUtils ju = new JudgeMediaTypeUtils();
                    int play = ju.getMediaFileType(rhref);
                    if (play == 1 || play == 2) {//过滤掉网页，资源为视频或者图片时下载
                        downLoadSync(rhref, rmd5, resId, ipAndPort, clist, count, detail, command);//下载所有视频资源
                    }
                }
//----------------------------------------------保存----------------------------------------------------------
                if (count.size() == clist.size()) {//成功 【发送广播】
                    command.setStatus(ParseManager.DOWN_COMPLETE_STATE);
                    EventBus.getDefault().post(new StringModel("syncresource", "true"));
                } else {//总数和成功的总数不相等就是有失败的
                    command.setStatus(ParseManager.DOWN_FAIL_STATE);//有下载失败的资源
                    EventBus.getDefault().post(new StringModel("syncresource", "true"));
                }
                //每次下完一个视频就保存一次文件
                detail.setDeContentList(clist);
                command.setDetail(detail);
                xStream.processAnnotations(Command.class);
                String xmlString = xStream.toXML(command);
                XmlUtils xmlUtils = new XmlUtils(context);
                boolean isSave = false;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                    isSave = FileUtils.saveToSDCard("downloadstatusreport.xml", "xml", xmlString);
                } else {
                    isSave = xmlUtils.writeXmlToMemory(xmlString, "downloadstatusreport.xml"); //4.3资源文件下载完成后  就存入本地 系统通知时直接上报
                }
                Log.e("上报资源下载状态保存成功", isSave + "downloadstatu.xml");
//--------------------------------------------------------------------------------------------------------
            }
        }
    }

    public void downLoadXml(String link, String md5, String ipAndPort) {//model下载成功只需告诉界面即可
        String dirName = "xml";
        HttpHelper httpHelper = new HttpHelper();
        String fileName = link.substring(link.lastIndexOf("/"), link.length());//得到文件的名称
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                File file = new File(FileUtils.SDPATH + dirName + fileName);
                int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + link, dirName, fileName, md5);
                if (flag == 0) {
                    String localMd5 = MD5.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件md5校验成功,且下载成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件md5校验成功,且下载成功！");
                        EventBus.getDefault().post(new StringModel("models.xml", "true"));
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件md5校验失败！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件md5校验失败！");
                    }
                } else if (flag == 1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--文件已存在！");
                    LogToFile.e(LogToFile.LOG, fileName + "--文件已存在！");
                } else if (flag == -1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--文件下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--文件下载失败！");
                }
            } else {//(写入手机内存)
                File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //存在找到路径，不存在创建xml目录
                File file = new File(workDir, fileName);//创建文件
                InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + link);
                boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                if (isSave) {
                    String localMd5 = MD5.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件md5校验成功,且下载成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件md5校验成功,且下载成功！");
                        EventBus.getDefault().post(new StringModel("models.xml", "true", ""));
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件md5校验失败！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件md5校验失败！");
                    }
                } else {
                    Log.e(LogToFile.TEST_LOG, fileName + "--文件下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--文件下载失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void downLoadSync(final String link, String md5, String resId, String ipAndPort, final ArrayList<Content> list, ArrayList<String> count, final Detail detail, final Command command) {
        HttpHelper httpHelper = new HttpHelper();
        String fileName = link.substring(link.lastIndexOf("/"), link.length());//得到文件的名称
        String dateStStart = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        try {
            final Content content = new Content();
            content.setResid(resId);
            content.setFilename(fileName);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                int flag = httpHelper.downloadVideo("http://" + ipAndPort + "/zhyh/" + link, MateFileDir, fileName, new FileUtils.ProgressListener() {
                    @Override
                    public void update(float progress) {
                        content.setProcess(String.format("%.2f", progress * 100) + "%");
                        content.setStatus(ParseManager.DOWN_ING_STATE);//正在下载中

                        list.add(content); //每次下完一个视频就保存一次文件,更新进度
                        detail.setDeContentList(list);
                        command.setDetail(detail);
                        command.setStatus(ParseManager.DOWN_ING_STATE);
                        xStream.processAnnotations(Command.class);
                        String xmlString = xStream.toXML(command);
                        XmlUtils xmlUtils = new XmlUtils(context);
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                            FileUtils.saveToSDCard("downloadstatusreport.xml", "xml", xmlString);
                        } else {
                            xmlUtils.writeXmlToMemory(xmlString, "downloadstatusreport.xml"); //4.3资源文件下载完成后  就存入本地 系统通知时直接上报
                        }
                        list.remove(list.size() - 1);//更新完后移除此元素

                    }
                }, md5);
                String saveFlag = "";
                if (flag == 0) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    File resFile = new File(FileUtils.SDPATH + MateFileDir + fileName);
                    String localMd5 = MD5Utils.getFileMD5(resFile);
                    if (localMd5.equals(md5)) {
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验成功");
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功");
                        count.add(localMd5);//成功的下载的个数
                        saveFlag = ParseManager.DOWN_COMPLETE_STATE;
                        content.setProcess("100%");
                        content.setStatus(ParseManager.DOWN_COMPLETE_STATE);
                        FileUtils.saveToSDCard("downloadlog_" + bd.ID + "_" + dateSt + ".log", MateFileDir, resId + "|" + dateStStart + "|" + saveFlag + "|");
                    } else {
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验失败");
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败");
                    }
                } else if (flag == -1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载失败！");
                    saveFlag = ParseManager.DOWN_ING_STATE;
                    content.setStatus(ParseManager.RESOURCE_FILE_NO_EXIST);//源文件不存在
                    content.setProcess("0%");
                    FileUtils.saveToSDCard("downloadlog_" + bd.ID + "_" + dateSt + ".log", MateFileDir, resId + "|" + dateStStart + "|" + saveFlag + "|");
                } else if (flag == 1) {
                    content.setProcess("100%");
                    content.setStatus(ParseManager.DOWN_COMPLETE_STATE);
                    count.add(md5);//成功的下载的个数
                }

                list.add(content);

            } else {//文件存入手机内存
                File workDir = context.getFilesDir();
                File file = new File(workDir, fileName);//创建文件
                InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + link);
                boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                FileOutputStream fos = context.openFileOutput("downloadlog_" + bd.ID + "_" + dateSt + ".log", Context.MODE_APPEND);//下载log日志
                if (isSave) {//下载成功
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    String localMd5 = MD5Utils.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        count.add(localMd5);//成功的下载的个数
                        fos.write((resId + "|" + dateStStart + "|" + ParseManager.DOWN_COMPLETE_STATE + "|").getBytes());//写入下载的log成功0001
                        fos.close();
                        content.setProcess("100%");
                        content.setStatus(ParseManager.DOWN_COMPLETE_STATE);//下载成功
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败！");
                        LogToFile.e(LogToFile.LOG,fileName+"--MD5校验失败！");
                    }
                } else {//下载失败
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    fos.write((resId + "|" + dateStStart + "|" + ParseManager.DOWN_ING_STATE + "|").getBytes());//写入下载的log失败0002
                    fos.close();
                    content.setProcess("0%");
                    content.setStatus(ParseManager.RESOURCE_FILE_NO_EXIST);//源文件不存在
                }
                list.add(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoadSyncBgImage(String link, String md5, String ipAndPort) {
        HttpHelper httpHelper = new HttpHelper();
        String fileName = link.substring(link.lastIndexOf("/"), link.length());//得到文件的名称
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + link, MateFileDir, fileName, md5);
                if (flag == 0) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    File imgFile = new File(FileUtils.SDPATH + MateFileDir + fileName);
                    String localMd5 = MD5Utils.getFileMD5(imgFile);
                    if (localMd5.equals(md5)) {
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验成功");
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功");
                    } else {
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验失败");
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败");
                    }
                } else if (flag == 1) {//文件已存在
                    Log.e(LogToFile.TEST_LOG, fileName + "--文件已存在！");
                    LogToFile.e(LogToFile.LOG, fileName + "--文件已存在！");
                } else if (flag == -1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载失败！");
                }

            } else {//文件存入手机内存
                File workDir = context.getFilesDir();
                File file = new File(workDir, fileName);//创建文件
                InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + link);
                boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                if (isSave) {//下载成功
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    String localMd5 = MD5Utils.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败");
                    }
                } else {//下载失败
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
