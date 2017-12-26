package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/5/9.
 */
@XStreamAlias("model")
public class Model {

    @XStreamAsAttribute()
    @XStreamAlias("id") //属性必须用注解申明
    String id;

    @XStreamAsAttribute()
    @XStreamAlias("bgcolor")
    String bgcolor;

    @XStreamAsAttribute()
    @XStreamAlias("bgimg")
    String bgimg;


    @XStreamAsAttribute()
    @XStreamAlias("ftpAdd")
    String ftpAdd;


    @XStreamAsAttribute()
    @XStreamAlias("href")
    String href;


    @XStreamAsAttribute()
    @XStreamAlias("md5")
    String md5;


    @XStreamAsAttribute()
    @XStreamAlias("filesize")
    String fileSize;

    @XStreamImplicit(itemFieldName = "area") //节点注解  必须写
     List<Area> aList;

    public List<Area> getaList() {
        return aList;
    }

    public void setaList(List<Area> aList) {
        this.aList = aList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getFtpAdd() {
        return ftpAdd;
    }

    public void setFtpAdd(String ftpAdd) {
        this.ftpAdd = ftpAdd;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
