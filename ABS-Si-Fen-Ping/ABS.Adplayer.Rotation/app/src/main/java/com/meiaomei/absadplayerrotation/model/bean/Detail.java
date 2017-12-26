package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/4/24.
 */
public class Detail {

    @XStreamImplicit(itemFieldName = "content")  //节点注解
    List<Content> deContentList;

    public List<Content> getDeContentList() {
        return deContentList;
    }

    public void setDeContentList(List<Content> deContentList) {
        this.deContentList = deContentList;
    }
}
