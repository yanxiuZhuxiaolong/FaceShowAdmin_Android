package com.yanxiu.gphone.faceshowadmin_android.net.main;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowMockRequest;

/**
 * 课程安排请求
 */

public class MainFragmentRequest extends FaceShowMockRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=app.manage.clazs.getClazs&clazsId=9
    public String method = "app.manage.clazs.getClazs";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String getMockDataPath() {
        return "getClazs.json";
    }
}
