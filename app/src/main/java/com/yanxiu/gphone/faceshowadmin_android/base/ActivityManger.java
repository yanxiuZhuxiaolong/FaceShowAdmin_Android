package com.yanxiu.gphone.faceshowadmin_android.base;

import android.app.Activity;


import com.yanxiu.gphone.faceshowadmin_android.MainActivity;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/6/9.
 */

public class ActivityManger {

    protected static ArrayList<Activity> activityList = new ArrayList<Activity>();

    /**
     * 每个activity在oncreate()里，必须调用该方法
     *
     * @param activity
     */
    public static void addActicity(Activity activity) {
        if (activityList != null) {
            activityList.add(activity);
        }
    }

    /**
     * 每个activity在onDestory()里，必须调用该方法
     *
     * @param activity
     */
    public static void destoryActivity(Activity activity) {
        if (activityList != null) {
            activityList.remove(activity);
        }
    }

    /**
     * 在退出程序时，必须调用该方法
     */
    public static void destoryAll() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
        }
    }


    public static void LogOut(String... isMain) {
        Activity activitys = null;
        if (activityList != null && activityList.size() > 0) {
            for (int i = 0; i < activityList.size(); i++) {
                if ((i == activityList.size() - 1) && isMain == null) {
                    activitys = activityList.get(i);
                } else {
                    activityList.get(i).finish();
                }
            }
            activityList.clear();
        }
        if (activitys != null) {
            activityList.add(activitys);
        }
    }

    /**
     * finish掉除了MainActivity之外的所有Activity
     */
    public static void destoryAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (!(activity instanceof MainActivity)) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public static Activity getTopActivity() {
        if (activityList != null && activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }
}
