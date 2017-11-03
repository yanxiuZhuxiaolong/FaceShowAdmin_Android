package com.yanxiu.gphone.faceshowadmin_android.net.notice;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=app.manage.getNotices
    public String method = "app.manage.getNotices";
    public String clazsId;
    public int offset;// 第几条开始，默认0
    public int pageSize;// 每页多少条，默认10
    public String keyword;// 搜索关键字


    @Override
    protected String urlPath() {
        return null;
    }
}
