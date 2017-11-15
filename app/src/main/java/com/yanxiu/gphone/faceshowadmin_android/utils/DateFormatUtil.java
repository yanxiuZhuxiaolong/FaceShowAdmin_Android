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
    public final static String FORMAT_THREE = "yyyy.MM.dd HH:mm:ss";


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

    public static String translationDateFormat(String date) {
        return translationBetweenTwoFormat(date, FORMAT_ONE, FORMAT_THREE);
    }

    /**
     * 回复里，时间需要把时间转化一下。
     * 如：2017年9月25日11:59:55，
     * 1.超过发布时间1天的显示具体日期和时间 2017.9.25 11:59
     * 2.未超过发布时间1天的，并且大于1小时的显示距离发布时间的小时
     * 3.未超过发布时间1天的，并且距离发布时间小于1小时的显示分钟
     * 4.小于1分钟的，显示刚刚
     *
     * @return
     */
    public static String getReplyTime(String date) {
        final long seconds = 1;
        final long minutes = seconds * 60;
        final long hour = minutes * 60;
        final long day = hour * 24;
        try {
            String result;
            long serverTime = dateToStamp(date);
            long nowTime = System.currentTimeMillis();
            long time = (nowTime - serverTime) / 1000;//秒
            if (time < 0) {
                return date;
            }
            if (time <= minutes) {
                result = "刚刚";
            } else if (time < hour) {
                result = time / minutes + "分钟前";
            } else if (time < day) {
                result = time / hour + "小时前";
            } else {
                result = translationBetweenTwoFormat(date, FORMAT_ONE, FORMAT_TWO);
            }
            return result;

        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }
}
