package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 */

// 1.1 创建主题
public class TopicCreateTopicRequest extends ImRequestBase {
    private String method="topic.createTopic";

    public String topicType;   // int型，1-私聊 2-群聊
    public String topicName;
    public String yxUsers;
    public String imMemberIds;

    public String fromGroupTopicId;
}
