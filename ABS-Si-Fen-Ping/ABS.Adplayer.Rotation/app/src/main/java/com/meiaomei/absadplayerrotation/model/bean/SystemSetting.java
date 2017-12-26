package com.meiaomei.absadplayerrotation.model.bean;

import java.io.Serializable;

/**
 * Created by huyawen on 2017/3/1.
 * 系统设置实体类
 */

public class SystemSetting implements Serializable {

    public String webIp;		//Web服务器IP
    public String webPort;	    // Web服务器端口
    public int heartbeatDuration;	// 心跳时长
    public String monitorPort;	// 监听端口
    public String ftpIp	;	    // Ftp服务器IP
    public String ftpPort	;	// Ftp服务器端口
    public String ftpUsername;  // Ftp用户名
    public String ftpPassword;  // Ftp登录密码
    public int playMode	;	    // 播放模式
    public boolean startWhenBoot;	// 是否开机启动
    public boolean autoShutdown;	// 启用自动关机
    public long shutdownTime;	// 自动关机时间


    public String getWebIp() {
        return webIp;
    }

    public void setWebIp(String webIp) {
        this.webIp = webIp;
    }

    public String getWebPort() {
        return webPort;
    }

    public void setWebPort(String webPort) {
        this.webPort = webPort;
    }

    public int getHeartbeatDuration() {
        return heartbeatDuration;
    }

    public void setHeartbeatDuration(int heartbeatDuration) {
        this.heartbeatDuration = heartbeatDuration;
    }

    public String getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(String monitorPort) {
        this.monitorPort = monitorPort;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public boolean isStartWhenBoot() {
        return startWhenBoot;
    }

    public void setStartWhenBoot(boolean startWhenBoot) {
        this.startWhenBoot = startWhenBoot;
    }

    public boolean isAutoShutdown() {
        return autoShutdown;
    }

    public void setAutoShutdown(boolean autoShutdown) {
        this.autoShutdown = autoShutdown;
    }

    public long getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(long shutdownTime) {
        this.shutdownTime = shutdownTime;
    }
}
