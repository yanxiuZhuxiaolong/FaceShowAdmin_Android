package com.yanxiu.gphone.faceshowadmin_android;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.EnvConfigBean;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlBean;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshowadmin_android.utils.CrashHandler;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;

/**
 * Created by frc on 17-10-27.
 */

public class FSAApplication extends Application {
    private static FSAApplication instance;

    public static FSAApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashHandler.getInstance().init(this);
        initUrlServer();
    }

    private void initUrlServer() {
        UrlBean urlBean;
        Gson gson = new Gson();
        String urlJson = FileUtils.getFromAssets(this, Constants.URL_SERVER_FILE_NAME);
        if (urlJson.contains(Constants.MULTICONFIG)) {
            EnvConfigBean envConfigBean = gson.fromJson(urlJson, EnvConfigBean.class);
            urlBean = envConfigBean.getData().get(envConfigBean.getCurrentIndex());
        } else {
            urlBean = gson.fromJson(urlJson, UrlBean.class);
        }
        UrlRepository.getInstance().setUrlBean(urlBean);

    }


}
