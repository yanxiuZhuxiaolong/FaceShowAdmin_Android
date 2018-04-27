package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

public class PolicyMqttServerResponse extends ImResponseBase {
    public Data data;

    public class Data {
        public String mqttServer;
    }
}
