package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-7.
 */

public class GetVotesRequest extends FaceShowBaseRequest {
    private String method = "app.manage.interact.getVote";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
