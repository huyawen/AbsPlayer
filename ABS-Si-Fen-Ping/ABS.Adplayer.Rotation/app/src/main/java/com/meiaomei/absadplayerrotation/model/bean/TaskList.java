package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by meiaomei on 2017/4/18.
 */
public class TaskList {

    int taskcount;

    @XStreamImplicit(itemFieldName = "taskitem")
    List<TaskItem> taskitemList;

    public int getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(int taskcount) {
        this.taskcount = taskcount;
    }

    public List<TaskItem> getTaskitemList() {
        return taskitemList;
    }

    public void setTaskitemList(List<TaskItem> taskitemList) {
        this.taskitemList = taskitemList;
    }

}
