package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;

/**
 * @author frc
 *         created at 17-6-2.
 */

public class GetResIdRequest extends SKRequestWithCookie {
    public String status = "upinfo";
    public String md5;
//    public String domain="main.zgjiaoyan.com";
    public String isexist = "0";
    public String filename;
    public String reserve ;

    @Override
    protected String urlServer() {
        return "http://newupload.yanxiu.com";
    }

    @Override
    protected String urlPath() {
        return "/fileUpload";
    }


    public static class Reserve {
        public String shareType = "0";
        public String typeId = "1000";
        public String source = "android";
        public String uid = UserInfo.getInstance().getInfo().getUserId()+"";
        public String from = "22";
        public String title;
        public String username = UserInfo.getInstance().getInfo().getRealName();
        public String description = "";

    }
}
