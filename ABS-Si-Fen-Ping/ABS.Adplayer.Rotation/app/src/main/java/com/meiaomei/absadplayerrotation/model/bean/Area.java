package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by meiaomei on 2017/5/9.
 */

@XStreamAlias("area")
public class Area {

    @XStreamAsAttribute()
    @XStreamAlias("id")
    String id;

    @XStreamAsAttribute()
    @XStreamAlias("x")
    int x;

    @XStreamAsAttribute()
    @XStreamAlias("y")
    int y;

    @XStreamAsAttribute()
    @XStreamAlias("width")
    int width;

    @XStreamAsAttribute()
    @XStreamAlias("height")
    int height;


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
