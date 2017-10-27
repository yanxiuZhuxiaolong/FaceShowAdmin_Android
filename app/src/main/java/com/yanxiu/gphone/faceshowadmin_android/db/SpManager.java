package com.yanxiu.gphone.faceshowadmin_android.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;

/**
 * sharePreference管理类
 * 所有sp存储，都应该写在该类里
 */
public class SpManager {

    public static final String SP_NAME = "face_show_admin_sp";
    private static SharedPreferences mySharedPreferences = FSAApplication.getInstance().getApplicationContext()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);


    private static final String USER_INFO = "userInfo";
    /**
     * 第一次启动
     */
    private static final String FIRST_START_UP = "first_start_up";
    /**
     * 版本号
     */
    private static final String APP_VERSION_CODE = "version_code";
    /*用户是否已经登录成功*/
    private static final String IS_LOGIN = "is_login";
    /*用户唯一标示token*/
    private static final String TOKEN = "token";
    private static final String PASSPORT = "pass_port";


    public static void setFirstStartUp(boolean isFirstStartUp) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(FIRST_START_UP, isFirstStartUp);
        editor.commit();
    }

    /**
     * 是否第一次启动
     *
     * @return true ： 第一次
     */
    public static boolean isFirstStartUp() {
        return mySharedPreferences.getBoolean(FIRST_START_UP, true);
    }

    /**
     * app版本号
     *
     * @return -1 ：没记录
     */
    public static void setAppVersionCode(int versionCode) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(APP_VERSION_CODE, versionCode);
        editor.commit();
    }

    /**
     * app版本号
     *
     * @return -1 ：没记录
     */
    public static int getAppVersionCode() {
        return mySharedPreferences.getInt(APP_VERSION_CODE, -1);
    }

    /**
     * 是否已经登录
     *
     * @return false:未登录   true :登录
     */
    public static boolean isLogined() {
        return mySharedPreferences.getBoolean(IS_LOGIN, false);
    }


    /**
     * 设置为登录状态
     */
    public static void haveSignIn() {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    /**
     * 设置为登出
     */
    public static void loginOut() {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }

    /**
     * 保存token
     *
     * @param token 唯一标示
     */
    public static void saveToken(String token) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    /**
     * 获取token
     *
     * @return token
     */
    public static String getToken() {
        return mySharedPreferences.getString(TOKEN, "");
    }


    public static void savePassPort(String passPort) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(PASSPORT, passPort);
        editor.commit();
    }

    /**
     * 获取passPort  用于上传资源
     *
     * @return passPort
     */
    public static String getPassport() {
        return mySharedPreferences.getString(PASSPORT, "");
    }


    public static void saveUserInfo(String userInfoStr) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_INFO, userInfoStr);
        editor.apply();
    }

    public static void saveUserInfo(UserInfo.Info userInfo) {
        String userInfoStr = RequestBase.getGson().toJson(userInfo);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_INFO, userInfoStr);
        editor.apply();
        UserInfo.update(userInfo);
    }


    public static UserInfo.Info getUserInfo() {
        String userInfoStr = mySharedPreferences.getString(USER_INFO, "");
        return RequestBase.getGson().fromJson(userInfoStr, UserInfo.Info.class);
    }
}
