package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/22 14:11.
 * Function :
 */
public class HeadimgUploadBean {

    public TplData tplData;

    public class TplData{
        public String code;
        public String message;
        public String result;
        public List<Data> data;

        public class Data{

            public String originalUrl;
            public String shortUrl;
            public String uniqueKey;
            public String url;
        }
    }
}
