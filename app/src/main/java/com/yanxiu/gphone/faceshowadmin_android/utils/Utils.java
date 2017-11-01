package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/22.
 */
public class Utils {

    /**
     * 得到屏幕宽度
     */
    public static int getScreenWidth() {
        return ((WindowManager) FSAApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight() {
        return ((WindowManager) FSAApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String FileMD5Helper(String str) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(str);
            byte[] buffer = new byte[1024 * 512];
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            int numRead = -1;
            while ((numRead = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, numRead);
            }
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new RuntimeException("no device Id");
    }

    public static String getTimeFromSum(String sum) {
        int number = 0;
        String time = "";
        try {
            number = Integer.parseInt(sum);

            int seconds = number % 60;
            int minutes = number / 60;

            int minutes2 = minutes % 60;
            int hours = minutes / 60;

            time = getStandTimeFromSum(hours) + ":" + getStandTimeFromSum(minutes2) + ":" + getStandTimeFromSum(seconds);
        } catch (Exception e) {
            if (sum.length() > 6) {
                time = sum.substring(0, 6) + "...";
            } else {
                time = sum;
            }
            e.printStackTrace();
        }
        return time;
    }

    private static String getStandTimeFromSum(int sum) {
        String time = "";
        if (sum < 10) {
            time = "0" + sum;
        } else {
            time = "" + sum;
        }
        return time;
    }
   private static Pattern mobileNoP = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2-9]))\\d{8}$");
    /**
     * 验证注册手机号码是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles == null) {
            return false;
        }

        Matcher m = mobileNoP.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证注册手机号码是否正确
     */
    public static boolean isMobileNO(CharSequence mobiles) {
        if (mobiles == null) {
            return false;
        }
        Matcher m = mobileNoP.matcher(mobiles);
        return m.matches();
    }

    /**
     * 根据不同的id类型，获取不同的组合文字结果
     *
     * @param id   id
     * @param str  已完成
     * @param flag true:已完成个数   false：总个数
     * @return
     */
    public static String getTextById(String id, String str, boolean flag) {
        String result = "";
        switch (id) {
            case "201":
            case "301":
            case "215":
            case "315":
            case "217":
            case "0":
                result = flag ? "已观看" + str + "分钟/" : str + "分钟";
                break;
            case "202":
            case "302":
                result = flag ? "已参加" + str + "个/" : str + "个";
                break;
            case "206":
            case "306":
                result = flag ? "已获得" + str + "分/" : str + "分";
                break;
            case "210":
            case "310":
                result = flag ? "已上传" + str + "个/" : str + "个";
                break;
            case "220":
            case "320":
                result = flag ? "已被点评" + str + "篇/" : str + "篇";
                break;
            case "211":
            case "311":
                result = flag ? "已提问" + str + "个/" : str + "个";
                break;
            default:
                result = flag ? "已完成" + str + "个/" : str + "个";
                break;
        }
        return result;
    }


    public static int convertDpToPx(Context context, int dp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    public static String getSize(String filesize) {

        String file_size = "";

        double size = Double.parseDouble(filesize);
        double K = size / 1000;
        double M = K / 1000;

        if (M > 0.1) {
            file_size = String.format("%.2f", M) + "M";
        } else if (M < 0.1 && K > 0.1) {
            file_size = String.format("%.2f", K) + "k";
        } else if (K < 0.1) {
            file_size = size + "b";
        }
        return file_size;
    }

    /**
     * 时间戳转换成日期
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 日期转换成时间戳
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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

    public static String getData(String data) {
        if (data == null || data.length() <= 0) {
            return "-";
        } else {
            return data.replace(" ", "_");
        }
    }

    /**
     * 得到客户端版本名
     */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 获取网络连接状态
    public static boolean isConnectWifi(Context context) {
        boolean isConnect = false;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null) {
            isConnect = mWifi.isConnected();
        }
        return isConnect;
    }

    public static boolean checktrafficConnection(Context context) {//判断是否的流量连接
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        final NetworkInfo mMobileNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (mMobileNetworkInfo != null) {
//            return mMobileNetworkInfo.isAvailable();    //getState()方法是查询是否连接了数据网络
            return mMobileNetworkInfo.isConnected();
        }
        return false;
    }

    // 获取网络连接状态
    public static int isConnect(Context context) {
        int n = 0;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();//获取网络的信息

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                n = 1;

            }

        }
        return n;
    }


    /**
     * 得到客户端版本名
     */
    public static String getClientVersionName(Context context) {
        try {
            PackageInfo packInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String MD5Helper(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("no device Id");
    }

    /**
     * 获取imei号
     *
     * @return
     */
    public static String getIMEI() {
        try {
            String deviceId = ((TelephonyManager) FSAApplication.getInstance().getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE)).getDeviceId();
            if (null == deviceId || deviceId.length() <= 0) {
                return "";
            } else {
                return deviceId.replace(" ", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getMacAddress() {
        String macAddress = null;
        WifiInfo wifiInfo = ((WifiManager) FSAApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .getConnectionInfo();
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
            if (macAddress == null || macAddress.length() <= 0) {
                return "";
            } else {
                return macAddress;
            }
        } else {
            return "";
        }
    }

    /**
     * 判断某个点是否在某个view上
     *
     * @param x
     * @param y
     * @param view
     * @return
     */
    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        return ((x > viewX && x < (viewX + view.getWidth())) && y > viewY && y < (viewY + view.getHeight()));
    }

    /**
     * 设置字体
     *
     * @param context
     * @param textView
     * @param fontPath
     */
    public static void setTextFont(Context context, TextView textView, String fontPath) {
        if (textView == null) {
            return;
        }
        AssetManager assetManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, fontPath);
        textView.setTypeface(typeface);

    }


    /**
     * 转换时间
     */
    public static String exchangeTime(long time) {
        String fen;
        String miao;
        fen = time / 60 + "";
        miao = time % 60 + "";
        if (time / 60 < 10) {
            if (time % 60 >= 10) {
                return "0" + fen + ":" + miao;
            } else {
                return "0" + fen + ":" + "0" + miao;
            }
        } else {
            if (time % 60 >= 10) {
                return fen + ":" + miao;
            } else {
                return fen + ":" + "0" + miao;
            }
        }
    }

    public static String exchangeRecordTime(long time) {
        String fen;
        String miao;
        fen = time / 60 + "";
        miao = time % 60 + "";
        if (time / 60 < 10) {
            if (time % 60 >= 10) {
                return "0" + fen + "分:" + miao + "秒";
            } else {
                return "0" + fen + "分:" + "0" + miao + "秒";
            }
        } else {
            if (time % 60 >= 10) {
                return fen + "分:" + miao + "秒";
            } else {
                return fen + "分:" + "0" + miao + "秒";
            }
        }
    }

    /**
     * 计算时间有没有超过24小时
     */
    public static boolean isBig24Hours(long lastTime, long curTime) {
        long time = curTime - lastTime;
        if (time > 24 * 60 * 60 * 1000) {
            return true;
        }
        return false;
    }


    public static String getData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String FormatDate(long time) {
        Date curDate = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(curDate);
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


}
