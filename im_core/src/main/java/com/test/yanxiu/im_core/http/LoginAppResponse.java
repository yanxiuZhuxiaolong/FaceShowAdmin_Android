package com.test.yanxiu.im_core.http;

import com.test.yanxiu.im_core.http.common.ImMember;

/**
 * Created by cailei on 02/03/2018.
 */

public class LoginAppResponse extends ImResponseBase {
    public Data data;

    public class Data {
        public String imToken;
        public ImMember imMember;
    }
}
