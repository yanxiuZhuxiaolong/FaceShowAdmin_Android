package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 11:28.
 * Function :
 */
public class AdressBookRequest extends RequestBase {

    public String method="app.manage.sysUser.getClazsMember";
    public String clazsId= String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String token= SpManager.getToken();
    public String offset;
    public String pageSize="10";

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
