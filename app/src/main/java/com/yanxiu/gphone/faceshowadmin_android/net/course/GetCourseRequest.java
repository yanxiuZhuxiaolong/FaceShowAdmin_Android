package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-8.
 */

public class GetCourseRequest extends FaceShowBaseRequest {
    private String method = "app.manage.course.getCourse";
    public String courseId;

    @Override
    protected String urlPath() {
        return null;
    }
}
