package com.yanxiu.gphone.faceshowadmin_android.schedule.bean;


import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;


/**
 * 日程数据（无用数据）
 * Created by 戴延枫
 */

public class ScheduleDataBean extends BaseBean {
    private ScheduleElectmentsBean schedules = new ScheduleElectmentsBean();

    public ScheduleElectmentsBean getSchedules() {
        return schedules;
    }
}
