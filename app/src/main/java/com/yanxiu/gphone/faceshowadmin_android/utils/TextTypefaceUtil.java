package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;

/**
 * 字体工具了类
 * Created by 戴延枫 on 2017/11/2.
 */

public class TextTypefaceUtil {
    private static final String PATH_TYPEFACE = "fonts/";

    public enum TypefaceType {
        METRO_LIGHT(PATH_TYPEFACE + "METRO-LIGHT.OTF");
        public String path;

        TypefaceType(String path) {
            this.path = path;
        }
    }

    /**
     * 设置字体
     */
    public static void setViewTypeface(TextTypefaceUtil.TypefaceType typefaceType, TextView... view) {
        Typeface tf = Typeface.createFromAsset(FSAApplication.getInstance().getAssets(),
                typefaceType.path);
        for (int i = 0; i < view.length; i++) {
            view[i].setTypeface(tf);
        }
    }
}
