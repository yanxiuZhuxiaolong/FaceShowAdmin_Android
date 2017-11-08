package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/8 14:31.
 * Function :
 */
public class AddStudentRequest extends RequestBase {

    public String method="sysUser.create";
    public String clazsId=String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String token= SpManager.getToken();
    public String realName;
    public String schoolName;
    public String sex;
    public String mobilePhone;

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
