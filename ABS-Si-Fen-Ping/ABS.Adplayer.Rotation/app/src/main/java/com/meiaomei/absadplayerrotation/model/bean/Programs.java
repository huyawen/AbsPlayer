package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/4/27.
 */

@XStreamAlias("programs")
public class Programs {

    @XStreamImplicit(itemFieldName = "program")
    List<Program> list;

    public List<Program> getList() {
        return list;
    }

    public void setList(List<Program> list) {
        this.list = list;
    }
}
