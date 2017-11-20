package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 资源请求
 */

public class ResourceRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=app.manage.resource.clazsResources&clazzId=1&offset&pageSize=10&keyword=
    public String method="app.manage.resource.clazsResources";
    public String clazsId;
    public String id;
    public String pageSize = "30";
    public String keyword;

    @Override
    protected String urlPath() {
        return null;
    }

}
