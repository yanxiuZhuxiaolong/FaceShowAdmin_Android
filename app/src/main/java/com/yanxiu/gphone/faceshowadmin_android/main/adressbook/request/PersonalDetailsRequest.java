package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 15:37.
 * Function :
 */
public class PersonalDetailsRequest extends RequestBase {

    public String method="sysUser.userInfo";
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
