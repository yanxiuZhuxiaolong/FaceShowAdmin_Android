package com.yanxiu.gphone.faceshowadmin_android.net.main;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 首页请求
 */

public class MainRequest extends FaceShowBaseRequest {
    public String method = "app.clazs.getCurrentClazs";

    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "main.json";
//    }
}
