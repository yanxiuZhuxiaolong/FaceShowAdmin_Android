package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by frc on 16-6-20.
 */
public class StringUtils {
    final static int BUFFER_SIZE = 4096;
    public final static String GB2312 = "gb2312";
    public final static String UTF_8 = "UTF-8";

    /**
     * 获取当前版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();// 得到包管理对象
            PackageInfo info = manager
                    .getPackageInfo(context.getPackageName(), 0);// 获取指定包的信息
            return info.versionName;// 获取版本
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unkonwn";
        }
    }

    /**
     * 把InputStream对象转换成String
     *
     * @param is
     * @return
     */
    public static String converStreamToString(InputStream is) {

        if (is == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();

        int count = 0;
        while (count < 3) {

            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                break;
            } catch (IOException e) {
                count++;
            }
        }

        return buffer.toString();
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     */
    public static String converStreamToString(InputStream in, String encoding) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            data = null;
            return new String(outStream.toByteArray(), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String timeString(int time) {
        time = time / 1000;
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String intTimeString(int time) {
        int miao = time % 60;
        int fen = time / 60;
        int hour = 0;
        if (fen >= 60) {
            hour = fen / 60;
            fen = fen % 60;
        }
        String timeString = "";
        String miaoString = "";
        String fenString = "";
        String hourString = "";
        if (miao < 10) {
            miaoString = "0" + miao;
        } else {
            miaoString = miao + "";
        }
        if (fen < 10) {
            fenString = "0" + fen;
        } else {
            fenString = fen + "";
        }
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }
        if (hour != 0) {
            timeString = hourString + ":" + fenString + ":" + miaoString;
        } else {
            timeString = fenString + ":" + miaoString;
        }
        return timeString;
    }

    public static boolean isEmpty(String str) {
        if (null != str) {
            if (str.length() > 4) {
                return false;
            }
        }
        return null == str || "".equals(str) || "NULL"
                .equals(str.toUpperCase());
    }

    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str);
    }

    public static boolean isEmptyArray(Object[] array, int len) {
        return null == array || array.length < len;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    private static Pattern replaceBlankP = Pattern.compile("\\s*|\t|\r|\n");

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Matcher m = replaceBlankP.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getDateTime(long value) {
        System.out.println(value);
        Date date = new Date(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(format.format(date));

        return format.format(date);
    }

    // Convert Unix timestamp to normal date style
    public static String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date(timestamp));
        return date;
    }

    public static double getM(double b) {
        double m;
        double kb;
        kb = b / 1024.0;
        m = kb / 1024.0;
        return m;
    }

    public static String getImage260_360Url(String originalUrl) {
        int dotIndex = originalUrl.lastIndexOf(".");
        String sizeUrlExe = originalUrl
                .substring(dotIndex, originalUrl.length());
        String sizeUrlHead = originalUrl.substring(0, dotIndex);
        String sizeNewUrl = sizeUrlHead + "_260_360" + sizeUrlExe;
        return sizeNewUrl;
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 课程里，时间需要把时间转化一下。
     * 如：2017年9月25日11:59:55，需要把最后的秒钟部分去掉转化后的结果为：2017年9月25日11:59
     *
     * @return
     */
    public static String getCourseTime(String date) {
        try {
            final String colon = ":";
            if (!TextUtils.isEmpty(date) && date.contains(colon)) {
                String result;
                int totalLength = date.length();
                int firstIndex = date.indexOf(colon);
                int lasetIndex = date.lastIndexOf(colon);
                if (firstIndex != lasetIndex) {
                    //后面的截掉
                    if (firstIndex + 2 < totalLength) {
                        result = date.substring(0, firstIndex + 3);
                    } else if (firstIndex + 1 < totalLength) {
                        result = date.substring(0, firstIndex + 2);
                    } else {
                        return date;
                    }
                } else {
                    return date;
                }
                return result;

            } else {
                return date;
            }
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 评论里，时间需要把时间转化一下。
     * 如：2017年9月25日11:59:55，
     * 1.超过发布时间1天的显示具体日期和时间
     * 2.未超过发布时间1天的，并且大于1小时的显示距离发布时间的小时
     * 3.未超过发布时间1天的，并且距离发布时间小于1小时的显示分钟
     * 4.小于1分钟的，显示刚刚
     *
     * @return
     */
    public static String getDiscussTime(String date) {
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
                result = date;
            }
            return result;

        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 评论里，时间需要把时间转化一下。
     * 如：2017年9月25日11:59:55，
     * 1.超过发布时间1天的显示具体日期和时间
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
                result = date;
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

    /**
     * 获取课程的开始 - 截止时间
     * 如：2017.11.3.10:57:19 - 19:00
     *
     * @param getStartTime
     * @param getEndTime
     * @return
     */
    public static String getCourseTime(String getStartTime, String getEndTime) {
        String time;
        if (TextUtils.isEmpty(getStartTime) || TextUtils.isEmpty(getEndTime)) {
            time = "";
        } else {
            String[] startTimes = getStartTime.split(" ");
            String[] startTime = startTimes[1].split(":");
            String[] endTime = getEndTime.split(" ")[1].split(":");
            time = startTimes[0] + " " + startTime[0] + ":" + startTime[1] + " - " + endTime[0] + ":" + endTime[1];
        }
        return time;
    }
}
