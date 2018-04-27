package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 */

// 2.2 发表文字内容
public class SaveTextMsgRequest extends ImRequestBase {
    private String method="topic.saveTextMsg";
    public String topicId;
    public String msg;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
