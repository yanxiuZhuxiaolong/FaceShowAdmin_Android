package com.yanxiu.gphone.faceshowadmin_android;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.tendcloud.tenddata.TCAgent;
import com.test.yanxiu.common_base.utils.EnvConfigBean;
import com.test.yanxiu.common_base.utils.UrlBean;
import com.test.yanxiu.common_base.utils.UrlRepository;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;

import com.yanxiu.gphone.faceshowadmin_android.utils.CrashHandler;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;

import org.litepal.LitePalApplication;

/**
 *
 * Created by frc on 17-10-27.
 */

public class FSAApplication extends LitePalApplication {
    private static FSAApplication instance;

    public static FSAApplication getInstance() {
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }

        TCAgent.LOG_ON=true;
        TCAgent.init(this,"56E7747DFD1A4B9EB9A2DA6E7A2924EA", "");
        TCAgent.setReportUncaughtExceptions(true);

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
