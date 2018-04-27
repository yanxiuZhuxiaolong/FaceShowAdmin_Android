package com.yanxiu.gphone.faceshowadmin_android.net.main;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 红点请求接口
 * @author  frc on 2017/12/20.
 */

public class RedDotRequest extends FaceShowBaseRequest {
    public String method="prompt.getUserPrompts";
    public String clazsId;
    public String bizIds;


    @Override
    protected String urlPath() {
        return null;
    }
}
