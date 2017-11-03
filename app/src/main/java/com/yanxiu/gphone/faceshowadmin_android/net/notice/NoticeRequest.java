package com.yanxiu.gphone.faceshowadmin_android.net.notice;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=app.manage.getNotices
    public String method = "app.manage.getNotices";
    public String clazsId;
    public String id;//
    public String pageSize;// 每页多少条，默认10


    @Override
    protected String urlPath() {
        return null;
    }
}
