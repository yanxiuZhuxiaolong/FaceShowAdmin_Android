package com.yanxiu.gphone.faceshowadmin_android.net.clazz;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by frc on 17-10-30.
 */

public class GetClazzListResponse extends FaceShowBaseResponse {


    private DataBean data;
    private long currentTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public static class DataBean implements Serializable {
        private List<ClazsInfosBean> clazsInfos;

        public List<ClazsInfosBean> getClazsInfos() {
            return clazsInfos;
        }

        public void setClazsInfos(List<ClazsInfosBean> clazsInfos) {
            this.clazsInfos = clazsInfos;
        }

        public static class ClazsInfosBean implements Serializable {
            /**
             * id : 2
             * platId : 1
             * projectId : 1
             * clazsName : 面授第二班
             * clazsStatus : 12
             * clazsType : 1
             * startTime : 2017-09-14 00:00:00
             * endTime : 2017-12-01 00:00:00
             * description : 第二个班级
             * manager : null
             * master : null
             * clazsStatusName : 进行中
             */

            private int id;
            private int platId;
            private int projectId;
            private String clazsName;
            private int clazsStatus;
            private int clazsType;
            private String startTime;
            private String endTime;
            private String description;
            private Object manager;
            private Object master;
            private String clazsStatusName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPlatId() {
                return platId;
            }

            public void setPlatId(int platId) {
                this.platId = platId;
            }

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
            }

            public String getClazsName() {
                return clazsName;
            }

            public void setClazsName(String clazsName) {
                this.clazsName = clazsName;
            }

            public int getClazsStatus() {
                return clazsStatus;
            }

            public void setClazsStatus(int clazsStatus) {
                this.clazsStatus = clazsStatus;
            }

            public int getClazsType() {
                return clazsType;
            }

            public void setClazsType(int clazsType) {
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

            public Object getManager() {
                return manager;
            }

            public void setManager(Object manager) {
                this.manager = manager;
            }

            public Object getMaster() {
                return master;
            }

            public void setMaster(Object master) {
                this.master = master;
            }

            public String getClazsStatusName() {
                return clazsStatusName;
            }

            public void setClazsStatusName(String clazsStatusName) {
                this.clazsStatusName = clazsStatusName;
            }
        }
    }
}
