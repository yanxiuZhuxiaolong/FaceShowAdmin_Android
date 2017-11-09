package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:12.
 * Function :
 */
public class PersonalDetailsResponse extends FaceShowBaseResponse {

    public PersonalDetailsData data;

    public class PersonalDetailsData{
        public String avatar;
        public int id;
        public String mobilePhone;
        public String realName;
        public String school;
        public int sex;
        public int stage;
        public String stageName;
        public int subject;
        public String subjectName;
        public int userId;
        public int userStatus;
    }

}
