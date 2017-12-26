package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by meiaomei on 2017/4/24.
 */

@XStreamAlias("command")//类注解
public class Command {

    @XStreamAlias("tasklist")
    TaskList taskList;

    @XStreamAlias("detail")
    Detail detail;          //资源节点

    @XStreamAlias("contents")//接口4.7.4有案例
    Contents contents;      //消息报文


    String status;             //下载状态   0001 下载完成  0002 下载中  0100 下载失败
    String result;          //服务器处理结果   0，成功  1 失败
    String cmdtype;         //同步节目的一个字段
    String csize;           ///同步节目单的大小

    String errorinfo;       //错误信息 具体信息
    String version;         //版本
    String taskid;             //任务id
    String tasktype;        //任务类型
    String playerstatus;    //播控终端状态           sleep休眠  wakeup唤醒
    String programname;     //播放节目单名称
    String time;            //抓取点时间
    String picture;         //把抓屏数据转换成jpg后，然后进行 base64编码 (节点作为可选属性，如果没有图片传null)
    String alarmcode;       //告警码
    String alarmmsg;        //告警时间
    String logtype;         //日志类型
    String logname;         //日志名称
    String link;            //资源清单url
    String md5;             //升级包 md5
    String servertime;       //服务器时间
    String playlistid;

    String ip;               //ip地址
    String mac;              //mac地址
    String gateway;          //网关
    String dns;              //域名
    String mask;             //网关
    String appversion;
    String startuptime;                   //开机时间   约束为0 当参数变化时 才需要提交
    String shutdowntime;                  //关机时间
    String diskspacealarm;                //硬盘告警阀
    String serverconfig;                  //服务器信息
    String selectinterval;                //轮询时间间隔
    String volume;                        //终端音量   0---100
    String ftpserver;                     //ftp下载服务器地址列表
    String httpserver;                    //http下载服务器地址列表
    String downloadrate;                  //终端下载速度    kb/s  测速度由客户端控制
    String downloadtime;                  //下载时间断   下载时间段：HH:MM:SS- HH:MM:SS，多个时间段之间用英文逗号分隔。各时段之间不能重叠。
    String logserver;                     //日志服务器路径 终端上报的服务器
    String uploadlogtime;                 //日志服务器路径。终端上报日志的服务器
    String keeplogtime;                   //日志保留时间（运行日志、播放日志、下载日志、调试）单位为 天  默认为1周


    public String getCsize() {
        return csize;
    }

    public void setCsize(String csize) {
        this.csize = csize;
    }

    public String getCmdtype() {
        return cmdtype;
    }

    public void setCmdtype(String cmdtype) {
        this.cmdtype = cmdtype;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getPlayerstatus() {
        return playerstatus;
    }

    public void setPlayerstatus(String playerstatus) {
        this.playerstatus = playerstatus;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAlarmcode() {
        return alarmcode;
    }

    public void setAlarmcode(String alarmcode) {
        this.alarmcode = alarmcode;
    }

    public String getAlarmmsg() {
        return alarmmsg;
    }

    public void setAlarmmsg(String alarmmsg) {
        this.alarmmsg = alarmmsg;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
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

    public String getKeeplogtime() {
        return keeplogtime;
    }

    public void setKeeplogtime(String keeplogtime) {
        this.keeplogtime = keeplogtime;
    }
}
