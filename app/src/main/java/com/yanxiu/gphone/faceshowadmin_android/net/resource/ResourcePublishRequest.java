package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 发布资源
 */

public class ResourcePublishRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=resource.save&resName=xxx&resType=0&publisherId=1&clazzId=1&suffix=xxx&url=xxxx
    public String method = "resource.save";
    public String clazsId;
    public String resName;
    public String resType = "1";
    public String resId;
    public String suffix ="";
    public String url;

    @Override
    protected String urlPath() {
        return null;
    }

}
