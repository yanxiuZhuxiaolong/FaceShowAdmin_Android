package com.test.yanxiu.common_base.utils;

import android.app.Activity;


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


    /**
     * 关闭指定activity 以上的所有activity
     * @param activityName 指定activity的名字
     */
    public static void clearTopFrom(String activityName) {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            if (activityList.get(i).getClass().getSimpleName().equals(activityName)) {
                break;
            }else {
                activityList.get(i).finish();
            }
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
     * 清楚list中最上面的几个Activity
     *
     * @param count
     */
    public static void destroyForwardActivityByCount(int count) {
        if (count > activityList.size()) {
            count = activityList.size();
        }
        for (int i = activityList.size(); activityList.size() - count < i; i--) {
            activityList.get(i - 1).finish();
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
