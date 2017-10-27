package com.yanxiu.gphone.faceshowadmin_android.net.login;


import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-21.
 */

public class GetUserInfoResponse extends FaceShowBaseResponse {


    private UserInfo.Info data;


    public UserInfo.Info getData() {
        return data;
    }

    public void setData(UserInfo.Info data) {
        this.data = data;
    }


}
