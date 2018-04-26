package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/26.
 */

public class GetMomentDetailRequest extends FaceShowBaseRequest {
    private String method = "moment.getMoment";
    public String momentId;

    @Override
    protected String urlPath() {
        return null;
    }
}
