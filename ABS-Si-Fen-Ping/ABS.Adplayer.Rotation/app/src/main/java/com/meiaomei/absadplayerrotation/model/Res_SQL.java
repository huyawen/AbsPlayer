package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */

//资源表
@Table(name = "Res")
public class Res_SQL {

    @Id(column = "ResId") //关联Rfile_SQL 的 ResId
    String resid;

    @Column(column = "Pls_Id")
    String plsId;

    @Column(column = "ResName")
    String resname;

    @Column(column = "Area") //是  Area的主键的外键
    String area;

    @Column(column = "PlayCnt")
    String playcnt;

    @Column(column = "Priority")
    String priority;

    @Column(column = "StdTime")
    String stdtime;

    @Column(column = "EdTime")
    String edtime;


    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getPlsId() {
        return plsId;
    }

    public void setPlsId(String plsId) {
        this.plsId = plsId;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPlaycnt() {
        return playcnt;
    }

    public void setPlaycnt(String playcnt) {
        this.playcnt = playcnt;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStdtime() {
        return stdtime;
    }

    public void setStdtime(String stdtime) {
        this.stdtime = stdtime;
    }

    public String getEdtime() {
        return edtime;
    }

    public void setEdtime(String edtime) {
        this.edtime = edtime;
    }
}
