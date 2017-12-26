package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by meiaomei on 2017/4/24.
 */
public class Content {


    String csize;            //文件大小
    String link;             //文件  下载相对路径
    String md5;              //文件的md5值
    String playmode;          //播放类型  插播 0，正常播 1
    String stardate;
    String enddate;

    //属性注解
    @XStreamAsAttribute()
    @XStreamAlias("resid")
    String resid;            // 资源id  素材id

    //属性注解
    @XStreamAsAttribute()
    @XStreamAlias("filename")
    String filename;         //资源名称

    //属性注解
    @XStreamAsAttribute()
    @XStreamAlias("process")
    String process;          //资源下载进度

    //属性注解
    @XStreamAsAttribute()
    @XStreamAlias("status")
    String status;           //资源下载状态  0001 下载成功0001 下载中0002 源文件不存在0101 下载超时 0102 账号权限需错误 0103 文件md5校验出错 0104 链接超时 0105


    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getCsize() {
        return csize;
    }

    public void setCsize(String csize) {
        this.csize = csize;
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

    public String getPlaymode() {
        return playmode;
    }

    public void setPlaymode(String playmode) {
        this.playmode = playmode;
    }

    public String getStardate() {
        return stardate;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
