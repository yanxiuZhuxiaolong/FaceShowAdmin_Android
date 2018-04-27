package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;


/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 15:36.
 * Function :
 */
public class ClassCircleRequest extends RequestBase {

    public String method="app.moment.getMoments";
    public String clazsId= String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String limit="10";
    public String offset;
    public String token= SpManager.getToken();
//    public String token="ce0d56d0d8a214fb157be3850476ecb5";

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
