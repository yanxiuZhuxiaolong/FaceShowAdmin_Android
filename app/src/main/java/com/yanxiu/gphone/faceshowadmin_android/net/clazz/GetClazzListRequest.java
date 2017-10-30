package com.yanxiu.gphone.faceshowadmin_android.net.clazz;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowMockRequest;

/**
 * Created by frc on 17-10-30.
 */

public class GetClazzListRequest extends FaceShowMockRequest {
    public String method = "app.manage.clazses";

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String getMockDataPath() {
        return "clazses.json";
    }
}
