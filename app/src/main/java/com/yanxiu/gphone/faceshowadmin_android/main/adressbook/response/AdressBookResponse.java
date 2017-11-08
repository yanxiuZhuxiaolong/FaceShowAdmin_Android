package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 11:38.
 * Function :
 */
public class AdressBookResponse extends FaceShowBaseResponse {

    public AdressData data;

    public class AdressData{
        public List<AdressBookPeople> masters;
        public AdressStudents students;

        public class AdressStudents{
            public List<AdressBookPeople> elements;
            public int offset;
            public int pageSize;
            public int totalElements;
        }
    }
}
