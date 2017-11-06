package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-6.
 */

public class GetClassCoursesRequest extends FaceShowBaseRequest {
    public String method = "app.manage.clazs.courses";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
