package com.meiaomei.absadplayerrotation.dbutility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meiaomei.absadplayerrotation.AbsPlbsApplication;
import com.meiaomei.absadplayerrotation.model.Area_SQL;
import com.meiaomei.absadplayerrotation.model.BgImg_SQL;
import com.meiaomei.absadplayerrotation.model.Config_SQL;
import com.meiaomei.absadplayerrotation.model.Content_SQL;
import com.meiaomei.absadplayerrotation.model.ExtraMessage_SQL;
import com.meiaomei.absadplayerrotation.model.Model_SQL;
import com.meiaomei.absadplayerrotation.model.PlayPlan_SQL;
import com.meiaomei.absadplayerrotation.model.Pls_SQL;
import com.meiaomei.absadplayerrotation.model.Res_SQL;
import com.meiaomei.absadplayerrotation.model.Rfile_SQL;
import com.meiaomei.absadplayerrotation.model.SystemSetting_SQL;
import com.meiaomei.absadplayerrotation.model.TaskItem_SQL;

/**
 * Created by meiaomei on 2017/3/2.
 */
public class AbsPlbsDB {

    Context context;
    static DbUtils dbUtils;

    public AbsPlbsDB(Context context) {
        this.context = context;
        dbUtils = DbUtils.create(context, "plbs.sqlite");
        dbUtils.configAllowTransaction(true);
    }


    /**
     * 取得
     */
    public static DbUtils getDbUtils() {
        initDBUtil(AbsPlbsApplication.getAppContext());
        return dbUtils;
    }


    /**
     * 初始化DBUtils
     *
     * @param context 上下文
     */
    public static void initDBUtil(Context context) {
        try {
            //数据库非空校验
            if (dbUtils == null) {
                dbUtils = DbUtils.create(context, "plbs.sqlite");
                dbUtils.configAllowTransaction(true);           //配置事务处理
                return;
            }

            //数据库名称校验   得到dbutils的数据库名称
            String dbName = dbUtils.getDaoConfig().getDbName();
            if (dbName == null || !dbName.equals("plbs.sqlite")) {
                dbUtils.close();
                dbUtils = DbUtils.create(context, "plbs.sqlite");
                dbUtils.configAllowTransaction(true);           //配置事务处理
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initNewTable() {//第一次登录创建新表
        initDBUtil(AbsPlbsApplication.getAppContext());
        try {
            dbUtils.createTableIfNotExist(PlayPlan_SQL.class);
            dbUtils.createTableIfNotExist(SystemSetting_SQL.class);
            dbUtils.createTableIfNotExist(Area_SQL.class);
            dbUtils.createTableIfNotExist(Content_SQL.class);
            dbUtils.createTableIfNotExist(Model_SQL.class);
            dbUtils.createTableIfNotExist(Pls_SQL.class);
            dbUtils.createTableIfNotExist(Res_SQL.class);
            dbUtils.createTableIfNotExist(Rfile_SQL.class);
            dbUtils.createTableIfNotExist(TaskItem_SQL.class);
            dbUtils.createTableIfNotExist(Config_SQL.class);
            dbUtils.createTableIfNotExist(BgImg_SQL.class);
            dbUtils.createTableIfNotExist(ExtraMessage_SQL.class);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     */
    public static boolean dropTable() {
        initDBUtil(AbsPlbsApplication.getAppContext());
        boolean flag = false;
        try {
            dbUtils.dropTable(PlayPlan_SQL.class);
            dbUtils.dropTable(SystemSetting_SQL.class);
            dbUtils.dropTable(Area_SQL.class);
            dbUtils.dropTable(Content_SQL.class);
            dbUtils.dropTable(Model_SQL.class);
            dbUtils.dropTable(Pls_SQL.class);
            dbUtils.dropTable(Res_SQL.class);
            dbUtils.dropTable(Rfile_SQL.class);
            dbUtils.dropTable(TaskItem_SQL.class);
            dbUtils.dropTable(Config_SQL.class);
            dbUtils.dropTable(ExtraMessage_SQL.class);
            dbUtils.dropTable(BgImg_SQL.class);
            flag = true;
        } catch (DbException e) {
            flag = false;
            Log.i("数据库", e.toString());
            e.printStackTrace();
        }
        return flag;
    }

    public static void beginTransaction() {
        initDBUtil(AbsPlbsApplication.getAppContext());
        SQLiteDatabase database = dbUtils.getDatabase();
        database.beginTransaction();
    }

    public static void setTransactionSuccessful() {
        initDBUtil(AbsPlbsApplication.getAppContext());
        SQLiteDatabase database = dbUtils.getDatabase();
        database.setTransactionSuccessful();
    }

    public static void endTransaction() {
        initDBUtil(AbsPlbsApplication.getAppContext());
        SQLiteDatabase database = dbUtils.getDatabase();
        database.endTransaction();
    }
}
