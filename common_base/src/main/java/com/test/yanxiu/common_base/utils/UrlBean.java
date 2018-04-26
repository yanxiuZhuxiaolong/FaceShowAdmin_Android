package com.test.yanxiu.common_base.utils;

import java.io.Serializable;

/**
 * server接口配置的具体url封装类
 */

public class UrlBean implements Serializable {

    public static final String RELEASE = "release";
    public static final String TEST = "test";
    public static final String DEV = "dev";

    private String server;     //普通接口
    private String loginServer;//登陆接口
    private String uploadServer;
    private String initializeUrl;
    private String imServer;
    private String qiNiuServer;
    private String mode;
    private String chooseClassServer;

    public String getChooseClassServer() {
        return chooseClassServer;
    }

    public void setChooseClassServer(String chooseClassServer) {
        this.chooseClassServer = chooseClassServer;
    }

    public String getImServer() {
        return imServer;
    }

    public void setImServer(String imServer) {
        this.imServer = imServer;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLoginServer() {
        return loginServer;
    }

    public void setLoginServer(String loginServer) {
        this.loginServer = loginServer;
    }

    public String getUploadServer() {
        return uploadServer;
    }

    public void setUploadServer(String uploadServer) {
        this.uploadServer = uploadServer;
    }

    public String getInitializeUrl() {
        return initializeUrl;
    }

    public void setInitializeUrl(String initializeUrl) {
        this.initializeUrl = initializeUrl;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getQiNiuServer() {
        return qiNiuServer;
    }

    public void setQiNiuServer(String qiNiuServer) {
        this.qiNiuServer = qiNiuServer;
    }
}
