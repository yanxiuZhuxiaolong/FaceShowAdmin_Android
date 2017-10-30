package com.yanxiu.gphone.faceshowadmin_android.main.bean;


import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

import java.util.ArrayList;


/**
 * clazsStatisticView数据
 * Created by 戴延枫 on 2017/10/30.
 */

public class ClazsStatisticViewBean extends BaseBean {
    private String clazsId;
    private String studensNum;//班级学员数
    private String masterNum;//班主任数
    private String courseNum;//课程数
    private String taskNum;//任务数

    public String getClazsId() {
        return clazsId;
    }

    public void setClazsId(String clazsId) {
        this.clazsId = clazsId;
    }

    public String getStudensNum() {
        return studensNum;
    }

    public void setStudensNum(String studensNum) {
        this.studensNum = studensNum;
    }

    public String getMasterNum() {
        return masterNum;
    }

    public void setMasterNum(String masterNum) {
        this.masterNum = masterNum;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }
}
