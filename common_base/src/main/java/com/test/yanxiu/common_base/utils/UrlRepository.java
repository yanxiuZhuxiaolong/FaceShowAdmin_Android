package com.test.yanxiu.common_base.utils;


/**
 * 存储url
 * application里去获取到的server信息，存储到这里，供全局使用
 */

public class UrlRepository {

    private UrlBean mUrlBean;

    private static UrlRepository INSTANCE = null;


    public static UrlRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UrlRepository();
        }
        return INSTANCE;
    }

    public String getServer() {
        if (mUrlBean != null) {
            return mUrlBean.getServer();
        } else {
            return null;
        }
    }

    public String getChooseClassServer() {
        if (mUrlBean != null) {
            return mUrlBean.getChooseClassServer();
        } else {
            return null;
        }
    }

    public String getLoginServer() {
        if (mUrlBean != null) {
            return mUrlBean.getLoginServer();
        } else {
            return null;
        }
    }

    public String getUploadServer() {
        if (mUrlBean != null) {
            return mUrlBean.getUploadServer();
        } else {
            return null;
        }
    }

    public String getInitializeServer() {
        if (mUrlBean != null) {
            return mUrlBean.getInitializeUrl();
        } else {
            return null;
        }
    }
    public String getImServer(){
        if (mUrlBean != null) {
            return mUrlBean.getImServer();
        } else {
            return null;
        }
    }

    public String getMode() {
        if (mUrlBean != null) {
            return mUrlBean.getMode();
        } else {
            return null;
        }
    }

    public String getQiNiuServer() {
        if (mUrlBean != null) {
            return mUrlBean.getQiNiuServer();
        } else {
            return null;
        }
    }

    public void setUrlBean(UrlBean urlBean) {
        this.mUrlBean = urlBean;
    }
}
