package com.test.yanxiu.im_ui.contacts.model;

import android.support.v4.app.NavUtils;
import android.text.TextUtils;

import com.test.yanxiu.im_core.http.GetContactsResponse;
import com.test.yanxiu.im_ui.Constants;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.db.BaseDaoFactory;
import com.test.yanxiu.im_ui.contacts.db.ClassDao;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.contacts.db.ContactsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通讯录对应的数据处理类
 *
 * @author frc on 2018/3/13.
 */

public class ContactsModel {
    private final String dbPath = "/data/data/com.yanxiu.gphone.faceshow/databases/db_" + Constants.imId + "_db.db";
    private ClassDao classDao;
    private ContactsDao contactsDao;
    private ClassBean mCurrentClass;
    private int mCurrentClassPosition;
    private String mCurrentQueryKey = "";

    private GetContactsResponse mData = null;


    public GetContactsResponse getData() {
        return mData;
    }

    private ArrayList<ClassBean> mClassBeanArrayList = new ArrayList<>();
    private HashMap<Long, ArrayList<ContactsPlayerBean>> mContactsPlayers = new HashMap<>();


    public void setData(GetContactsResponse mData) {
        deleteTable();
        createTheDataWeNeed(mData);
    }

    private void deleteTable() {
        if (classDao == null) {
            classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
        }

        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        classDao.deleteTable();
        classDao = null;


        contactsDao.deleteTable();
        contactsDao = null;


    }

    private void createTheDataWeNeed(GetContactsResponse sData) {

        this.mData = sData;
        if (classDao == null) {
            classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
        }

        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        mClassBeanArrayList.clear();
        mContactsPlayers.clear();
        //存储班级信息
        for (GetContactsResponse.GroupsBean groupsBean : mData.getData().getContacts().getGroups()) {
            ClassBean classBean = new ClassBean(groupsBean.getGroupId(), groupsBean.getGroupName());
            mClassBeanArrayList.add(classBean);
            classDao.insert(classBean);
            ArrayList<ContactsPlayerBean> contactsPlayerBeanArrayList = new ArrayList<>();
            //存储班级下成员信息
            for (GetContactsResponse.ContactsBean contactsBean : groupsBean.getContacts()) {
                ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean(contactsBean.getMemberInfo(), groupsBean.getGroupId(), groupsBean.getGroupName());
                contactsPlayerBeanArrayList.add(contactsPlayerBean);
                contactsDao.insert(contactsPlayerBean);
            }
            mContactsPlayers.put(classBean.getClassId(), contactsPlayerBeanArrayList);
        }

    }

    public List<ContactsPlayerBean> getPlayersDataByClass(ClassBean classData) {
        mCurrentClass = classData;
        if (TextUtils.isEmpty(mCurrentQueryKey)) {
            if (mContactsPlayers != null && mContactsPlayers.size() > 0) {
                return mContactsPlayers.get(classData.getClassId());
            }
            if (contactsDao == null) {
                contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
            }
            ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean();
            contactsPlayerBean.setClassId(classData.getClassId());
            return contactsDao.query(contactsPlayerBean);
        } else {
            return getQueryResult(mCurrentQueryKey);
        }

    }


    public List<ClassBean> getClassListData() {
        if (mClassBeanArrayList != null && mClassBeanArrayList.size() > 0) {
            return mClassBeanArrayList;
        }

        if (classDao == null) {
            classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
        }

        List<ClassBean> classBeanList = classDao.query(new ClassBean());
        if (null == classBeanList || classBeanList.size() < 1) {
            return classDao.query(new ClassBean());
        } else {
            return classBeanList;
        }


    }

    public List<ContactsPlayerBean> getQueryResult(String queryContent) {
        mCurrentQueryKey = queryContent;
        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean();
        contactsPlayerBean.setClassId(mCurrentClass.getClassId());
        contactsPlayerBean.setName(queryContent);
        return contactsDao.fuzzyQuery(contactsPlayerBean);
    }

    public List<ContactsPlayerBean> getCurrentClassPlayerList() {
        if (mCurrentClass == null) {
            return null;
        }
        return getPlayersDataByClass(mCurrentClass);
    }

    public int getCurrentClassPosition() {
        return mCurrentClassPosition;
    }

    public void setCurrentClassPosition(int mCurrentClassPosition) {
        this.mCurrentClassPosition = mCurrentClassPosition;
    }

    public void clearQueryKey() {
        mCurrentQueryKey = "";
    }


}
