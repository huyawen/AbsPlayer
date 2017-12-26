package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/5/2.
 */

@Table(name = "BgImg")
public class BgImg_SQL {

    public int id;

    @Column(column = "Content_Id")
    String contentId;

    @Column(column = "FtpAdd")
    String ftpAdd;

    @Column(column = "Href")
    String href;

    @Column(column = "md5")
    String md5;

    @Column(column = "filesize")
    String fileSize;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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
