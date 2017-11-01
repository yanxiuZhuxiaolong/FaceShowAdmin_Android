package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-1.
 */

public class CreateNewCheckInRequest extends FaceShowBaseRequest {
    public String method = "interact.createSignIn";
    public String courseId;
    public String clazsId;
    public String title;
    public String startTime;
    public String endTime;
    public String antiCheat;
    public String qrcodeRefreshRate;
    public String successPrompt;

    @Override
    protected String urlPath() {
        return null;
    }
}
