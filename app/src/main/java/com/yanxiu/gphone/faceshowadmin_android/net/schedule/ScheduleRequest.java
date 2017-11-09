package com.yanxiu.gphone.faceshowadmin_android.net.schedule;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 日程请求
 */

public class ScheduleRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=schedule.list&clazsId=1
    public String method = "schedule.list";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "getClazs.json";
//    }
}
