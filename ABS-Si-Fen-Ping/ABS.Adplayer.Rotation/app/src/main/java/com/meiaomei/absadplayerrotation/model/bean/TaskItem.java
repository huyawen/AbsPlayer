package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by meiaomei on 2017/4/18.
 */
public class TaskItem {   //注意   如果定义的属性是long类型的话会自动生成0的xml值

    String tasktype;      //任务类型    program 播放内容
    String taskid;     //任务id      任务编号uuid
    String version;    //最新版本号
    String link;       //最新版本更新url
    String control;       //控制指令   0 重启  1 休眠  2 唤醒
    String fontsize;      //字体大小
    String bgcolor;    //字幕背景颜色
    String fontcolor;  //字体颜色
    String position;   //显示位置
    String speed;         //滚动速度  0慢  1 正常  2 快
    String starttime;    //开始时间
    String endtime;      //结束时间
    String count;        //播放次数    0表示一直播放
    String timelength;   //播放时长
    String message;      //消息内容
    String status;
    String cmd;           //任务指令  1  暂停  2  恢复 3 删除 4 取消插播
    String logtype;       //日志类型   runlog 运行日志   playlog  播放日志  download 下载日志

    String uploadFile;    //在播内容上报的元素
    @XStreamAlias("contents")
    Contents contents;      //消息报文

    @XStreamAlias("config")
    Config config;     //配置实体类



    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTimelength() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength = timelength;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }
}
