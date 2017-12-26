package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/4/28.
 */

@Table(name = "Area")
public class Area_SQL {

    @Id(column = "Id")//值时 Area 为显示的分区id  是Res 的外键
    String area_id;

    @Column(column = "X")
    int x;

    @Column(column = "Y")
    int y;

    @Column(column = "Width")
    int width;

    @Column(column = "Height")
    int height;

    public String getArea() {
        return area_id;
    }

    public void setArea(String area) {
        this.area_id = area;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
