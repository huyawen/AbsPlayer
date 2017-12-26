package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by huyawen on 2017/7/1.
 */

@XStreamAlias("RegistrationItemModel")
public class RegistrationItemModel {

    private String Custerm;
    private String CpuID;
    private String AssemblyTitle;
    private String AssemblyVersion;

    public String getCusterm() {
        return Custerm;
    }

    public void setCusterm(String custerm) {
        Custerm = custerm;
    }

    public String getCpuID() {
        return CpuID;
    }

    public void setCpuID(String cpuID) {
        CpuID = cpuID;
    }

    public String getAssemblyTitle() {
        return AssemblyTitle;
    }

    public void setAssemblyTitle(String assemblyTitle) {
        AssemblyTitle = assemblyTitle;
    }

    public String getAssemblyVersion() {
        return AssemblyVersion;
    }

    public void setAssemblyVersion(String assemblyVersion) {
        AssemblyVersion = assemblyVersion;
    }
}
