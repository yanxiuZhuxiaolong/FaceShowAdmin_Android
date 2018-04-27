package com.yanxiu.gphone.faceshowadmin_android.net.main;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * @author frc on 2017/12/20.
 */

public class RedDotResponse extends FaceShowBaseResponse {

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

    public static class DataBean {
        private NewBean taskNew;
        private NewBean resourceNew;
        private NewBean momentNew;
        private NewBean momentMsgNew;

        public NewBean getTaskNew() {
            return taskNew;
        }

        public void setTaskNew(NewBean taskNew) {
            this.taskNew = taskNew;
        }

        public NewBean getResourceNew() {
            return resourceNew;
        }

        public void setResourceNew(NewBean resourceNew) {
            this.resourceNew = resourceNew;
        }

        public NewBean getMomentNew() {
            return momentNew;
        }

        public void setMomentNew(NewBean momentNew) {
            this.momentNew = momentNew;
        }

        public NewBean getMomentMsgNew() {
            return momentMsgNew;
        }

        public void setMomentMsgNew(NewBean momentMsgNew) {
            this.momentMsgNew = momentMsgNew;
        }
    }

    public static class NewBean {

        private int userId;
        private int clazsId;
        private String bizId;
        private int promptNum;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getClazsId() {
            return clazsId;
        }

        public void setClazsId(int clazsId) {
            this.clazsId = clazsId;
        }

        public String getBizId() {
            return bizId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public int getPromptNum() {
            return promptNum;
        }

        public void setPromptNum(int promptNum) {
            this.promptNum = promptNum;
        }
    }
}
