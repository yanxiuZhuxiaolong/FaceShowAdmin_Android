package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

/**
 * @author frc on 2018/1/17.
 */

public class GetUserMomentsRequest extends RequestBase {

    public String method = "app.moment.getUserMoments";
    public String clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String limit = "10";
    public String userId;
    public String offset;
    public String token = SpManager.getToken();

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
