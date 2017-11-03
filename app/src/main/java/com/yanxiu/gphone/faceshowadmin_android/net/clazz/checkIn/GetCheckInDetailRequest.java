package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-3.
 */

public class GetCheckInDetailRequest extends FaceShowBaseRequest {
    public String method = "app.manage.interact.getSignIn";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
