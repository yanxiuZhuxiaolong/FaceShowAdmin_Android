package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

public class PolicyConfigResponse extends ImResponseBase {
    public Data data;

    public class Data {


        private String mqttServer;

        public String getMqttServer() {
            return mqttServer;
        }

        public void setMqttServer(String mqttServer) {
            this.mqttServer = mqttServer;
        }

    }
}
