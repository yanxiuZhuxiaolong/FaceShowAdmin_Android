package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-8.
 */

public class GetClazsUserQuestionRequest extends FaceShowBaseRequest {
    private String method = "interact.clazsUserQuestion";
    public String stepId;
    public String status;
    public String id;

    @Override
    protected String urlPath() {
        return null;
    }
}
