package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */
@Table(name = "Pls")
public class Pls_SQL {

    @Id(column = "Pls_Id")
    String plsId;

    @Column(column = "Content_Id")
    String contentId;

    @Column(column = "AreaType") //关联屏幕样式id  即model sql中的id
    String areatype;

    @Column(column = "StdTime")
    String stdtime;

    @Column(column = "EdTime")
    String edtime;

    @Column(column = "PlayType") //defaultpls 默认的播放   normalpls 正常的播放
    String playType;

    public String getPlsId() {
        return plsId;
    }

    public void setPlsId(String plsId) {
        this.plsId = plsId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getAreatype() {
        return areatype;
    }

    public void setAreatype(String areatype) {
        this.areatype = areatype;
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

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }
}
