package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:27.
 * Function :
 */
public class PersonalDetailsSignResponse extends FaceShowBaseResponse {

    public PersonalDetailsSignData data;

    public class PersonalDetailsSignData{
        public double signinPercent;
        public int totalSigninNum;
        public int userSigninNum;
    }

}
