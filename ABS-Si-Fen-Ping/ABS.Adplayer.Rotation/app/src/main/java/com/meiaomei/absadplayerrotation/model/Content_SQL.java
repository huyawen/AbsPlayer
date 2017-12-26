package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */

@Table(name = "Content")
public class Content_SQL {

    @Id(column = "Content_Id")
    String contentId;

    @Column(column = "TaskId")
    String taskId;

    @Column(column = "Csize")
    String csize;            //文件大小

    @Column(column = "Link")
    String link;             //文件  下载相对路径

    @Column(column = "Md5")
    String md5;              //文件的md5值

    @Column(column = "PlayMode")
    String playmode;          //播放类型  插播 0，正常播 1

  /*  @Column(column = "ResId")
    String resid;            // 资源id  素材id

    @Column(column = "FileName")
    String filename;         //资源名称

    @Column(column = "Process")
    String process;          //资源下载进度

    @Column(column = "Status")
    String status;           //资源下载状态  0001 下载成功0001 下载中0002 源文件不存在0101 下载超时 0102 账号权限需错误 0103 文件md5校验出错 0104 链接超时 0105

    @Column(column = "StarDate")
    String stardate;

    @Column(column = "EndDate")
    String enddate;
*/
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

}
