package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/8 14:44.
 * Function :
 */
public class AddStudentResponse extends FaceShowBaseResponse {

    public AddStudentData data;

    public ErrorMessage error;

    public class AddStudentData{
        public int id;
        public String mobilePhone;
        public String realName;
        public String school;
        public String email;
        public int sex;
        public int stage;
        public int subject;
        public int ucnterId;
        public int userId;
        public int userStatus;
    }

    public class ErrorMessage{
        public String code;
        public String message;
        public String title;
    }

}
