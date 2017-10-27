package com.yanxiu.gphone.faceshowadmin_android.net.login;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 通过token获取用户信息
 * Created by frc on 17-9-21.
 */

public class GetUserInfoRequest extends FaceShowBaseRequest {
    public String method = "app.sysUser.userInfo";

    @Override
    protected String urlPath() {
        return null;
    }

}
