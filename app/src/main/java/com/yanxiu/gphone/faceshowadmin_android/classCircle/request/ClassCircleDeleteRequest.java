package com.yanxiu.gphone.faceshowadmin_android.classCircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/6 17:26.
 * Function :
 */
public class ClassCircleDeleteRequest extends RequestBase {

    public String method="moment.deleteMoment";
    public String token= SpManager.getToken();
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
}
