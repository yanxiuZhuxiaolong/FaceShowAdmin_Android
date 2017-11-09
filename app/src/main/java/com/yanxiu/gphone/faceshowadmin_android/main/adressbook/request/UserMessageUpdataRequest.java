package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/8 17:02.
 * Function :
 */
public class UserMessageUpdataRequest extends RequestBase {

    public String method="sysUser.update";
    public String realName;
    public String sex;
//    public String subject;
//    public String stage;
    public String schoolName;
    public String url;
    public String userId=String.valueOf(UserInfo.getInstance().getInfo().getUserId());

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
