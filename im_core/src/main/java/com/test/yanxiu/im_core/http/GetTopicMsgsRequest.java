package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 05/03/2018.
 */

// 2.1 获取主题内容
public class GetTopicMsgsRequest extends ImRequestBase {
    private String method="topic.getTopicMsgs";

    public String topicId;
    public String startId;      // 起始消息id
    public String order;        // 排序：asc、desc 默认-desc
    public String dataNum;      // 每次获取消息数量，默认-20
}
