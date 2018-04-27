package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 18:53.
 * Function :
 */
public class SendClassCircleRequest extends RequestBase {

    public String method = "moment.publishMoment";
    public String token = SpManager.getToken();
    //    public String clazsId="7";
    public String clazsId= String.valueOf(SpManager.getCurrentClassInfo().getId());
    public String content;
    public String resourceIds = "";
    /**
     * 通过七牛上传,写死
     */
    public String resourceSource = "qiniu";

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "sendclasscircle.json";
//    }
}
