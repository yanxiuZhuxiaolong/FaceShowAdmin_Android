package com.test.yanxiu.common_base.utils;

/**
 * Created by srt on 2018/4/10.
 */

public class EscapeCharacterUtils {

    /**
     * 用于 处理 字符串转义
     * &quot; 引号“
     * &amp; &符号
     * &lt;  <
     * &gt; >
     * &nbsp; 空格
     */
    public  static String unescape(String response) {
        //先转义& 符号  &quot的& 可能被转义为 &amp;
        if (response.contains("&amp;")) {
            response = response.replace("&amp;", "&");
        }
        if (response.contains("&quot;")) {
            response = response.replace("&quot;", "\\\"");
        }
        if (response.contains("&lt;")) {
            response = response.replace("&lt;", "<");
        }
        if (response.contains("&gt;")) {
            response = response.replace("&gt;", ">");
        }
//        if (response.contains("&nbsp;")) {
//            response = response.replace("&nbsp", "&");
//        }
        return response;
    }


}
