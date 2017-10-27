package com.yanxiu.gphone.faceshowadmin_android.net.login;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowWithOutTokenBaseRequest;

/**
 * 修改用户密码
 * Created by frc on 17-10-23.
 */

public class ModifyPasswordRequest extends FaceShowWithOutTokenBaseRequest {
    public String method = "sysUser.app.initPassword";
    public String mobile;
    public String code;
    public String password;

    @Override
    protected String urlPath() {
        return null;
    }
}
