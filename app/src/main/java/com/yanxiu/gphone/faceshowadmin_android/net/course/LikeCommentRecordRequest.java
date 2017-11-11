package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * @author frc on 17-11-11.
 */

public class LikeCommentRecordRequest extends FaceShowBaseRequest {
    private String method = "interact.likeCommentRecord";
    public String commentRecordId;

    @Override
    protected String urlPath() {
        return null;
    }
}
