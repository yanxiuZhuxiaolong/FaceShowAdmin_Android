package com.test.yanxiu.im_ui;

/**
 * Created by cailei on 02/03/2018.
 */

public class Constants {
//    public static final long imId = 9;
//    public static final String imToken = "fb1a05461324976e55786c2c519a8ccc";

    public static String token;

    // start *** im 用户信息，module对接用，因为已经写入所有code中，所以这样替换最为便捷
    public static long imId;
    public static String imToken;
    public static String imAvatar;
    // end *** im 用户信息


    public static final String kShareTopic = "Share Topic";
    public static final String kCreateTopicMemberId = "Create Topic Member Id";
    public static final String kCreateTopicMemberName = "Create Topic Member Name";
    public static final String kFromTopicId = "From Topic Id";

    public static final int IM_REQUEST_CODE_BASE = 800;
    public static final int IM_REQUEST_CODE_MSGLIST = IM_REQUEST_CODE_BASE + 1;
    public static final int IM_REQUEST_CODE_CONTACT = IM_REQUEST_CODE_BASE + 2;


}
