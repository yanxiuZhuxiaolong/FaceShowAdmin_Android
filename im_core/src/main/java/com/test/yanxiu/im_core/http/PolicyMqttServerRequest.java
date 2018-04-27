package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

// 1.2 获取mqtt服务器配置
public class PolicyMqttServerRequest extends ImRequestBase {
    private String method="policy.mqtt.server";

    public String type = "tcp";     // 连接方式： tcp ws
}
