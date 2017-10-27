package com.yanxiu.gphone.faceshowadmin_android.net.login;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowWithOutTokenBaseRequest;

/**
 * 获取验证码
 * Created by frc on 17-10-23.
 */

public class GetVerificationCodeRequest extends FaceShowWithOutTokenBaseRequest {
    public String method = "sysUser.app.getCode";
    public String mobile;

    @Override
    protected String urlPath() {
        return null;
    }
}
