package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 * 与TopicGetTopics不同，这个请求里不带有members列表信息
 */

// 1.6 获取当前用户的主题列表
public class TopicGetMemberTopicsRequest extends ImRequestBase {
    private String method="topic.getMemberTopics";
}
