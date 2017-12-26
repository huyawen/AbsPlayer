package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by meiaomei on 2017/4/27.
 */

public class Res {

    @XStreamAsAttribute()
    @XStreamAlias("resname")
    String resname;

    @XStreamAsAttribute()
    @XStreamAlias("resid")
    String resid;

    @XStreamAsAttribute()
    @XStreamAlias("area")
    String area;

    @XStreamAsAttribute()
    @XStreamAlias("playcnt")
    String playcnt;

    @XStreamAsAttribute()
    @XStreamAlias("priority")
    String priority;

    @XStreamAsAttribute()
    @XStreamAlias("stdtime")
    String stdtime;

    @XStreamAsAttribute()
    @XStreamAlias("edtime")
    String edtime;

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
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
