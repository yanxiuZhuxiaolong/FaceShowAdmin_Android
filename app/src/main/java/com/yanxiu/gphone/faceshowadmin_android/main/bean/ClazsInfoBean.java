package com.yanxiu.gphone.faceshowadmin_android.main.bean;


import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

/**
 * 首页班级数据
 * Created by 戴延枫 on 2017/10/30.
 */

public class ClazsInfoBean extends BaseBean {
    private String id;
    private String platId;
    private String projectId;
    private String clazsName;
    private String clazsStatus;
    private String clazsType;
    private String startTime;
    private String endTime;
    private String description;
    private String clazsStatusName;
    private String master;
    private String manager;

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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getClazsName() {
        return clazsName;
    }

    public void setClazsName(String clazsName) {
        this.clazsName = clazsName;
    }

    public String getClazsStatus() {
        return clazsStatus;
    }

    public void setClazsStatus(String clazsStatus) {
        this.clazsStatus = clazsStatus;
    }

    public String getClazsType() {
        return clazsType;
    }

    public void setClazsType(String clazsType) {
        this.clazsType = clazsType;
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

    public String getClazsStatusName() {
        return clazsStatusName;
    }

    public void setClazsStatusName(String clazsStatusName) {
        this.clazsStatusName = clazsStatusName;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
