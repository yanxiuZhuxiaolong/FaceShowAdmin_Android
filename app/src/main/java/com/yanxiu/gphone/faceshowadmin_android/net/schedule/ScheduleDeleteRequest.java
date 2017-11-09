package com.yanxiu.gphone.faceshowadmin_android.net.schedule;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * 删除日程请求
 */

public class ScheduleDeleteRequest extends FaceShowBaseRequest {
    //    http://orz.yanxiu.com/pxt/platform/data.api?method=schedule.del&scheduleId=1
    public String method = "schedule.del";
    public String scheduleId;

    @Override
    protected String urlPath() {
        return null;
    }
}
