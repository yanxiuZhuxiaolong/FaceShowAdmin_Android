package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-10.
 */

public class GetCourseCommentRecordsRequest extends FaceShowBaseRequest {
    private String method = "app.interact.commentRecords";
    public String stepId;
    public String id;
    public String limit;
    public String order;

    @Override
    protected String urlPath() {
        return null;
    }
}
