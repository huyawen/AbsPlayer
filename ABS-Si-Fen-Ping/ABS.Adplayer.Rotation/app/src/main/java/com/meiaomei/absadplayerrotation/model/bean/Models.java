package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by huyawen on 2017/4/25.
 */

@XStreamAlias("models")  //别名注解 必须用注解申明
public class Models {

    @XStreamImplicit(itemFieldName = "model")  //节点注解  必须写
    List<Model> mList;

    public List<Model> getmList() {
        return mList;
    }

    public void setmList(List<Model> mList) {
        this.mList = mList;
    }

    //在非静态内部类中不可以声明静态成员
    //静态内部类不能访问其外部类的非静态成员变量和方法
    //静态内部类的对象可以直接生成：Outer.Inner in=new Outer.Inner()；而不需要通过生成外部类对象来生成。
}
