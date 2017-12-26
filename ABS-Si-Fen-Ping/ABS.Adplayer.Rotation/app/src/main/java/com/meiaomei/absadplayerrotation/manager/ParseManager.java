package com.meiaomei.absadplayerrotation.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meiaomei.absadplayerrotation.dbutility.AbsPlbsDB;
import com.meiaomei.absadplayerrotation.model.Config_SQL;
import com.meiaomei.absadplayerrotation.model.Model_SQL;
import com.meiaomei.absadplayerrotation.model.bean.Area;
import com.meiaomei.absadplayerrotation.model.bean.Command;
import com.meiaomei.absadplayerrotation.model.bean.Config;
import com.meiaomei.absadplayerrotation.model.bean.Content;
import com.meiaomei.absadplayerrotation.model.bean.Contents;
import com.meiaomei.absadplayerrotation.model.bean.Detail;
import com.meiaomei.absadplayerrotation.model.bean.Model;
import com.meiaomei.absadplayerrotation.model.bean.Models;
import com.meiaomei.absadplayerrotation.model.bean.Program;
import com.meiaomei.absadplayerrotation.model.bean.Res;
import com.meiaomei.absadplayerrotation.model.bean.Resource;
import com.meiaomei.absadplayerrotation.model.bean.TaskItem;
import com.meiaomei.absadplayerrotation.model.bean.TaskList;
import com.meiaomei.absadplayerrotation.model.event.StringModel;
import com.meiaomei.absadplayerrotation.utils.DateUtils;
import com.meiaomei.absadplayerrotation.utils.DeviceInfoUtils;
import com.meiaomei.absadplayerrotation.utils.FileUtils;
import com.meiaomei.absadplayerrotation.utils.JudgeMediaTypeUtils;
import com.meiaomei.absadplayerrotation.utils.LogToFile;
import com.meiaomei.absadplayerrotation.utils.MD5Utils;
import com.meiaomei.absadplayerrotation.utils.MySort;
import com.meiaomei.absadplayerrotation.utils.XmlUtils;
import com.meiaomei.absadplayerrotation.utils.ZipUtils;
import com.thoughtworks.xstream.XStream;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by meiaomei on 2017/5/3.
 */
public class ParseManager {

    public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";  //XML文件头
    public final static String TASK_COMPLETE_STATE = "0000";  //收完任务的通知
    public final static String DOWN_COMPLETE_STATE = "0001"; //     4.3Command state//0001：下载完成  Content state 0001：下载成功
    public final static String DOWN_ING_STATE = "0002";     //      4.3Command state//0002: 下载中    Content state 0002: 下载中
    public final static String DOWN_FAIL_STATE = "0100";    // 4.3Command state //0100：下载失败  Content state
    public final static String RESOURCE_FILE_NO_EXIST = "0101";//Content state 源文件不存在
    public final static String DOWN_OUT_TIME = "0102";         //Content state 下载超时
    public final static String USER_POWER_ERROR = "0103";     //Content state 0103：帐号权限错误
    public final static String MD5_ERROR = "0104";            //Content state 0104：文件md5校验错
    public final static String SOCKET_OUT_TIME = "0105";      //Content state 0105：连接超时连接超时
    //终端错误码表
    public final static String HARD_DISK_OUT = "2001";        //磁盘空间不足
    public final static String SETTING_INFO_FAILED = "2002"; //配置信息设置失败
    public final static String TASK_DOWN_FAILED = "2003";    //任务下载失败
    public final static String HARD_DISK_ERROR = "2004";     //硬盘mount不上
    public final static String DECODE_ERROR = "2005";        //解码异常
    public final static String LED_CONNECT_ERROR = "2006";   //LED屏连接异常
    public final static String MD5_FAILED = "2007";          //存储有非法内容（例如MD5校验不成功）
    public final static String XML_WORN = "2008";            //Xml消息不完整
    public final static String LOG_UP_FAILED = "2009";       //日志上传失败，具体原因见告警信息
    public final static String FTP_CONNECT_FAILED = "3001";  //ftp连接失败
    public final static String FILE_NO_EXIST = "3002";       //文件不存在


    boolean isOne = true;
    Context context;
    String rPath;
    String programPath;
    OkHttpManager manager = new OkHttpManager();
    XStream xStream = new XStream();
    DbUtils dbUtils = AbsPlbsDB.getDbUtils();
    String MateFileDir = "files";
    Build bd = new Build();
    Date date = new Date();
    String dateSt = DateUtils.formatDate(date, "yyyyMMdd");//下载日志的时间格式
    HttpHelper httpHelper;
    String ApkName = "";

    public ParseManager(Context context) {
        this.context = context;
        httpHelper = new HttpHelper();
    }

    //解析programs类,并且按照播放类型分类存入集合
    public ArrayList<ArrayList> parseProgram(Program program, String playMode) {
        ArrayList<ArrayList> listgroup = new ArrayList<>();
        Program.DefaultPls defaultPls = program.getDefaultPls();
        if (defaultPls != null) {//默认的列表
            String aretType = defaultPls.getAreatype(); //屏幕样式id
            List<Res> resList = defaultPls.getResList();
            if (resList.size() > 0) {
                ArrayList<HashMap<String, String>> defaultplslist = new ArrayList<>();
                for (int j = 0; j < resList.size(); j++) {
                    HashMap<String, String> map = new HashMap<>();
                    Res res = resList.get(j);
                    String dresName = res.getResname();
                    String dresId = res.getResid();
                    map.put("default", "default");
                    map.put("areaType", aretType);
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有内存卡存入
                        if (dresName.contains("/")) {
                            map.put("path", FileUtils.SDPATH + MateFileDir + dresName.substring(dresName.lastIndexOf("/"), dresName.length()) + "|" + dresId);
                        } else {
                            map.put("path", FileUtils.SDPATH + MateFileDir + "/" + dresName + "|" + dresId);
                        }
                    } else {
                        if (dresName.contains("/")) {
                            map.put("path", context.getFilesDir().getAbsolutePath() + dresName.substring(dresName.lastIndexOf("/"), dresName.length()) + "|" + dresId);
                        } else {
                            map.put("path", context.getFilesDir().getAbsolutePath() + "/" + dresName + "|" + dresId);
                        }
                    }
                    defaultplslist.add(map);//默认播放的集合列表
                }
                Collections.reverse(defaultplslist);
                if (defaultplslist.size() > 0) {
                    listgroup.add(defaultplslist);//插播没有优先级先不处理
                }
            }
        }


        List<Program.Pls> plsList = program.getPlsList();
        if (plsList != null && plsList.size() > 0) {
            for (int k = 0; k < plsList.size(); k++) {
                Program.Pls pls = plsList.get(k);
                String areaType = pls.getAreatype();//屏幕样式id
                String pls_edTime = pls.getEdtime();
                String pls_stdTime = pls.getStdtime();

                Program.Pls.ResArr resArr = pls.getResArr();
                List<Res> resList1 = resArr.getResList();
                if (resList1 != null && resList1.size() > 0) {
                    ArrayList<HashMap<String, String>> listz = new ArrayList<>();
                    ArrayList<HashMap<String, String>> listd = new ArrayList<>();
                    ArrayList<HashMap<String, String>> listc = new ArrayList<>();
                    for (int o = 0; o < resList1.size(); o++) {
                        Res res = resList1.get(o);
                        String resID = res.getResid();
                        String resName = res.getResname();
                        String resStdtime = res.getStdtime();
                        String resEdTime = res.getEdtime();
                        String priority = res.getPriority();
                        String playcnt = res.getPlaycnt();
                        String area = res.getArea();
                        JudgeMediaTypeUtils ju = new JudgeMediaTypeUtils();
                        int play = ju.getMediaFileType(resName);
                        if (playMode.equals("0")) {//插播的集合
                            if (playcnt != "" && playcnt != null) {//防止播放模式为空报错
                                int cnt = Integer.valueOf(playcnt);//有次数的播
                                for (int i = 0; i < cnt; i++) {//有几次加几次
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("insert", "insert");
                                    map.put("areaType", areaType);
                                    map.put("area", area);//显示的分区
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有内存卡存入
                                        if (play == 1 || play == 2) {//图片/视频
                                            if (resName.contains("/")) {
                                                map.put("path", FileUtils.SDPATH + MateFileDir + resName.substring(resName.lastIndexOf("/"), resName.length()) + "|" + resID);
                                            } else {
                                                map.put("path", FileUtils.SDPATH + MateFileDir + "/" + resName + "|" + resID);
                                            }
                                        } else if (play == 3) {//网页
                                            resName = resName.replaceFirst("zhyh//", "");
                                            map.put("path", resName + "|" + resID);
                                        }
                                    } else {//存入手机内存
                                        if (play == 1 || play == 2) {//图片/视频
                                            if (resName.contains("/")) {
                                                map.put("path", context.getFilesDir().getAbsolutePath() + resName.substring(resName.lastIndexOf("/"), resName.length()) + "|" + resID);
                                            } else {
                                                map.put("path", context.getFilesDir().getAbsolutePath() + "/" + resName + "|" + resID);
                                            }
                                        } else if (play == 3) {//网页
                                            resName = resName.replaceFirst("zhyh//", "");
                                            map.put("path", resName + "|" + resID);
                                        }
                                    }
                                    map.put("pls_edTime", pls_edTime);//插播有pls pls可以为""
                                    map.put("pls_stdTime", pls_stdTime);
                                    map.put("priority", priority);
                                    listc.add(map);//插播的集合
                                }
                            }

                        } else if (playMode.equals("1")) {//普通播放
                            playcnt = playcnt.equals("") || playcnt == null ? "1" : playcnt;
                            int cnt = Integer.valueOf(playcnt);
                            for (int i = 0; i < cnt; i++) {//有几次加几次
                                HashMap<String, String> map = new HashMap<>();
                                map.put("normal", "normal");
                                map.put("areaType", areaType);
                                map.put("area", area);//显示的分区
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有内存卡存入
                                    if (play == 1 || play == 2) {//图片/视频
                                        if (resName.contains("/")) {
                                            map.put("path", FileUtils.SDPATH + MateFileDir + resName.substring(resName.lastIndexOf("/"), resName.length()) + "|" + resID);
                                        } else {
                                            map.put("path", FileUtils.SDPATH + MateFileDir + "/" + resName + "|" + resID);
                                        }
                                    } else if (play == 3) {//网页的处理
                                        resName = resName.replaceFirst("zhyh//", "");
                                        map.put("path", resName + "|" + resID);
                                    }
                                } else {
                                    if (play == 1 || play == 2) {//图片/视频
                                        if (resName.contains("/")) {
                                            map.put("path", context.getFilesDir().getAbsolutePath() + resName.substring(resName.lastIndexOf("/"), resName.length()) + "|" + resID);
                                        } else {
                                            map.put("path", context.getFilesDir().getAbsolutePath() + "/" + resName + "|" + resID);
                                        }
                                    } else if (play == 3) {
                                        resName = resName.replaceFirst("zhyh//", "");
                                        map.put("path", resName + "|" + resID);
                                    }
                                }
                                map.put("pls_stdTime", pls_stdTime);
                                map.put("pls_edTime", pls_edTime);//
                                map.put("priority", priority);
                                listz.add(map);//正常播放的集合
                            }
                        }
                    }

                    Collections.shuffle(listz);//重新排序洗牌
                    Collections.shuffle(listc);

                    Collections.sort(listz, new MySort(false, "priority", true));//按照优先级排序
                    Collections.sort(listc, new MySort(false, "priority", true));

                    for (HashMap<String, String> hm : listz) {
                        Log.e("program-path->", hm.get("areaType") + "|" + hm.get("area") + "|" + hm.get("path"));
                    }


                    if (listz.size() > 0) {
                        listgroup.add(listz);
                    }
                    if (listc.size() > 0) {
                        listgroup.add(listc);
                    }
                }
            }

        }

        return listgroup;
    }

    public ArrayList<HashMap<String, String>> parseModels(Models models) {
        try {
            if (dbUtils.findFirst(Model_SQL.class) != null) {
                return null;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        List<Model> modelList = models.getmList();
        ArrayList<HashMap<String, String>> modelArrayList = new ArrayList<>();
        HashMap<String, String> modelMap = new HashMap<>();
        if (modelList.size() > 0) {
            for (int i = 0; i < modelList.size(); i++) {
                Model model = modelList.get(i);
                String id = model.getId();
                String mbgColor = model.getBgcolor();
                String mbgImg = model.getBgimg();
                List<Area> areaList = model.getaList();
                if (areaList.size() > 0) {
                    for (int j = 0; j < areaList.size(); j++) {
                        Area area = areaList.get(j);
                        String areaId = area.getId();
                        int x = area.getX();
                        int y = area.getY();
                        int width = area.getWidth();
                        int height = area.getHeight();

                        try {//模型解析一次就存入数据库
                            Model_SQL sql = new Model_SQL();
                            sql.setAreaType(id);
                            sql.setBgColor(mbgColor);
                            sql.setBgImg(mbgImg);
                            sql.setArea(areaId);
                            sql.setX(x);
                            sql.setY(y);
                            sql.setWidth(width);
                            sql.setHeight(height);
                            dbUtils.save(sql);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        modelMap.put("modelId", id);
                        modelMap.put("bgcolor", mbgColor);
                        modelMap.put("bgimg", mbgImg);
                        modelMap.put("areaId", areaId);
                        modelMap.put("x", x + "");
                        modelMap.put("y", y + "");
                        modelMap.put("width", width + "");
                        modelMap.put("height", height + "");
                        modelArrayList.add(modelMap);
                    }
                }
            }
        }
        return modelArrayList;
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
                    String localMd5 = MD5Utils.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件MD5校验成功，且下载成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件MD5校验成功，文件下载成功！");
                        EventBus.getDefault().post(new StringModel("models.xml", "true"));
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件MD5校验失败！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件MD5校验失败！");
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
                    String localMd5 = MD5Utils.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件下载成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--文件下载成功！");
                        EventBus.getDefault().post(new StringModel("models.xml", "true", ""));
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--文件MD5校验失败！");
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
                    downLoadBgImage(bhref, bmd5, ipAndPort);//下载图片资源  就是为default准备的
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
                    if (!TextUtils.isEmpty(rhref)) {//防止报空
                        int play = ju.getMediaFileType(rhref);
                        if (play == 1 || play == 2) {//过滤掉网页，资源为视频或者图片时下载
                            downLoadVideo(rhref, rmd5, resId, ipAndPort, clist, count, detail, command);//下载所有视频资源
                        }
                    }
                }
//----------------------------发送界面-------------------------------------------------------------------------
                if (count.size() == clist.size()) {//成功 【发送广播】
                    command.setStatus(DOWN_COMPLETE_STATE);
                    EventBus.getDefault().post(new StringModel("resource", "true"));
                } else {//总数和成功的总数不相等就是有失败的
                    command.setStatus(DOWN_FAIL_STATE);
                    EventBus.getDefault().post(new StringModel("resource", "false"));
                }//----------------------------------------------保存----------------------------------------------------------
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
                Log.e("心跳下载的资源文件:", isSave + "");
            }
        }
    }

    private void downLoadVideo(String link, String md5, String resId, String ipAndPort, final ArrayList<Content> list, ArrayList<String> count, final Detail detail, final Command command) {

        HttpHelper httpHelper = new HttpHelper();
        String fileName = link.substring(link.lastIndexOf("/"), link.length());//得到文件的名称
        String dateStStart = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        try {
            final Content content = new Content();//被final修饰的对象可以set值。不能再次new了
            content.setResid(resId);
            content.setFilename(fileName);

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                int flag = httpHelper.downloadVideo("http://" + ipAndPort + "/zhyh/" + link, MateFileDir, fileName, new FileUtils.ProgressListener() {
                    @Override
                    public void update(float progress) {
                        content.setProcess(String.format("%.2f", progress * 100) + "%");
                        content.setStatus(ParseManager.DOWN_ING_STATE);//正在下载中
                        //每次下完一个视频就保存一次文件
                        list.add(content);
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
                        list.remove(list.size() - 1);

                    }
                }, md5);
                String saveFlag = "";
                if (flag == 0) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    File videoFile = new File(FileUtils.SDPATH + MateFileDir + fileName);
                    String localMd5 = MD5Utils.getFileMD5(videoFile);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验成功！");
                        count.add(localMd5);//成功的下载的个数
                        content.setProcess("100%");
                        content.setStatus(DOWN_COMPLETE_STATE);//下载成功
                        saveFlag = DOWN_COMPLETE_STATE;
                        FileUtils.saveToSDCard("downloadlog_" + bd.ID + "_" + dateSt + ".log", MateFileDir, resId + "|" + dateStStart + "|" + saveFlag + "|");//保存的日志。暂时不做
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败");
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验失败！");
                    }
                } else if (flag == -1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载失败！");
                    saveFlag = DOWN_ING_STATE;
                    content.setStatus(RESOURCE_FILE_NO_EXIST);//源文件不存在
                    content.setProcess("0%");
                    FileUtils.saveToSDCard("downloadlog_" + bd.ID + "_" + dateSt + ".log", MateFileDir, resId + "|" + dateStStart + "|" + saveFlag + "|");
                } else if (flag == 1) {//已经存在的话就设置为下载成功
                    content.setProcess("100%");
                    content.setStatus(DOWN_COMPLETE_STATE);//下载成功
                    count.add(md5);
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
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验成功！");
                        count.add(localMd5);//成功的下载的个数
                        fos.write((resId + "|" + dateStStart + "|" + DOWN_COMPLETE_STATE + "|").getBytes());//写入下载的log成功0001
                        fos.close();
                        content.setProcess("100%");
                        content.setStatus(DOWN_COMPLETE_STATE);//下载成功
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + " MD5校验失败");
                    }
                } else {//下载失败
                    Log.e(LogToFile.TEST_LOG, fileName + " 下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + " 下载失败！");
                    fos.write((resId + "|" + dateStStart + "|" + DOWN_ING_STATE + "|").getBytes());//写入下载的log失败0002
                    fos.close();
                    content.setProcess("0%");
                    content.setStatus(RESOURCE_FILE_NO_EXIST);//源文件不存在
                }
                list.add(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoadBgImage(String link, String md5, String ipAndPort) {
        HttpHelper httpHelper = new HttpHelper();
        String fileName = link.substring(link.lastIndexOf("/"), link.length());//得到文件的名称
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + link, MateFileDir, fileName, md5);
                File file1 = new File(FileUtils.SDPATH + MateFileDir + fileName);
                if (flag == 0) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载成功！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    String localMd5 = MD5Utils.getFileMD5(file1);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功，且下载成功！");
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验成功，且下载成功！");
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败！");
                        LogToFile.e(LogToFile.LOG, fileName + "--MD5校验失败！");
                    }
                } else if (flag == 1) {
                    Log.e(LogToFile.TEST_LOG, fileName + "--已存在！");
                    LogToFile.e(LogToFile.LOG, fileName + "--已存在！");
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
                    LogToFile.e(LogToFile.LOG, fileName + "--下载成功！");
                    String localMd5 = MD5Utils.getFileMD5(file);
                    if (localMd5.equals(md5)) {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验成功！");
                    } else {
                        Log.e(LogToFile.TEST_LOG, fileName + "--MD5校验失败！");
                    }
                } else {//下载失败
                    Log.e(LogToFile.TEST_LOG, fileName + "--下载失败！");
                    LogToFile.e(LogToFile.LOG, fileName + "--下载失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectLayout() {
        xStream.processAnnotations(Models.class);
        Models models = (Models) xStream.fromXML(new File(FileUtils.SDPATH + "xml/" + "models.xml"));
        ArrayList<HashMap<String, String>> modelsList = parseModels(models);
        for (int k = 0; k < modelsList.size(); k++) {
            HashMap<String, String> hashMap = modelsList.get(k);
            String modelId = hashMap.get("modelId");
            switch (modelId) {
                case "model_full_h":
                    String model_full_h_bgColor = hashMap.get("bgcolor");
                    String model_full_h_bgImg = hashMap.get("bgimg");

                    break;

                case "model_full_v":
                    String model_full_v_bgColor = hashMap.get("bgcolor");
                    String model_full_v_bgImg = hashMap.get("bgimg");

                    break;

                case "model_second_h":
                    String model_second_h_areaId = hashMap.get("areaId");
                    if (model_second_h_areaId.equals("area1")) {
                        String area1_x = hashMap.get("x");
                        String area1_y = hashMap.get("y");
                        String area1_width = hashMap.get("width");
                        String area1_height = hashMap.get("height");
                    } else {
                        String area2_x = hashMap.get("x");
                        String area2_y = hashMap.get("y");
                        String area2_width = hashMap.get("width");
                        String area2_height = hashMap.get("height");
                        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(Integer.valueOf(area2_width), Integer.valueOf(area2_height));
                        l.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    }
                    break;

                case "model_second_v":
                    String model_second_v_areaId = hashMap.get("areaId");
                    if (model_second_v_areaId.equals("area1")) {
                        String area1_x = hashMap.get("x");
                        String area1_y = hashMap.get("y");
                        String area1_width = hashMap.get("width");
                        String area1_height = hashMap.get("height");
                    } else {
                        String area2_x = hashMap.get("x");
                        String area2_y = hashMap.get("y");
                        String area2_width = hashMap.get("width");
                        String area2_height = hashMap.get("height");
                    }
                    break;

                case "model_three_h":
                    String model_three_h_areaId = hashMap.get("areaId");
                    if (model_three_h_areaId.equals("area1")) {
                        String area1_x = hashMap.get("x");
                        String area1_y = hashMap.get("y");
                        String area1_width = hashMap.get("width");
                        String area1_height = hashMap.get("height");
                    } else if (model_three_h_areaId.equals("area2")) {
                        String area2_x = hashMap.get("x");
                        String area2_y = hashMap.get("y");
                        String area2_width = hashMap.get("width");
                        String area2_height = hashMap.get("height");
                    } else {
                        String area3_x = hashMap.get("x");
                        String area3_y = hashMap.get("y");
                        String area3_width = hashMap.get("width");
                        String area3_height = hashMap.get("height");
                    }
                    break;

            }
        }
    }


    public void parseCommand(String xmlCommand, final String ipAndPort, final String macAdress) {//模拟真实解析环境   4.1任务轮询/心跳接口
        xStream.processAnnotations(Command.class);
        Command command = (Command) xStream.fromXML(xmlCommand);
        if (command != null) {
            TaskList task = command.getTaskList();
            if (task != null) {
                int taskCount = task.getTaskcount();
                if (taskCount == 0) {//没有任务
                    return;
                } else {
                    List<TaskItem> taskItemList = task.getTaskitemList();
                    for (int i = 0; i < taskItemList.size(); i++) {
                        TaskItem taskItem = taskItemList.get(i);
                        String taskType = taskItem.getTasktype();
                        String taskId = taskItem.getTaskid();
                        switch (taskType) {
                            case "program"://4.1.3 任务类型为播放内容
                                Contents pcontents = taskItem.getContents();//contents  只有一个
                                List<Content> contentList = pcontents.getContentList(); //content有多个
                                if (contentList.size() > 0) {
                                    for (int j = 0; j < contentList.size(); j++) {
                                        Content pcontent = contentList.get(j);
                                        String cSize = pcontent.getCsize();
                                        programPath = pcontent.getLink();//prokgram文件的下载路径
                                        final String md5 = pcontent.getMd5();
                                        final String playMode = pcontent.getPlaymode();

                                        new Thread(new Runnable() {//【下载节目单的线程】
                                            @Override
                                            public void run() {
                                                //下载program.xml文件
                                                String dirName = "xml";//向内存设备中新建文件夹
                                                String fileName = programPath.substring(programPath.lastIndexOf("/"), programPath.length());//播放节目单的名称
                                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//有内存卡存入
                                                    int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + programPath, dirName, fileName, md5);
                                                    File file = new File(FileUtils.SDPATH + dirName + fileName);
                                                    if (flag == 0) {
                                                        try {
                                                            String localMd5 = MD5Utils.getFileMD5(file);
                                                            if (md5.equals(localMd5)) {
                                                                Log.e(LogToFile.TEST_LOG, fileName + "--文件MD5校验成功!");
                                                                Log.e(LogToFile.TEST_LOG, fileName + "--文件下载成功!");
                                                                LogToFile.e(LogToFile.LOG, fileName + "--文件下载成功!");
                                                                StringModel model = new StringModel("program", playMode, fileName);
                                                                EventBus.getDefault().post(model);
                                                            } else {
                                                                alarmReport(ParseManager.MD5_ERROR, "program.xml文件", macAdress, ipAndPort);
                                                            }
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if (flag == 1) {
                                                        Log.e(LogToFile.TEST_LOG, fileName + "--文件已经存在!");
                                                        LogToFile.e(LogToFile.LOG, fileName + "--文件已经存在!");
                                                        StringModel model = new StringModel("program", playMode, fileName);
                                                        EventBus.getDefault().post(model);
                                                    } else if (flag == -1) {
                                                        alarmReport(ParseManager.DOWN_FAIL_STATE, "program.xml文件", macAdress, ipAndPort);
                                                        Log.e(LogToFile.TEST_LOG, fileName + "--文件下载失败!");
                                                        LogToFile.e(LogToFile.LOG, fileName + "--文件下载失败!");
                                                    }
                                                } else {//否则存入手机内存
                                                    File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //得到xml目录路径
                                                    File file = new File(workDir, fileName);//创建文件
                                                    try {
                                                        InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + programPath);
                                                        boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                                                        if (isSave) {
                                                            Log.e(LogToFile.TEST_LOG, fileName + "--文件下载成功!");
                                                            LogToFile.e(LogToFile.LOG, fileName + "--文件下载成功!");
                                                            StringModel model = new StringModel("program", playMode, fileName);
                                                            EventBus.getDefault().post(model);
                                                        } else {
                                                            alarmReport(ParseManager.DOWN_FAIL_STATE, "program.xml文件", macAdress, ipAndPort);
                                                            Log.e(LogToFile.TEST_LOG, fileName + "--文件下载失败!");
                                                        }
                                                    } catch (IOException e) {
                                                        Log.e(LogToFile.TEST_LOG, fileName + "--文件下载失败!");
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                        }).start();
                                    }
                                }

                                break;

                            case "upgrade"://4.1.4任务类型为软件升级
                                String version = taskItem.getVersion();
                                String upGradePath = taskItem.getLink();
                                apkUpGrade(taskId, version, upGradePath, macAdress, ipAndPort);
                                break;

                            case "control"://4.1.5任务类型为控制类任务(无法实现远程开机)
                                String control = taskItem.getControl();
                                Intent intent = new Intent();
                                if ("0".equals(control)) {//重启指令(暂时实现不来)

                                } else if ("1".equals(control)) {//关机指令 发送关机广播
                                    intent.setAction("com.abs.plbs");
                                    context.sendBroadcast(intent);
                                } else if ("2".equals(control)) {//唤醒指令(暂时实现不来)

                                }
                                break;

                            case "realtimemsg": //4.1.6即时消息类任务
                                String fontSize = taskItem.getFontsize();
                                String bgColor = taskItem.getBgcolor();
                                String fontColor = taskItem.getFontcolor();
                                String position = taskItem.getPosition();
                                String speed = taskItem.getSpeed();
                                String startTime = taskItem.getStarttime();
                                String endTime = taskItem.getEndtime();
                                String count = taskItem.getCount();
                                String timeLength = taskItem.getTimelength();
                                String message = taskItem.getMessage();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("fontSize", fontSize);
                                hashMap.put("bgColor", bgColor);
                                hashMap.put("fontColor", fontColor);
                                hashMap.put("position", position);
                                hashMap.put("speed", speed);
                                hashMap.put("startTime", startTime);
                                hashMap.put("endTime", endTime);
                                hashMap.put("count", count);
                                hashMap.put("timeLength", timeLength);
                                hashMap.put("message", message);
                                LogToFile.e(LogToFile.LOG, "字幕下发成功，内容为：" + message);
                                StringModel sm = new StringModel("realtimemsg", hashMap);
                                EventBus.getDefault().post(sm);//发送给mainactivity  【发送广播】
                                break;

                            case "cancelrealtimemsg"://4.1.7取消即时消息类任务
                                StringModel stringModel = new StringModel("cancelrealtimemsg");
                                EventBus.getDefault().post(stringModel);//发送给mainactivity
                                break;

                            case "config"://4.1.8.配置终端参数任务
                                Config config = taskItem.getConfig();
                                String startUpTime = config.getStartuptime();//开机时间
                                String shutDowntime = config.getShutdowntime();//关机时间
                                String diskSpaceAlarm = config.getDiskspacealarm();//硬盘阀值
                                String serverConfig = config.getServerconfig();//服务器信息
                                String selectIngerVal = config.getSelectinterval();//轮循时间
                                String volume = config.getVolume();
                                String ftpServer = config.getFtpserver();//ftp服务器
                                String httpServer = config.getHttpserver();//服务器地址
                                String downLoadRate = config.getDownloadrate();//下载速率
                                String downLoadTime = config.getDownloadtime();//下载时间段
                                String logServer = config.getLogserver();//上传日志的服务器路径
                                String upLoadLogTime = config.getUploadlogtime();//日志定时上传的时间 格式HH:MM:SS
                                String keepLogTime = config.getKeeplogtime();//日志保留时间（运行日志、播放日志、下载日志、调试）单位为 天  默认为1周

                                try {//将配置终端任务参数存到数据库，通知上报时上传

                                    Config_SQL conf = dbUtils.findFirst(Config_SQL.class);
                                    if (conf == null) {
                                        Config_SQL config_sql = new Config_SQL();
                                        config_sql.setStartuptime(startUpTime);
                                        config_sql.setShutdowntime(shutDowntime);
                                        config_sql.setDiskspacealarm(diskSpaceAlarm);
                                        config_sql.setServerconfig(serverConfig);
                                        config_sql.setSelectinterval(selectIngerVal);
                                        config_sql.setVolume(volume);
                                        config_sql.setFtpserver(ftpServer);
                                        config_sql.setHttpserver(httpServer);
                                        config_sql.setDownloadrate(downLoadRate);
                                        config_sql.setDownloadtime(downLoadTime);
                                        config_sql.setLogserver(logServer);
                                        config_sql.setUploadlogtime(upLoadLogTime);
                                        config_sql.setKeeplogtime(keepLogTime);
                                        dbUtils.save(config_sql);
                                    } else {
                                        conf.setStartuptime(startUpTime);
                                        conf.setShutdowntime(shutDowntime);
                                        conf.setDiskspacealarm(diskSpaceAlarm);
                                        conf.setServerconfig(serverConfig);
                                        conf.setSelectinterval(selectIngerVal);
                                        conf.setVolume(volume);
                                        conf.setFtpserver(ftpServer);
                                        conf.setHttpserver(httpServer);
                                        conf.setDownloadrate(downLoadRate);
                                        conf.setDownloadtime(downLoadTime);
                                        conf.setLogserver(logServer);
                                        conf.setUploadlogtime(upLoadLogTime);
                                        conf.setKeeplogtime(keepLogTime);
                                        dbUtils.update(conf);
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                                break;

                            case "controlprogram"://4.1.9.播放控制类任务(暂时没实现)
                                String cmd = taskItem.getCmd();
                                if ("1".equals(cmd)) {//暂停

                                } else if ("2".equals(cmd)) {//恢复

                                } else if ("3".equals(cmd)) {//删除

                                } else if ("4".equals(cmd)) {//取消插播

                                }
                                break;

                            case "cfgreport"://4.1.10 通知终端配置上报
                                cfGrePort(macAdress, ipAndPort);
                                break;

                            case "workstatusreport"://4.1.11 通知终端工作状态上报  (成都消息 可以不用上报)
                                workStatReport(taskId, macAdress, ipAndPort);
                                break;

                            case "monitorreport"://4.1.12 通知终端在播内容上报
                                StringModel monitorModel = new StringModel("monitorreport", macAdress, ipAndPort);
                                EventBus.getDefault().post(monitorModel);//发送给mainactivity
                                break;

                            case "logreport"://4.1.13 通知终端日志上报  (成都消息 暂时可以不用上报)
//                                String logType = taskItem.getLogtype();
//                                if (logType.equals("runlog")) {//运行日志
//                                } else if (logType.equals("playlog")) {  //播放日志
//                                } else if (logType.equals("downloadlog")) {//下载日志
//                                }
//                                logUp("downloadlog"/*logType*/, taskId,macAdress,ipAndPort);
//                                break;

                            case "downloadres"://4.1.14 通知终端【下载资源文件的线程】
                                Contents contents = taskItem.getContents();
                                List<Content> resContentList = contents.getContentList();
                                if (resContentList.size() > 0) {
                                    for (int k = 0; k < resContentList.size(); k++) {
                                        Content content = resContentList.get(k);
                                        String rSize = content.getCsize();
                                        rPath = content.getLink();//res资源文件的下载路径
                                        final String rmd5 = content.getMd5();

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //下载资源文件
                                                String dirName = "xml";//向内存设备中新建文件夹
                                                String name = rPath.substring(rPath.lastIndexOf("/"), rPath.length());//资源文件的名称
                                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡存入)
                                                    int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + rPath, dirName, name, rmd5);
                                                    File file = new File(FileUtils.SDPATH + dirName + name);
                                                    if (flag == 0) {
                                                        try {
                                                            String localmd5 = MD5Utils.getFileMD5(file);
                                                            if (rmd5.equals(localmd5)) {
                                                                Log.e(LogToFile.TEST_LOG, name + "--文件下载成功！");
                                                                LogToFile.e(LogToFile.LOG, name + "--文件下载成功！");
                                                                Log.e(LogToFile.TEST_LOG, name + "--文件MD5校验成功！");
                                                                xStream.processAnnotations(Resource.class);//下载成功就解析
                                                                Resource resource = (Resource) xStream.fromXML(file);
                                                                String xmlResource = xStream.toXML(resource);
                                                                parseResource(xmlResource, ipAndPort);
                                                            } else {
                                                                alarmReport(ParseManager.MD5_ERROR, "resource.xml文件", macAdress, ipAndPort);
                                                            }
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if (flag == 1) {
                                                        Log.e(LogToFile.TEST_LOG, name + "--文件已经存在！");
                                                        LogToFile.e(LogToFile.LOG, name + "--文件已经存在！");
                                                        EventBus.getDefault().post(new StringModel("resource", "true"));
                                                    } else {
                                                        alarmReport(ParseManager.DOWN_FAIL_STATE, "resource.xml文件", macAdress, ipAndPort);
                                                        Log.e(LogToFile.TEST_LOG, name + "--文件下载失败！");
                                                    }
                                                } else {//(否则入手机内存)
                                                    File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //创建xml目录
                                                    File file = new File(workDir, name);//创建文件
                                                    try {
                                                        InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + rPath);
                                                        boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                                                        if (isSave) {
                                                            Log.e(LogToFile.TEST_LOG, name + "--文件下载成功！");
                                                            LogToFile.e(LogToFile.LOG, name + "--文件下载成功！");
                                                            xStream.processAnnotations(Resource.class);//下载成功就解析
                                                            Resource resource = (Resource) xStream.fromXML(file);
                                                            String xmlResource = xStream.toXML(resource);
                                                            parseResource(xmlResource, ipAndPort);
                                                        } else {
                                                            Log.e(LogToFile.TEST_LOG, name + " 文件下载失败！");
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }).start();

                                    }
                                }

                                break;

                            case "downloadstatusreport"://4.1.15 通知终端上报资源下载状态
                                sourceDownReport(macAdress, ipAndPort);
                                break;
                        }

                        //调用4.2 终端任务获取状态上报接口 (终端针对每个taskitem里面的任务进行反馈)
                        taskGetReport(taskId, taskType, macAdress, ipAndPort);
                    }
                }
            }
        }
    }


    private void apkUpGrade(final String taskId, final String version, final String path, final String macAdress, final String ipAndPort) {//4.1.4任务类型为软件升级
        new Thread(new Runnable() {
            @Override
            public void run() {
                String zipName = path.substring(path.lastIndexOf("/"), path.length());
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    int flag = httpHelper.downlaodFile("http://" + ipAndPort + "/zhyh/" + path, MateFileDir, zipName, "");//下载没playdev
                    if (flag == 0) {
                        boolean isZip = ZipUtils.upZipFile(new File(FileUtils.SDPATH + MateFileDir + zipName), FileUtils.SDPATH + "Apk", false, new ZipUtils.ApkNameListener() {
                            @Override
                            public void getApkName(String apkName) {
                                ApkName = apkName;
                            }
                        });
                        if (isZip) {//解压成功
                            Log.e(LogToFile.TEST_LOG, "apk包 解压成功!");
                            LogToFile.e(LogToFile.LOG, "apk包 解压成功!");
                            if (!ApkName.equals("xyt.apk")) {//如果解压缩包的名字不是xyt.apk   就不是我们的apk
                                return;
                            }
                            ApkName = "/" + ApkName;
                            int install = installSlient(ApkName);//静默安装
                            if (install == 0) {
                                Log.e(LogToFile.TEST_LOG, "apk包 install 成功!");
                                LogToFile.e(LogToFile.LOG, "apk包 install 成功!");
                                upGradeReport(taskId, version, macAdress, ipAndPort);//升级完成后调用
                            } else {
                                Log.e(LogToFile.TEST_LOG, "apk包 install 失败!");
                            }
                        } else {
                            alarmReport(DOWN_FAIL_STATE, "apk升级失败", macAdress, ipAndPort);
                        }
                    } else if (flag == 1) {
                        Log.e(LogToFile.TEST_LOG, "apk包 已存在!");
                        LogToFile.e(LogToFile.LOG, "apk包 已存在!");
                    } else if (flag == -1) {
                        Log.e(LogToFile.TEST_LOG, "apk包 下载失败!");
                        LogToFile.e(LogToFile.LOG, "apk包 下载失败!");
                        alarmReport(DOWN_FAIL_STATE, "apk下载失败", macAdress, ipAndPort);
                    }
                } else {//否则存入手机内存
                    File workDir = context.getDir("Apk", Context.MODE_PRIVATE); //得到xml目录路径
                    File file = new File(workDir, zipName);//创建文件
                    try {
                        InputStream in = httpHelper.getInputStearmFormUrl("http://" + ipAndPort + "/zhyh/" + path);
                        boolean isSave = FileUtils.writeDatesToSDCard(file, in);
                        if (isSave) {
                            boolean isZip = ZipUtils.upZipFile(new File(FileUtils.SDPATH + MateFileDir + zipName), FileUtils.SDPATH + "Apk", false, new ZipUtils.ApkNameListener() {
                                @Override
                                public void getApkName(String apkName) {
                                    ApkName = apkName;
                                }
                            });
                            if (isZip) {//解压成功
                                if (!ApkName.equals("xyt.apk")) {//如果解压缩包的名字不是xyt.apk   就不是我们的apk
                                    return;
                                }
                                ApkName = "/" + ApkName;
                                int install = installSlient(ApkName);//静默安装
                                if (install == 0) {
                                    upGradeReport(taskId, version, macAdress, ipAndPort);//升级完成后调用
                                } else {
                                }
                            } else {
                                alarmReport(DOWN_FAIL_STATE, "apk升级失败", macAdress, ipAndPort);
                            }
                        } else {
                            alarmReport(DOWN_FAIL_STATE, "apk下载失败", macAdress, ipAndPort);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        alarmReport(DOWN_FAIL_STATE, "apk下载失败", macAdress, ipAndPort);
                    }
                }
            }
        }).start();
    }

    //静默安装   返回0 成功 1失败
    public int installSlient(String pathApk) {
        String cmd = "pm install -r " + FileUtils.SDPATH + "Apk" + pathApk;
        Process process = null;
        DataOutputStream os = null;

        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            int value = process.waitFor();
            return (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    public void taskGetReport(String taskId, final String taskType, String macAdress, String ipAndPort) {// 4.2 终端任务获取状态上报接口(提交终端)
        Command command = new Command();
        command.setTaskid(taskId);
        command.setTasktype(taskType);
        command.setStatus(TASK_COMPLETE_STATE);//0000 表示收完任务通知
        xStream.processAnnotations(Command.class);
        String xmlString = XML_HEAD + xStream.toXML(command);

        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "taskreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
//                Command command = (Command) xStream.fromXML(data);
//                if ("0".equals(command.getResult())) {
//                    Log.e("----》", " 4.2 终端任务获取状态上报接口(提交终端成功)" + ":" + taskType);
//                } else if ("1".equals(command.getResult())) {
//                    Log.e("5555", command.getErrorinfo());
//                }
            }
        });

    }


    public void sourceDownReport(String macAdress, String ipAndPort) {//4.3 资源下载状态报告(提交)
        String dirName = "xml";
        String xmlString = "";
        xStream.processAnnotations(Command.class);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(有内存卡读出)
            File file = new File(FileUtils.SDPATH + dirName + "/downloadstatusreport.xml");
            Command command = (Command) xStream.fromXML(file);
            xmlString = xStream.toXML(command);
        } else {//手机内存中读出
            File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //存在找到路径，不存在创建xml目录
            Command command = (Command) xStream.fromXML(new File(workDir.getAbsolutePath() + "/downloadstatusreport.xml"));
            xmlString = xStream.toXML(command);
        }

        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "downloadstatusreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {

                }
            }
        });

    }


    public void upGradeReport(String taskid, String version, String macAdress, String ipAndPort) { //4.4软件升级状态报告(暂停)
        Command command = new Command();
        command.setTaskid(taskid);
        command.setVersion(version);
        command.setStatus(DOWN_COMPLETE_STATE);
        xStream.processAnnotations(Command.class);
        String xmlString = xStream.toXML(command);

        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "upgradereport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {

                }
            }
        });
    }

    public void cfGrePort(String macAdress, String ipAndPort) {//4.5配置信息上报接口
        Command command = new Command();
        DeviceInfoUtils deviceInfoUtils = new DeviceInfoUtils(context);
        HashMap<String, String> hashMap = deviceInfoUtils.getWebMacInfo();//从本地设备得到
        command.setIp(hashMap.get("ip"));
        command.setMac(macAdress);//此处为手动输入的
        command.setGateway(hashMap.get("defaultGateway"));
        command.setDns(hashMap.get("dns"));
        command.setMask(hashMap.get("subnetMask"));//子网掩码
        command.setAppversion(hashMap.get("appVersion"));
        command.setDiskspacealarm(hashMap.get("totalPhysicalMemory"));//硬盘告警阀值，单位（MB）

        Config_SQL config_sql = null;
        try {
            config_sql = dbUtils.findFirst(Config_SQL.class);
            if (config_sql != null) {//从平台得到存入数据库后查到
                command.setStartuptime(config_sql.getStartuptime());//开机时间：HH:MM:SS
                command.setShutdowntime(config_sql.getShutdowntime());//关机时间：HH:MM:SS`
                command.setServerconfig(config_sql.getServerconfig());//服务器信息  http://ip:port/appname
                command.setSelectinterval(config_sql.getSelectinterval());//轮询时间间隔，单位 默认值10秒
                command.setVolume(config_sql.getVolume());//终端音量0-100
                command.setFtpserver(config_sql.getFtpserver());//ftp下载服务器地址  ftp://user:pwd@[ServerIp]:port/（建行提供账号加解密算法）
                command.setHttpserver(config_sql.getHttpserver());//http下载服务器地址
                command.setDownloadrate(config_sql.getDownloadrate());//终端下载速率：kb/s
                command.setDownloadtime(config_sql.getDownloadtime());//下载时间段：HH:MM:SS- HH:MM:SS，多个时间段之间用英文逗号分隔。各时段之间不能重叠。
                command.setLogserver(config_sql.getLogserver());//日志服务器路径。终端上报日志的服务器
                command.setUploadlogtime(config_sql.getUploadlogtime());//日志定时上传时间,格式: HH:MM:SS
                command.setKeeplogtime(config_sql.getKeeplogtime());//日志保留时间（运行日志、播放日志、下载日志、调试日志）单位：天如7天
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        String xmlString = xStream.toXML(command);
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "cfgreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {//上报成功

                } else if ("1".equals(command.getResult())) {
                    Log.e("失败原因：", command.getErrorinfo());
                }
            }
        });
    }

    public void monitorReport(String programName, String base64Bitmap, String macAdress, String ipAndPort, ArrayList<String> resIdList) {//4.7在播内容上报
        Command command = new Command();
        command.setProgramname(programName);//播放节目单名称
        java.util.Date date = new java.util.Date();//当前时间
        String dates = DateUtils.formatDate(date, "yyyyMMddHHmmss");
        command.setTime(dates);//格式：YYYYMMDDhhmmss 例如：20100620123445
        Contents contents = new Contents();
        ArrayList<Content> contentList = new ArrayList<Content>();
        for (int i = 0; i < resIdList.size(); i++) {
            if (resIdList.get(i) != null) {
                Content content = new Content();
                content.setResid(resIdList.get(i));
                contentList.add(content);
            }
        }
        contents.setContentList(contentList);
        command.setPicture(base64Bitmap);
        command.setContents(contents);

        xStream.processAnnotations(Command.class);
        String xmlString = xStream.toXML(command);
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "monitorreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {

                }
            }
        });
    }


//------------------------------------------------------暂时不用上报----------------------------------------------------------------------

    public void workStatReport(String taskId, String macAdress, String ipAndPort) {//4.6 工作状态上报接口
        Command command = new Command();
        command.setTaskid(taskId);
        command.setPlayerstatus("wakeup");//sleep休眠状态表示平台的远程关机后终端状态 sleep 和 wakeup

        String xmlString = xStream.toXML(command);
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "workstatusreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {


                } else if ("1".equals(command.getResult())) {
                    Log.e("失败原因：", command.getErrorinfo());
                }
            }
        });
    }

    //4.8 终端告警上报 终端发生错误后，自动上报
    public void alarmReport(String alarmCode, String alarmMsg, String macAdress, String ipAndPort) {
        Command command = new Command();
        command.setAlarmcode(alarmCode);
        command.setAlarmmsg(alarmMsg);
        java.util.Date date = new java.util.Date();
        String dates = DateUtils.formatDate(date, "yyyyMMddHHmmss");
        command.setTime(dates);//格式：YYYYMMDDhhmmss 例如：20100620123445

        String xmlString = xStream.toXML(command);
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "alarmreport", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
//                Command command = (Command) xStream.fromXML(data);
//                if ("0".equals(command.getResult())) {
//
//                }
            }
        });
    }

    public void logUp(final String logType, final String taskId, final String macAdress, final String ipAndPort) {//上传日志
        String path = "";
        Config_SQL config_sql = null;
        try {
            config_sql = dbUtils.findFirst(Config_SQL.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (config_sql != null) {//从平台得到存入数据库后查到
            String logSer = config_sql.getLogserver();//得到log上传的服务器地址
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//(从内存卡读出)
                path = FileUtils.SDPATH + MateFileDir + "/" + logType + "_" + bd.ID + "_" + dateSt + ".log";
            } else {//(从手机内存读出)
                File file = context.getFilesDir();
                path = file.getAbsolutePath() + "/" + logType + "_" + bd.ID + "_" + dateSt + ".log";
            }

            manager.upLoadFile(logSer, path, ipAndPort, new OkHttpManager.ReqCallBack<String>() {//(okhttp 上传log文件)
                @Override
                public void onReqSuccess(String result) {
                    logComplete(logType, taskId, macAdress, ipAndPort);//成功上报
                }

                @Override
                public void onReqFailed(String errorMsg) {
                    alarmReport(LOG_UP_FAILED, "日志上传失败", macAdress, ipAndPort);//失败报警
                }
            });
        }

    }

    private void logComplete(String logType, String taskId, String macAdress, String ipAndPort) {//4.9 日志上传完成上报
        Command command = new Command();
        command.setTaskid(taskId);
        command.setLogtype(logType);
        command.setLogname("temp_ runlog_省id+终端序列号_20110223"); //日志名称
        String xmlString = xStream.toXML(command);
        String url = "http://" + ipAndPort + "/zhyh/PlayDev";
        manager.postHead(url, "logcomplete", macAdress, xmlString, new OkHttpManager.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                xStream.processAnnotations(Command.class);
                Command command = (Command) xStream.fromXML(data);
                if ("0".equals(command.getResult())) {

                }
            }
        });
    }

}
