package com.meiaomei.absadplayerrotation.model.bean;

import java.util.Date;


/**
 * Created by huyawen on 2017/2/28.
 * 素材类
 */
public class Material {


    public String id	;	    //ID
    public String name	;	    //名称
    public String type	;	    //类型
    public int size		;	    //体积
    public int playOrder;		//播放顺序
    public int playDuration;	//播放时长
    public String proPackage;	//所在文件夹
    public Date addTime	;	    //添加时间
    public String addUser	;	//添加操作人


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(int playDuration) {
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
