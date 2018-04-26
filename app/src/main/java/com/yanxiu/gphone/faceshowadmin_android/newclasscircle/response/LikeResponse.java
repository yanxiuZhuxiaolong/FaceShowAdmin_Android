package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:28.
 * Function :
 */
public class LikeResponse extends FaceShowBaseResponse {

    public Data data;

    public class Data{
        public String id;
        public String clazsId;
        public String momentId;
        public String createTime;
        public Publisher publisher;
    }

}
