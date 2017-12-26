package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */

@Table(name = "TaskItem")
public class TaskItem_SQL {

    @Id(column = "TaskId")
    String taskid;     //任务id      任务编号uuid

    @Column(column = "TaskType")
    String tasktype;      //任务类型    program 播放内容

    @Column(column = "Version")
    String version;    //最新版本号

    @Column(column = "Link")
    String link;       //最新版本更新url

    @Column(column = "Control")
    String control;       //控制指令   0 重启  1 休眠  2 唤醒

    @Column(column = "FontSize")
    String fontSize;      //字体大小

    @Column(column = "BgColor")
    String bgColor;    //字幕背景颜色

    @Column(column = "FontColor")
    String fontcolor;  //字体颜色

    @Column(column = "Position")
    String position;   //显示位置

    @Column(column = "Speed")
    String speed;         //滚动速度  0慢  1 正常  2 快

    @Column(column = "StartTime")
    String starttime;    //开始时间

    @Column(column = "EndTime")
    String endtime;      //结束时间

    @Column(column = "Count")
    String count;        //播放次数    0表示一直播放

    @Column(column = "TimeLength")
    String timelength;   //播放时长

    @Column(column = "Message")
    String message;    //消息内容

    @Column(column = "Status")
    String status;

    @Column(column = "Cmd")
    String cmd;           //任务指令  1  暂停  2  恢复 3 删除 4 取消插播

    @Column(column = "LogType")
    String logtype;    //日志类型   runlog 运行日志   playlog  播放日志  download 下载日志


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

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
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
}
