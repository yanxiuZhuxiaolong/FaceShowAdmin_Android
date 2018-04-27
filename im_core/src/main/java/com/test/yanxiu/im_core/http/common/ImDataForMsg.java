package com.test.yanxiu.im_core.http.common;

import java.util.List;

/**
 * Created by cailei on 05/03/2018.
 */

public class ImDataForMsg {
    public long imEvent;
    public String reqId;
    // 还有一堆不确定干什么用，暂时没写
    public List<ImMsg> topicMsg;
}
