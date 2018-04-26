package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleNewMessageRequest extends FaceShowBaseRequest {
    private String method="app.moment.getUserMomentMsg";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
