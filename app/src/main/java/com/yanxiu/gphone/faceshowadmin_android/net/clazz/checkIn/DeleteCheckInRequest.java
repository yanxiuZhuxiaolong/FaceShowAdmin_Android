package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-16.
 */

public class DeleteCheckInRequest extends FaceShowBaseRequest {
    private String method = "interact.deleteStep";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
