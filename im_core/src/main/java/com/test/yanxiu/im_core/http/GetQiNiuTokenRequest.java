package com.test.yanxiu.im_core.http;

import com.test.yanxiu.common_base.utils.UrlRepository;


/**
 * @author frc on 2018/1/22.
 */

public class GetQiNiuTokenRequest extends ImRequestBase {
    //http://wiki.yanxiu.com/pages/viewpage.action?pageId=12322622
    private String method = "upload.token";
    public String type;
    public String size;
    public String name;
    public String token;
    public String lastModifiedDate;
    public String shareType;
    //必填
    public String dtype;
    public String from;


    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getQiNiuServer();
    }
}
