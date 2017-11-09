package com.yanxiu.gphone.faceshowadmin_android.net.resource;


import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.SKRequestWithCookie;

/**
 * @author dyf
 *         创建资源请求
 *         上传外链 wiki地址：http://wiki.yanxiu.com/pages/viewpage.action?pageId=7212069
 */

public class CreateResourceRequest extends FaceShowBaseRequest {
    public String filename;
    public String createtime;
    public String reserve;
    public String flow = "6";

    @Override
    protected String urlServer() {
        return "http://api.rms.yanxiu.com/resource/create";
    }

    @Override
    protected String urlPath() {
        return null;
    }


    public static class Reserve {

        public String typeId = "";
        public String title = "";
        public String categoryIds = "";
        public String description = "";
        public String clubids = "";
        public String barId = "";
        public String source = "android";
        public String shareType = "0";
        public String from = "22";
        public String uid = UserInfo.getInstance().getInfo().getUserId() + "";
        public String username = UserInfo.getInstance().getInfo().getRealName();
        public String externalUrl;
        public String source_id = "0";
        public String protocol = "srt";

    }
}
