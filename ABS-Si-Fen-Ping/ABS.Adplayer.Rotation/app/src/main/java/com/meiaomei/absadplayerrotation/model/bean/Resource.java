package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/4/28.
 */

@XStreamAlias("resource")
public class Resource {

    @XStreamAlias("model")
    Model model;

    @XStreamImplicit(itemFieldName = "bgimg")
    List<BgImg> bgImgList;

    @XStreamImplicit(itemFieldName = "file")
    List<Rfile> rfileList;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<BgImg> getBgImgList() {
        return bgImgList;
    }

    public void setBgImgList(List<BgImg> bgImgList) {
        this.bgImgList = bgImgList;
    }

    public List<Rfile> getRfileList() {
        return rfileList;
    }

    public void setRfileList(List<Rfile> rfileList) {
        this.rfileList = rfileList;
    }

    public  static  class BgImg{

        @XStreamAsAttribute()
        @XStreamAlias("ftpAdd") //此处注意 案例中全是小写  此处附录为大写  待确定
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


    public  static  class Rfile{

        @XStreamAsAttribute()
        @XStreamAlias("resid")
        String resId;

        @XStreamAsAttribute()
        @XStreamAlias("resname")
        String resName;

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

}
