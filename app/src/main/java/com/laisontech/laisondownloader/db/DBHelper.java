package com.laisontech.laisondownloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SDP on 2017/5/5.
 * 记录：所以在有新的版本时，则直接对数据进行移植即可，表单的增加，需要数据库版本进行更改，但是如果对某个表单增加了列，或者删除了列，则需要进行数据库升级。
 * 2017 12 21日：版本13 抄表的表单，定位的表单等
 * <p>
 * 2017 12 25日：版本14，增加了手动录入的数据库，以及判断和升级
 * <p>
 * 需要增加判断，增加新的表单时，对当前的的数据库版本进行保存，然后在数据库升级时对之前的版本进行判断
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    //数据库名称 从网络端下载的表计信息
    private static final String DB_NAME = "lasionappdownload.db";
    //版本号，从10开始，每次数据库增加列则加1即可
    private static final int DB_VERSION = 1;
    private Map<String, Dao> maps = new HashMap<>();
    //使用单利进行数据的访问
    private static DBHelper instance;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);

                }
            }
        }
        return instance;
    }

    //获取
    @SuppressWarnings (value={"unchecked"})
    public synchronized Dao getDao(Class clz) throws SQLException {
        Dao dao = null;
        String clzSimpleName = clz.getSimpleName();
        if (maps.containsKey(clzSimpleName)) {
            dao = maps.get(clzSimpleName);
        } else {
            dao = (Dao) super.getDao(clz);
            maps.put(clzSimpleName, dao);
        }
        return dao;
    }

    //onCreate只会创建一回
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            //第一个是链接资源，第二个参数就是需要创建的表格Bean类
            TableUtils.createTable(connectionSource, LastSearchInfo.class);
            //这个方法是对之前如果未安装此app的时候，则直接进行创建
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.e(DBHelper.class.getName(), "DatabaseHelper onUpgrade() oldVersion:" + oldVersion + ",newVersion:" + newVersion);
        try {
            //旧的版本小于当前的版本，则将旧版本的数据迁移到新的版本里面,有字段增加的将false改为true即可。
            if (oldVersion < DB_VERSION) {
                DatabaseUtil.upgradeTable(sqLiteDatabase, connectionSource, LastSearchInfo.class, DatabaseUtil.OperationType.ADD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //关闭所有操作
    public void closeDB() {
        super.close();
        for (String key : maps.keySet()) {
            Dao dao = maps.get(key);
            dao = null;
        }
    }
}
