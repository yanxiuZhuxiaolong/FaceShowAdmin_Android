package com.test.yanxiu.im_ui.contacts.DatabaseFramework.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbField;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbPrimaryKey;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 1. 完成自动建表的功能
 * 通过反射和注释 实现类映射到sql语句  穿件数据库
 * 2. 增加数据：
 * 通过反射拿去到类字节码的 变量名和具体数值  先存储到hashMap中  然后通过Sql语句拼接成ContentValues
 *
 * @author by frc on 2018/3/7.
 */

public class BaseDao<T> implements IBaseDao<T> {
    //持有数据库操作的引用
    private SQLiteDatabase sqLiteDatabase;
    //表名
    private String tableName;
    //持有操作数据库所对应的java类型
    private Class<T> entityClass;
    //标识：是否做过初始化操作
    private boolean isInit = false;

    //定义一个缓存空间（key value)
    private HashMap<String, Field> cacheMap;


    //框架内部的逻辑，最好不要提供构造方法给调用层用
    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;
        //根据传入的entityClass类型来建立表，只需要建一次
        if (!isInit) {
            //自动建表
            //取到表名
            if (entityClass.getAnnotation(DbTable.class) == null) {
                //反射到类名
                tableName = entityClass.getSimpleName();
            } else {
                //取注解上的名字
                tableName = entityClass.getAnnotation(DbTable.class).value();
            }
            if (!sqLiteDatabase.isOpen()) {//数据库是否打卡
                return false;
            }
            //执行建表操作
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            cacheMap = new HashMap();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    /**
     * 将数据库横向key与Class反射获得的Field建立映射关系
     */
    private void initCacheMap() {
        //1取所有的列名
        String sql = "select * from " + tableName + " limit 1,0";//空表
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        //2取所有成员变量
        Field[] columnFields = entityClass.getDeclaredFields();
        //把所有字段的访问权限打开
        for (Field columnField : columnFields) {
            columnField.setAccessible(true);
        }
        for (String columnName : columnNames) {
            Field columnField = null;
            for (Field field : columnFields) {
                String fieldName;
                if (field.getAnnotation(DbField.class) != null) {
                    fieldName = field.getAnnotation(DbField.class).value();
                } else {
                    fieldName = field.getName();
                }

                if (columnName.equals(fieldName)) {
                    columnField = field;
                }
            }
            if (columnField != null)
                cacheMap.put(columnName, columnField);
        }


        //对1和进行映射
    }

    private String getCreateTableSql() {
        //create table if no exits tb_user(_id integer,name varchar(20),password varchar(20));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");
        //反射得到所有的成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            //拿到数据的类型（通过注解或者反射）
            Class type = field.getType();
            if (field.getAnnotation(DbField.class) != null) {
                if (type == String.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " TEXT,");
                } else if (type == Integer.class) {
                    if (field.getAnnotation(DbPrimaryKey.class) != null) {
                        stringBuffer.append(field.getAnnotation(DbField.class).value() + " INTEGER PRIMARY KEY,");
                    } else {
                        stringBuffer.append(field.getAnnotation(DbField.class).value() + " INTEGER;");
                    }
                } else if (type == Long.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " BIGINT,");
                } else {
                    //不支持的类型
                    continue;
                }

            } else {
                if (type == String.class) {
                    stringBuffer.append(field.getName() + " TEXT,");
                } else if (type == Integer.class) {
                    if (field.getAnnotation(DbPrimaryKey.class) != null) {
                        stringBuffer.append(field.getName() + " INTEGER PRIMARY KEY,");
                    } else {
                        stringBuffer.append(field.getName() + " INTEGER,");

                    }
                } else if (type == Long.class) {
                    stringBuffer.append(field.getName() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getName() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getName() + " BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }


    @Override
    public long insert(T entity) {

        Map<String, String> map = getValues(entity);
        //把数据转移到ContentValues中
        ContentValues contentValues = getContentValues(map);
        //开始插入数据
        long result = sqLiteDatabase.insert(tableName, null, contentValues);
        return result;
    }


    public void execute(String sql) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public long update(T entity, T where) {
        int result = -1;
        Map values = getValues(entity);
        ContentValues con = getContentValues(values);
        Map whereCause = getValues(where);
        Condition condition = new Condition(whereCause);
        result = sqLiteDatabase.update(tableName, con, condition.whereCasue, condition.whereArgs);
        return result;
    }

    @Override
    public int delete(T where) {
        Map map = getValues(where);
        Condition co = new Condition(map);
        return sqLiteDatabase.delete(tableName, co.whereCasue, co.whereArgs);
    }

    @Override
    public void deleteTable() {
        sqLiteDatabase.execSQL("DROP TABLE " + tableName);
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValues(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + "," + limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.whereCasue
                , condition.whereArgs, null, null, orderBy, limitString);
        //定义个用来解析游标的方法
        List<T> result = getResult(cursor, where);


        return result;
    }

    @Override
    public List<T> fuzzyQuery(T where) {
        Map map = getValues(where);

        FuzzySearchCondition condition = new FuzzySearchCondition(map);
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.whereCasue
                , condition.whereArgs, null, null, null, null);
        //定义个用来解析游标的方法
        List<T> result = getResult(cursor, where);


        return result;
    }


    /**
     * @param cursor
     * @param obj    用来表示类结构的
     * @return
     */
    private List<T> getResult(Cursor cursor, T obj) {
        ArrayList list = new ArrayList();
        Object item = null;
        while (cursor.moveToNext()) {
            try {
                item = obj.getClass().newInstance();
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    //取列名
                    String columnName = (String) entry.getKey();
                    //以列名拿到这个列在游标中的位置
                    Integer columnIndex = cursor.getColumnIndex(columnName);

                    Field field = (Field) entry.getValue();
                    Class type = field.getType();

                    if (columnIndex != -1) {
                        if (type == String.class) {
                            field.set(item, cursor.getString(columnIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(columnIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(columnIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(columnIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(columnIndex));
                        } else {
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return list;
    }


    private class Condition {
        private String whereCasue;
        private String[] whereArgs;

        public Condition(Map<String, String> whereCasue) {
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            //取所有的字段名
            Set keys = whereCasue.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = whereCasue.get(key);
                if (value != null) {
                    stringBuilder.append(" and " + key + "=?");
                    list.add(value);
                }

            }
            this.whereCasue = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }

    private class FuzzySearchCondition {
        private String whereCasue;
        private String[] whereArgs;

        public FuzzySearchCondition(Map<String, String> whereCasue) {
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            //取所有的字段名
            Set keys = whereCasue.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = whereCasue.get(key);
                if (value != null) {
                    if (key.equals("classId")) {
                        stringBuilder.append(" and " + key + "=?");
                        list.add(value);
                    } else {
                        stringBuilder.append(" and " + key + " like ?");
                        list.add("%" + value + "%");
                    }

                }

            }
            this.whereCasue = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }


    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;

    }

    private Map<String, String> getValues(T entity) {
        HashMap<String, String> map = new HashMap<>();
        //返回的是所有成员变量的字段
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                //获取列名
                String key = null;
                if (field.getAnnotation(DbField.class) != null) {
                    key = field.getAnnotation(DbField.class).value();
                } else {
                    key = field.getName();
                }

                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
