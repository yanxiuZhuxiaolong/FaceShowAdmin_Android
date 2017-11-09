package com.yanxiu.gphone.faceshowadmin_android.net.schedule;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 发布日程
 */

public class ScheduleUpdateRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=schedule.update
    public String method = "schedule.update";
    public String scheduleId;
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
