package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-2.
 */

public class GetClassUserSignInsRequest extends FaceShowBaseRequest {
    public String method = "interact.clazsUserSignIn";
    public String stepId;
    public String status;// 1- signed in   0-no sign in
    public String signInTime;
    public String id;
    public String pageSize="10000";

    @Override
    protected String urlPath() {
        return null;
    }
}
