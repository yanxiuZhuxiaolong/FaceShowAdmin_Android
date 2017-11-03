package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-2.
 */

public class SupplementalSignInRequest extends FaceShowBaseRequest {
    public String method = "interact.replenishSignIn";
    public String stepId;
    public String userId;
    public String signInTime;

    @Override
    protected String urlPath() {
        return null;
    }
}
