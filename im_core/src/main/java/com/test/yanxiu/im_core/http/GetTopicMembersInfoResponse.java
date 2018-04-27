package com.test.yanxiu.im_core.http;

import com.test.yanxiu.im_core.http.common.ImDataForUpdateMemberInfo;

/**
 * Created by srt on 2018/3/27.
 */

public class GetTopicMembersInfoResponse extends ImResponseBase {



    private ImDataForUpdateMemberInfo data;

    public ImDataForUpdateMemberInfo getData() {
        return data;
    }

    public void setData(ImDataForUpdateMemberInfo data) {
        this.data = data;
    }
}
