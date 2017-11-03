package com.yanxiu.gphone.faceshowadmin_android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by frc on 17-11-3.
 */

public class DateFormatUtil {
    public final static String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_TWO = "yyyy.MM.dd HH:mm";

    public static String translationBetweenTwoFormat(String dateStr, String formatStr1, String formatStr2) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(formatStr1, Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(formatStr2, Locale.getDefault());
        try {
            Date date = simpleDateFormat1.parse(dateStr);
            return simpleDateFormat2.format(date);
        } catch (ParseException e) {
            return null;
        }
    }


}
