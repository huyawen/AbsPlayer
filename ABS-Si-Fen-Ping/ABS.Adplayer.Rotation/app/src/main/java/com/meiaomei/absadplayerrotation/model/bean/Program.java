package com.meiaomei.absadplayerrotation.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by huyawen on 2017/4/26.
 */

@XStreamAlias("program")
public class Program {

    @XStreamImplicit(itemFieldName = "pls")
    List<Pls> plsList;

    @XStreamAlias("defaultpls")
    DefaultPls defaultPls;


    public List<Pls> getPlsList() {
        return plsList;
    }

    public void setPlsList(List<Pls> plsList) {
        this.plsList = plsList;
    }

    public DefaultPls getDefaultPls() {
        return defaultPls;
    }

    public void setDefaultPls(DefaultPls defaultPls) {
        this.defaultPls = defaultPls;
    }

    public static class DefaultPls{
        @XStreamAsAttribute()
        @XStreamAlias("areatype")
        String areatype;

        @XStreamImplicit(itemFieldName = "res")
        List<Res> resList;

        public String getAreatype() {
            return areatype;
        }

        public void setAreatype(String areatype) {
            this.areatype = areatype;
        }

        public List<Res> getResList() {
            return resList;
        }

        public void setResList(List<Res> resList) {
            this.resList = resList;
        }
    }

    public static class Pls{
        @XStreamAsAttribute()
        @XStreamAlias("areatype")
        String areatype;

        @XStreamAsAttribute()
        @XStreamAlias("stdtime")
        String stdtime;

        @XStreamAsAttribute()
        @XStreamAlias("edtime")
        String edtime;

        @XStreamAlias("resarr")
        ResArr resArr;

        public String getAreatype() {
            return areatype;
        }

        public void setAreatype(String areatype) {
            this.areatype = areatype;
        }

        public String getStdtime() {
            return stdtime;
        }

        public void setStdtime(String stdtime) {
            this.stdtime = stdtime;
        }

        public String getEdtime() {
            return edtime;
        }

        public void setEdtime(String edtime) {
            this.edtime = edtime;
        }

        public ResArr getResArr() {
            return resArr;
        }

        public void setResArr(ResArr resArr) {
            this.resArr = resArr;
        }

        public static class ResArr{

            @XStreamImplicit(itemFieldName = "res")
            List<Res> resList;

            public List<Res> getResList() {
                return resList;
            }

            public void setResList(List<Res> resList) {
                this.resList = resList;
            }
        }
    }

}
