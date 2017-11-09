package com.yanxiu.gphone.faceshowadmin_android.schedule.bean;


import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

import java.util.ArrayList;


/**
 * 日程数据(无用数据)
 * Created by 戴延枫
 */

public class ScheduleElectmentsBean extends BaseBean {

    private ArrayList<ScheduleBean> elements = new ArrayList<>();

    public ArrayList<ScheduleBean> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ScheduleBean> elements) {
        this.elements = elements;
    }
}
