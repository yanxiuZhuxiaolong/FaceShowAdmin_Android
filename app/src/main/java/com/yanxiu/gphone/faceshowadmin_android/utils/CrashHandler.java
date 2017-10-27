package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;


import com.yanxiu.gphone.faceshowadmin_android.base.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Crash捕获机制
 * Created by sunpeng on 2016/12/27.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Constants.SDCARD_ROOT_NAME + "/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;


    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        if (DEBUG) {
            mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
            mContext = context.getApplicationContext();
        }

    }

    /**
     * 当程序中有未捕获的异常，系统将会自动调用#uncaughtException方法
     *
     * @param t 出现未捕获异常的线程
     * @param e 未捕获的异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSDCard(e);
            uploadException(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
        //如果系统有默认的异常处理，就让系统处理，没有的话就自己kill自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * 上传异常日志到服务器
     *
     * @param e
     * @throws IOException
     */
    private void uploadException(Throwable e) throws IOException {

    }

    /**
     * 导出异常到本地文件
     *
     * @param e
     */
    private void dumpExceptionToSDCard(Throwable e) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.e(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);

            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

        pw.print("App Version");
        pw.print(pi.versionName);
        pw.print("'_'");
        pw.println(pi.versionCode);

        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("'_'");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.print(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

    }
}
