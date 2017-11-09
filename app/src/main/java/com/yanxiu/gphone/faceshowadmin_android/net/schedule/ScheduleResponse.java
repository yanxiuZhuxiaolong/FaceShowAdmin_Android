package com.yanxiu.gphone.faceshowadmin_android.net.schedule;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleBean;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleDataBean;

/**
 * 日程
 */

public class ScheduleResponse extends FaceShowBaseResponse {

    private ScheduleDataBean data = new ScheduleDataBean();

    public ScheduleBean getSchedule() {
        ScheduleBean result;
        try{
            result = data.getSchedules().getElements().get(0);
        }catch (Exception e){
            result = null;
        }
        return result;
    }
}
