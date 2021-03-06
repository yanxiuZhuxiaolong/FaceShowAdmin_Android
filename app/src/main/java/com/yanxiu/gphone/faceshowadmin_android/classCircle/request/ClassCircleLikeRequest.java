package com.yanxiu.gphone.faceshowadmin_android.classCircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:36.
 * Function :
 */
public class ClassCircleLikeRequest extends RequestBase {

    public String method="moment.like";
    public String token= SpManager.getToken();
    public String clazsId= String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String momentId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "like.json";
//    }
}
