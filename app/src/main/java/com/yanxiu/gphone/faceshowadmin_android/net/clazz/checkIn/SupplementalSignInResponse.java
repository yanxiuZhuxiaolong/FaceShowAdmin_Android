package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-11-2.
 */

public class SupplementalSignInResponse extends FaceShowBaseResponse {



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


        private int id;
        private int userId;
        private int signinId;
        private int signinStatus;
        private String signinTime;
        private Object signinRemark;
        private String signinDevice;
        private Object signinPosition;
        private Object userName;
        private Object avatar;
        private Object stepId;
        private Object successPrompt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getSigninId() {
            return signinId;
        }

        public void setSigninId(int signinId) {
            this.signinId = signinId;
        }

        public int getSigninStatus() {
            return signinStatus;
        }

        public void setSigninStatus(int signinStatus) {
            this.signinStatus = signinStatus;
        }

        public String getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(String signinTime) {
            this.signinTime = signinTime;
        }

        public Object getSigninRemark() {
            return signinRemark;
        }

        public void setSigninRemark(Object signinRemark) {
            this.signinRemark = signinRemark;
        }

        public String getSigninDevice() {
            return signinDevice;
        }

        public void setSigninDevice(String signinDevice) {
            this.signinDevice = signinDevice;
        }

        public Object getSigninPosition() {
            return signinPosition;
        }

        public void setSigninPosition(Object signinPosition) {
            this.signinPosition = signinPosition;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public Object getStepId() {
            return stepId;
        }

        public void setStepId(Object stepId) {
            this.stepId = stepId;
        }

        public Object getSuccessPrompt() {
            return successPrompt;
        }

        public void setSuccessPrompt(Object successPrompt) {
            this.successPrompt = successPrompt;
        }
    }
}
