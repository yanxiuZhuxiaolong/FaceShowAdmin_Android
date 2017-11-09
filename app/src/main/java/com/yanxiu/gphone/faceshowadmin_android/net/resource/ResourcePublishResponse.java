package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleBean;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleDataBean;

/**
 * 发布资源
 */

public class ResourcePublishResponse extends FaceShowBaseResponse {

    private ScheduleDataBean data = new ScheduleDataBean();

    public ScheduleBean getSchedule() {
        return data.getSchedules().getElements().get(0);
    }
}
