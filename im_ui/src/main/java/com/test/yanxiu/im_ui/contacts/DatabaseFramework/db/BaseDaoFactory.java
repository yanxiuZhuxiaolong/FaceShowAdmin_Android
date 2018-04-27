package com.test.yanxiu.im_ui.contacts.DatabaseFramework.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by frc on 2018/3/7.
 */

public class BaseDaoFactory {
    private static BaseDaoFactory outInstance;

    public static BaseDaoFactory getOutInstance(String dbPath) {
        outInstance = new BaseDaoFactory(dbPath);
        return outInstance;
    }

    private SQLiteDatabase sqLiteDatabase;
    //定义建数据库的路径
    // 建议大家写到SD卡中  好处是APP被删除了，下次再安装的时候，数据还在

    private String sqliteDatabasePath;

    //设计一个数据库的连接池
    protected Map<String, BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());


    protected BaseDaoFactory(String dbPath) {
        //todo 可以判断是否有sd卡  默认存储路径为：/data/data/(packageName)/databases/dbName
        try {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param daoClass
     * @param entityClass
     * @param <T>         BaseDao
     * @param <M>         User
     * @return
     */
    public synchronized <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao<M> baseDao = null;
        if (map.get(daoClass.getSimpleName()) != null) {
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
            map.put(daoClass.getSimpleName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}
