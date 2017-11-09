package com.yanxiu.gphone.faceshowadmin_android.schedule.bean;


import com.yanxiu.gphone.faceshowadmin_android.main.bean.ClazsStatisticViewBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.TodaySignInBean;
import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

import java.util.ArrayList;


/**
 * 日程数据
 * Created by 戴延枫
 */

public class ScheduleBean extends BaseBean {

    private String id;
    private String clazsId;
    private String startTime;
    private String endTime;
    private String subject;
    private String remark;
    private String status;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazsId() {
        return clazsId;
    }

    public void setClazsId(String clazsId) {
        this.clazsId = clazsId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
