package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by huyawen on 2017/3/1.
 * 自增主键 id 和 setting设置的json串  只有两个字段
 */

@Table(name = "SystemSetting")
public class SystemSetting_SQL {


    public int id;

    @Column(column = "Ip")
    public String ip;

    @Column(column = "Port")
    public  String port;

    @Column(column = "Mac")
    public  String mac;

    @Column(column = "Hearts")
    public  String hearts;

    @Column(column = "ImgTime")
    public  String imgTime;

    @Column(column = "ImgCnt")
    public  String imgCnt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getHearts() {
        return hearts;
    }

    public void setHearts(String hearts) {
        this.hearts = hearts;
    }

    public String getImgTime() {
        return imgTime;
    }

    public void setImgTime(String imgTime) {
        this.imgTime = imgTime;
    }

    public String getImgCnt() {
        return imgCnt;
    }

    public void setImgCnt(String imgCnt) {
        this.imgCnt = imgCnt;
    }
}
