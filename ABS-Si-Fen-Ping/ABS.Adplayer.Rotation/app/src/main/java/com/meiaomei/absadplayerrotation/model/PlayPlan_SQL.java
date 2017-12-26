package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by huyawen on 2017/3/2.
 * 播放   实体类   存本地数据库
 */

@Table(name = "PlayPlan")
public class PlayPlan_SQL {


    @com.lidroid.xutils.db.annotation.Id(column = "ID")   //播放文件id
    public String Id;

    @Column(column = "Name") //播放文件名称
    public String name;

    @Column(column = "Type") //播放文件类型  .jpeg为图片   .mp4为视频
    public String type;

    @Column(column = "Size") //播放文件大小
    public long size;

    @Column(column = "PlayOrder") //播放顺序
    public int playOrder;

    @Column(column = "PlayDuration")//播放文件时长
    public long playDuration;

    @Column(column = "ProPackage") //播放文件所在文件夹
    public String proPackage;

    @Column(column = "AddTime")//文件添加时间
    public Date addTime;

    @Column(column = "AddUser")//文件添加操作人
    public String addUser;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }

    public long getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(long playDuration) {
        this.playDuration = playDuration;
    }

    public String getProPackage() {
        return proPackage;
    }

    public void setProPackage(String proPackage) {
        this.proPackage = proPackage;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }
}
