package com.yanxiu.gphone.faceshowadmin_android.net.schedule;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 发布日程
 */

public class SchedulePublishRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=schedule.new
    public String method = "schedule.new";
    public String clazsId;
    public String subject;
    public String imageUrl;
    public String startTime;
    public String endTime;
    public String remark;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "getClazs.json";
//    }
}
