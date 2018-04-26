package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/16.
 */

public class DiscardCommentRequest extends FaceShowBaseRequest {
    private String method="moment.discardComment";
    public String commentId;
    @Override
    protected String urlPath() {
        return null;
    }
}
