package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */

//文件表

@Table(name = "Rfile")
public class Rfile_SQL {

    @Id(column = "ResId") //关联Res_SQL  的 ResId
    String resId;

    @Column(column = "Content_Id")
    String contentId;

    @Column(column = "ResName")
    String resName;

    @Column(column = "FtpAdd")
    String ftpAdd;

    @Column(column = "Href")
    String href;

    @Column(column = "Md5")
    String md5;

    @Column(column = "FileSize")
    String fileSize;


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
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
