package com.yanxiu.gphone.faceshowadmin_android.net.updata;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;

import com.yanxiu.gphone.faceshowadmin_android.utils.NetWorkUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/7 17:17.
 * Function :
 */
public class InitializeRequest extends RequestBase {
    public String did= Constants.deviceId;
    public String brand=Constants.BRAND;
    public String nettype= NetWorkUtils.getNetWorkType();
    public String osType=Constants.osType;
    public String os=Constants.OS;
    public String debugtoken="";
    public String trace_uid= UserInfo.getInstance().getInfo()!=null?UserInfo.getInstance().getInfo().getUserId()+"":"123";
    public String appVersion=Constants.versionName;
    public String osVer=Constants.versionName;
    public String mode= UrlRepository.getInstance().getMode()!=null?UrlRepository.getInstance().getMode():"test";
    public String operType=Constants.OPERTYPE;
    public String phone="-";
    public String remoteIp="";
    public String productLine=Constants.PRODUCTLINE;
    public String content="";
    public String channel;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getInitializeServer();
    }

    @Override
    protected String urlPath() {
        return "/app/log/uploadDeviceLog/release.do";
    }
}
