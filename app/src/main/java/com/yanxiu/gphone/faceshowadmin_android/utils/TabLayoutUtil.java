package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;


/**
 * Created by 戴延枫 on 2017/11/3.
 */

public class TabLayoutUtil {

    /**
     * 设置TabLayout下划线的长度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicatorWidth(final TabLayout tabs, final int leftDip, final int rightDip) {
        if (tabs != null) {
            tabs.post(new Runnable() {
                @Override
                public void run() {
                    Class<?> tabLayout = tabs.getClass();
                    Field tabStrip = null;
                    try {
                        tabStrip = tabLayout.getDeclaredField("mTabStrip");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                    tabStrip.setAccessible(true);
                    LinearLayout llTab = null;
                    try {
                        llTab = (LinearLayout) tabStrip.get(tabs);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
                    int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

                    for (int i = 0; i < llTab.getChildCount(); i++) {
                        View child = llTab.getChildAt(i);
                        child.setPadding(0, 0, 0, 0);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        params.leftMargin = left;
                        params.rightMargin = right;
                        child.setLayoutParams(params);
                        child.invalidate();
                    }
                }
            });
        }
    }
}