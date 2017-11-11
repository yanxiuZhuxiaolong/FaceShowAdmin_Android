package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-11.
 */

public class GetReplyRequest extends FaceShowBaseRequest {
    private String method = "interact.getSubjectivityAnswer";
    public String questionId;

    @Override
    protected String urlPath() {
        return null;
    }
}
