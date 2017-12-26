package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/4/18.
 */

public class Contents {

    @XStreamImplicit(itemFieldName = "content")  //节点注解
    List<Content> contentList;    //播放列表文件


    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

}
