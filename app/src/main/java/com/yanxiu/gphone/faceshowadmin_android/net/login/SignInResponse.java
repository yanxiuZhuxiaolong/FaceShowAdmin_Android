package com.yanxiu.gphone.faceshowadmin_android.net.login;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * 登录返回的数据
 * Created by frc on 17-9-14.
 */

public class SignInResponse extends FaceShowBaseResponse {


    private String data;
    private String token;
    private String passport;

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
