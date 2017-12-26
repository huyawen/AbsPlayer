package com.meiaomei.absadplayerrotation.impl.systemset.impl;

import android.content.Context;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meiaomei.absadplayerrotation.dbutility.AbsPlbsDB;
import com.meiaomei.absadplayerrotation.impl.systemset.SystemSetBusi;
import com.meiaomei.absadplayerrotation.model.SystemSetting_SQL;
import com.meiaomei.absadplayerrotation.view.ZToast;

import java.util.Map;

/**
 * Created by meiaomei on 2017/3/1.
 */
public class SystemBusiImpl implements SystemSetBusi {


    Context context;
    DbUtils dbUtils;

    public SystemBusiImpl(Context context) {
        this.context = context;
        dbUtils = AbsPlbsDB.getDbUtils();
    }


    /**
     * 检查服务器填入项是否为空
     *
     * @param webIp         服务器ip
     * @param webPort       服务器的端口
     * @param heartDuartion 心跳时长
     * @param seePort       监听端口
     * @return 检查通过为 true  否则为false
     */
    @Override
    public boolean checkSaveWeb(String webIp, String webPort, String heartDuartion, String seePort) {
        if (checkIsEmpty(webIp, "服务器的ip为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(webPort, "服务器的端口为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(heartDuartion, "心跳时长为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(seePort, "mac地址为空，请填写！")) {
            return false;
        }
        return true;
    }


    //检查空的公用方法
    private boolean checkIsEmpty(String content, String hintContent) {
        if (content == null || content.equals("")) {
            ZToast.getInstance(context).show(hintContent, Toast.LENGTH_LONG);
            return true;
        }
        return false;
    }


    /**
     * 检查ftp填入项是否为空
     *
     * @param ftpIp        frp的ip
     * @param ftpPort      frp的的端口
     * @param userName     用户名
     * @param userPassword 用户密码
     * @return 检查通过为 true  否则为false
     */
    @Override
    public boolean checkSaveFtp(String ftpIp, String ftpPort, String userName, String userPassword) {
        if (checkIsEmpty(ftpIp, "Ftp的ip为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(ftpPort, "Ftp端口为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(userName, "用户名为空，请填写！")) {
            return false;
        }
        if (checkIsEmpty(userPassword, "用户密码为空，请填写！")) {
            return false;
        }
        return true;
    }


    //保存系统设置到本地
    @Override
    public boolean saveSetting(Map<String, String> infoMap) {
        boolean flag = true;
        try {
            AbsPlbsDB.beginTransaction();//开启事物
            SystemSetting_SQL systemSetting_sql = dbUtils.findFirst(SystemSetting_SQL.class);

            if (systemSetting_sql == null) {//为空就保存
                SystemSetting_SQL systemSetting = new SystemSetting_SQL();
                systemSetting.setIp(infoMap.get("webIP"));
                systemSetting.setPort(infoMap.get("webPort"));
                systemSetting.setHearts(infoMap.get("heartbeatDuration"));
                systemSetting.setMac(infoMap.get("macAddress"));
                systemSetting.setImgTime(infoMap.get("imgTime"));
                systemSetting.setImgCnt(infoMap.get("imgCnt"));
                dbUtils.save(systemSetting);
            } else {//不为空就更新
                systemSetting_sql.setIp(infoMap.get("webIP"));
                systemSetting_sql.setPort(infoMap.get("webPort"));
                systemSetting_sql.setHearts(infoMap.get("heartbeatDuration"));
                systemSetting_sql.setMac(infoMap.get("macAddress"));
                systemSetting_sql.setImgTime(infoMap.get("imgTime"));
                systemSetting_sql.setImgCnt(infoMap.get("imgCnt"));
                dbUtils.update(systemSetting_sql);
            }

            AbsPlbsDB.setTransactionSuccessful();
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            AbsPlbsDB.endTransaction();
        }
        return flag;
    }
}
