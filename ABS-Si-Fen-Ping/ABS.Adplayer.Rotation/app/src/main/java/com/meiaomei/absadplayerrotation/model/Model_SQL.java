package com.meiaomei.absadplayerrotation.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by meiaomei on 2017/8/7.
 */

@Table(name = "Model")
public class Model_SQL {

    int id;

    @Column(column = "AreaType")
    String areaType;

    @Column(column = "BgColor")
    String bgColor;

    @Column(column = "BgImg")
    String bgImg;

    @Column(column = "Area")
    String area;

    @Column(column = "X")
    int x;

    @Column(column = "Y")
    int y;

    @Column(column = "Width")
    int width;

    @Column(column = "Height")
    int height;

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
