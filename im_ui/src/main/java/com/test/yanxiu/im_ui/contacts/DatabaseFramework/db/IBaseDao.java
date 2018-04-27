package com.test.yanxiu.im_ui.contacts.DatabaseFramework.db;

import java.util.List;

/**
 * 规范所有的数据库操作
 *
 * @author by frc on 2018/3/7.
 */

public interface IBaseDao<T> {
    long insert(T entity);

    long update(T entity, T where);

    int delete(T where);

    void deleteTable();

    List<T> query(T where);

    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

    List<T> fuzzyQuery(T where);


}
