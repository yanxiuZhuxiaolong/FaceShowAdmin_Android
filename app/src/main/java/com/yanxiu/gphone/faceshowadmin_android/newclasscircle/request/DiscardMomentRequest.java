package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 删除已发布的班级圈
 *
 * @author frc on 2018/1/16.
 */

public class DiscardMomentRequest extends FaceShowBaseRequest {
    private String method = "moment.discardMoment";
    public String momentId;

    @Override
    protected String urlPath() {
        return null;
    }
}
