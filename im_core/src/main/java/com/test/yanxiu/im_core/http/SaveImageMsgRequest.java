package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 */

// 2.3发表图片内容
public class SaveImageMsgRequest extends ImRequestBase {
    private String method = "topic.saveImageMsg";
    public String topicId;
    public String rid;
    public String width;
    public String height;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
