package com.yanxiu.gphone.faceshowadmin_android.main.bean;


import com.yanxiu.gphone.faceshowadmin_android.model.*;

import java.util.ArrayList;


/**
 * 课程安排
 * Created by 戴延枫 on 2017/10/30.
 */

public class CourseArrangeBean extends BaseBean {
    private ArrayList<CourseBean> todayCourses = new ArrayList<>();
    private ProjectInfoBean projectInfo = new ProjectInfoBean();
    private ArrayList<TodaySignInBean> todaySignIns = new ArrayList<>();
    private ClazsInfoBean clazsInfo = new ClazsInfoBean();
    private ClazsStatisticViewBean clazsStatisticView;

    public ArrayList<CourseBean> getTodayCourses() {
        return todayCourses;
    }

    public void setTodayCourses(ArrayList<CourseBean> todayCourses) {
        this.todayCourses = todayCourses;
    }

    public ProjectInfoBean getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(ProjectInfoBean projectInfo) {
        this.projectInfo = projectInfo;
    }

    public ArrayList<TodaySignInBean> getTodaySignIns() {
        return todaySignIns;
    }

    public void setTodaySignIns(ArrayList<TodaySignInBean> todaySignIns) {
        this.todaySignIns = todaySignIns;
    }

    public ClazsInfoBean getClazsInfo() {
        return clazsInfo;
    }

    public void setClazsInfo(ClazsInfoBean clazsInfo) {
        this.clazsInfo = clazsInfo;
    }

    public ClazsStatisticViewBean getClazsStatisticView() {
        return clazsStatisticView;
    }

    public void setClazsStatisticView(ClazsStatisticViewBean clazsStatisticView) {
        this.clazsStatisticView = clazsStatisticView;
    }
}
