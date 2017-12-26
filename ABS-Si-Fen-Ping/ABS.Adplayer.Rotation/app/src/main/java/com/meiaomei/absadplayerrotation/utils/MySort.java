package com.meiaomei.absadplayerrotation.utils;

import java.util.Comparator;
import java.util.HashMap;

import javax.xml.transform.dom.DOMLocator;

/**
 * Created by meiaomei on 2017/5/23.
 */
public class MySort implements Comparator <HashMap<String, String>> {

    private  boolean isAsc;//是否为升续
    private  String key; //根据那个key排序
    private  boolean isNum; // 排序value是否为数值型

    public MySort(boolean isAsc, String key, boolean isNum) {
        this.isAsc = isAsc;
        this.key = key;
        this.isNum = isNum;
    }

    @Override
    public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {

        String v1=lhs.get(this.key)==null?"1":lhs.get(this.key);//处理如果没有优先级 默认为1级
        String v2=rhs.get(this.key)==null?"1":rhs.get(this.key);
        if (!isNum){
            return isAsc ? (v1.compareTo(v2)) : (v2.compareTo(v1));
        }else {
            if (Double.parseDouble(v1) > Double.parseDouble(v2)){
                return isAsc ? 1 : -1;
            }else if (Double.parseDouble(v1) < Double.parseDouble(v2)){
                return isAsc ? -1 : 1 ;

            }else {
                return 0;
            }
        }

    }
}
