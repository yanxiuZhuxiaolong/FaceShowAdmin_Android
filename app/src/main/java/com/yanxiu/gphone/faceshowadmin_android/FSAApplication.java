package com.yanxiu.gphone.faceshowadmin_android;

import android.app.Application;
import android.content.Context;

import com.yanxiu.gphone.faceshowadmin_android.utils.CrashHandler;

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
    }


}
