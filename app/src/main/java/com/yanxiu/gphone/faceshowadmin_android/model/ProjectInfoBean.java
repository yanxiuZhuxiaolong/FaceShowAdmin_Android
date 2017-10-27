package com.yanxiu.gphone.faceshowadmin_android.model;

import java.util.ArrayList;

/**
 * 首页项目数据
 * Created by 戴延枫 on 2017/9/21.
 */

public class ProjectInfoBean extends BaseBean {
    private String id;
    private String platId;
    private String projectName;
    private String projectStatus;
    private String startTime;
    private String endTime;
    private String description;
    private ArrayList<ProjectManagerInfos> projectManagerInfos = new ArrayList<>();
    private String projectStatusName;
    private String manager;

    public class ProjectManagerInfos extends BaseBean {
        private String id;
        private String projectId;
        private String userId;
        private String userName;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatId() {
        return platId;
    }

    public void setPlatId(String platId) {
        this.platId = platId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ProjectManagerInfos> getProjectManagerInfos() {
        return projectManagerInfos;
    }

    public void setProjectManagerInfos(ArrayList<ProjectManagerInfos> projectManagerInfos) {
        this.projectManagerInfos = projectManagerInfos;
    }
}
