package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-10-31.
 */

public class GetCheckInNotesRequest extends FaceShowBaseRequest {
    public String method = "app.manage.clazs.signIns";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
