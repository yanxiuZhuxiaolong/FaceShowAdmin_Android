package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-10.
 */

public class GetCourseReplyRequest extends FaceShowBaseRequest {
    private String method = "app.interact.getComment";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
