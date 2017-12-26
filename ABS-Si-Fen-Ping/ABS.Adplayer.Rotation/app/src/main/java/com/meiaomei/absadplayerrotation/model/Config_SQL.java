package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/5/2.
 */

@Table(name = "Config")
public class Config_SQL {

    public int id;

    @Column(column = "StartUpTime")
    String startuptime;                   //开机时间   约束为0 当参数变化时 才需要提交

    @Column(column = "ShutDownTime")
    String shutdowntime;                  //关机时间

    @Column(column = "ShutDownTime")
    String diskspacealarm;                //硬盘告警阀

    @Column(column = "ServerConfig")
    String serverconfig;                  //服务器信息

    @Column(column = "SelectInterVal")
    String selectinterval;                //轮询时间间隔

    @Column(column = "Volume")
    String volume;                        //终端音量   0---100

    @Column(column = "FtpServer")
    String ftpserver;                     //ftp下载服务器地址列表

    @Column(column = "HttpServer")
    String httpserver;                    //http下载服务器地址列表


    @Column(column = "DownLoadRate")
    String downloadrate;                  //终端下载速度    kb/s  测速度由客户端控制

    @Column(column = "DownLoadTime")
    String downloadtime ;                 //下载时间断   下载时间段：HH:MM:SS- HH:MM:SS，多个时间段之间用英文逗号分隔。各时段之间不能重叠。

    @Column(column = "LogServer")
    String logserver;                     //日志服务器路径 终端上报的服务器

    @Column(column = "UpLoadLogTime")
    String uploadlogtime;                 //日志服务器路径。终端上报日志的服务器

    @Column(column = "KeepLogTime")
    String keeplogtime;                   //日志保留时间（运行日志、播放日志、下载日志、调试）单位为 天  默认为1周

    public String getKeeplogtime() {
        return keeplogtime;
    }

    public void setKeeplogtime(String keeplogtime) {
        this.keeplogtime = keeplogtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartuptime() {
        return startuptime;
    }

    public void setStartuptime(String startuptime) {
        this.startuptime = startuptime;
    }

    public String getShutdowntime() {
        return shutdowntime;
    }

    public void setShutdowntime(String shutdowntime) {
        this.shutdowntime = shutdowntime;
    }

    public String getDiskspacealarm() {
        return diskspacealarm;
    }

    public void setDiskspacealarm(String diskspacealarm) {
        this.diskspacealarm = diskspacealarm;
    }

    public String getServerconfig() {
        return serverconfig;
    }

    public void setServerconfig(String serverconfig) {
        this.serverconfig = serverconfig;
    }

    public String getSelectinterval() {
        return selectinterval;
    }

    public void setSelectinterval(String selectinterval) {
        this.selectinterval = selectinterval;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFtpserver() {
        return ftpserver;
    }

    public void setFtpserver(String ftpserver) {
        this.ftpserver = ftpserver;
    }

    public String getHttpserver() {
        return httpserver;
    }

    public void setHttpserver(String httpserver) {
        this.httpserver = httpserver;
    }

    public String getDownloadrate() {
        return downloadrate;
    }

    public void setDownloadrate(String downloadrate) {
        this.downloadrate = downloadrate;
    }

    public String getDownloadtime() {
        return downloadtime;
    }

    public void setDownloadtime(String downloadtime) {
        this.downloadtime = downloadtime;
    }

    public String getLogserver() {
        return logserver;
    }

    public void setLogserver(String logserver) {
        this.logserver = logserver;
    }

    public String getUploadlogtime() {
        return uploadlogtime;
    }

    public void setUploadlogtime(String uploadlogtime) {
        this.uploadlogtime = uploadlogtime;
    }
}
