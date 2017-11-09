package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 资源详情请求
 */

public class ResourceDetailRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=resource.view&resId=73
    public String method = "resource.view";
    public String resId;

    @Override
    protected String urlPath() {
        return null;
    }

}
