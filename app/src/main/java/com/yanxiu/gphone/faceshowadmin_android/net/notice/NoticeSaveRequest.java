package com.yanxiu.gphone.faceshowadmin_android.net.notice;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/11/2.
 */

public class NoticeSaveRequest extends FaceShowBaseRequest {
    // http://orz.yanxiu.com/pxt/platform/data.api?method=notice.save&clazsId=1&title=xxx&content=xxx&url=xxx
    public String method = "notice.save";
    public String clazsId;// 班级id
    public String title;// 标题
    public String content;// 内容
    public String url; //附件


    @Override
    protected String urlPath() {
        return null;
    }
}
