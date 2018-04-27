package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 */

// 1.5 获取多个主题
public class TopicGetTopicsRequest extends ImRequestBase {
    private String method="topic.getTopics";

    public String topicIds;   // 主题id，多个逗号分隔，每次最多10个
}
