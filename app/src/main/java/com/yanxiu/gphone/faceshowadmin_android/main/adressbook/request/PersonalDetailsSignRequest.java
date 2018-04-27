package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:24.
 * Function :
 */
public class PersonalDetailsSignRequest extends RequestBase {

    public String method="interact.clazsUserSignInPercent";
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
