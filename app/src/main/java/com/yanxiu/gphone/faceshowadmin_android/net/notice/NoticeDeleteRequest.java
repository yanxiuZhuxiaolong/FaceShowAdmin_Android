package com.yanxiu.gphone.faceshowadmin_android.net.notice;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/11/2.
 */

public class NoticeDeleteRequest extends FaceShowBaseRequest {
    //   http://orz.yanxiu.com/pxt/platform/data.api?method=notice.delete&noticeId=1
    public String method = "notice.delete";
    public String noticeId;
    public String clazsId;// 班级id

    @Override
    protected String urlPath() {
        return null;
    }
}
