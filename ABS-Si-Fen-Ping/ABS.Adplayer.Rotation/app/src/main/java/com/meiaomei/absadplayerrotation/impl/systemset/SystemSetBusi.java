package com.meiaomei.absadplayerrotation.impl.systemset;

import java.util.Map;

/**
 * Created by meiaomei on 2017/3/1.
 */
public interface SystemSetBusi {


    boolean checkSaveWeb(String webIp, String webPort, String heartDuartion, String seePort);
    boolean checkSaveFtp(String ftpIp, String ftpPort, String userName, String userPassword);
    boolean saveSetting(Map<String, String> infoMap);
}
