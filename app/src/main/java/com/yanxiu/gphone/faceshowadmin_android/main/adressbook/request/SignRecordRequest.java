package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 17:00.
 * Function :
 */
public class SignRecordRequest extends RequestBase {

    public String method="app.manage.clazsuser.signIns";
    public String clazsId=String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String userId;
    public String token= SpManager.getToken();

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
