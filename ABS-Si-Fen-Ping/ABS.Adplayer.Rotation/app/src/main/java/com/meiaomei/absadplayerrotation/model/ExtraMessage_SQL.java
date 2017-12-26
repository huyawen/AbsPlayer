package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/6/2.
 */

@Table(name="ExtraMessage")
public class ExtraMessage_SQL {

    public int id;

    @Column(column = "PlayMode")
    String playMode;

    @Column(column = "PgFileName")
    String pgFileName;



    public String getPlayMode() {
        return playMode;
    }

    public void setPlayMode(String playMode) {
        this.playMode = playMode;
    }

    public String getPgFileName() {
        return pgFileName;
    }

    public void setPgFileName(String pgFileName) {
        this.pgFileName = pgFileName;
    }

}
