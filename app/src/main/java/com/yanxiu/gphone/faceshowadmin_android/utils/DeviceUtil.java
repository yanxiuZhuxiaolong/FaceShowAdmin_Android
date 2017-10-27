package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class DeviceUtil {
    public static String getAppDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) FSAApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        WifiManager wifiManager = (WifiManager) FSAApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            deviceId = deviceId + macAddress;
        }

        if (deviceId == null || "".equals(deviceId.trim())) {
            deviceId = "-";
        }

        return SysEncryptUtil.getMD5(deviceId);
    }

    /**
     * 获取厂商品牌
     */
    public static String getBrandName() {
        String brand = Build.BRAND;
        if (brand == null || brand.length() <= 0) {
            return "";
        } else {
            return getData(brand);
        }
    }

    /**
     * 获取modelname
     *
     * @return
     */
    public static String getModelName() {
        String model = Build.MODEL;
        if (model == null || model.length() <= 0) {
            return "";
        } else {
            return getData(model);
        }
    }

    private static String getData(String data) {
        if (data == null || data.length() <= 0) {
            return "-";
        } else {
            return data.replace(" ", "_");
        }
    }


    public static String getOSVersionCode() {
        int version = -1;
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(version);
    }
}
