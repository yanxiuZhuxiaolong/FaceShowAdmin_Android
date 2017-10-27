package com.yanxiu.gphone.faceshowadmin_android.model;

/**
 * 课程安排
 * Created by 戴延枫 on 2017/9/15.
 */

public class MainBean extends BaseBean {

    private ProjectInfoBean projectInfo = new ProjectInfoBean();
    private ClazsInfoBean clazsInfo = new ClazsInfoBean();

    public ProjectInfoBean getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(ProjectInfoBean projectInfo) {
        this.projectInfo = projectInfo;
    }

    public ClazsInfoBean getClazsInfo() {
        return clazsInfo;
    }

    public void setClazsInfo(ClazsInfoBean clazsInfo) {
        this.clazsInfo = clazsInfo;
    }
}
