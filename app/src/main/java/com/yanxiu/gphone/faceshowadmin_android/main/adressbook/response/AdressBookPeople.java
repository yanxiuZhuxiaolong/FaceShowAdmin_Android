package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import java.io.Serializable;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 12:10.
 * Function :
 */
public class AdressBookPeople implements Serializable {

    /**
     * 0默认 1标题教师  2标题学生
     * */
    public int type;
    public String text;

    public String avatar;
    public int id;
    public String mobilePhone;
    public String realName;
    public String school;
    public int stage;
    public int subject;
    public int sex;
    public int userId;
    public int userStatus;

    public AdressBookPeople(int type,String text) {
        this.type = type;
        this.text=text;
    }
}
