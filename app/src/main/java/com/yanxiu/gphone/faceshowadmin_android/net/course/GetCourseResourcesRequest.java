package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-9.
 */

public class GetCourseResourcesRequest extends FaceShowBaseRequest {
    private String method = "app.manage.resource.courseResources";
    public String courseId;

    @Override
    protected String urlPath() {
        return null;
    }
}
