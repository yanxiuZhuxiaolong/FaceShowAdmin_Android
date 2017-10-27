package com.yanxiu.gphone.faceshowadmin_android.net.base;

import java.io.Serializable;

/**
 * rersponse基类
 */

public class FaceShowBaseResponse implements Serializable {
    private int code;
    private String message;
    private Object currentUser;
    private Error error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Object currentUser) {
        this.currentUser = currentUser;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error implements Serializable {
        private int code;
        private String title;
        private String message;
        private ErrorData data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ErrorData getData() {
            return data;
        }

        public void setData(ErrorData data) {
            this.data = data;
        }
    }

    public class ErrorData implements Serializable {

        /**
         * id : 62
         * title : 2017-09-26下午签到
         * startTime : 2017-09-26 07:00:00
         * endTime : 2017-09-26 15:00:00
         * antiCheat : 1
         * successPrompt : 签到成功，感谢您的参与！
         * openStatus : 6
         * bizId : 9
         * bizSource : clazs
         * createTime : 2017-09-22 09:36:23
         * stepId : null
         * totalUserNum : 0
         * signInUserNum : 0
         * opentStatusName : 进行中
         * percent : 0
         * userSignIn : null
         */

        private int id;
        private String title;
        private String startTime;
        private String endTime;
        private int antiCheat;
        private String successPrompt;
        private int openStatus;
        private int bizId;
        private String bizSource;
        private String createTime;
        private Object stepId;
        private int totalUserNum;
        private int signInUserNum;
        private String opentStatusName;
        private int percent;
        private UserSignInBean userSignIn;
        private String signinTime;

        public String getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(String signinTime) {
            this.signinTime = signinTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public int getAntiCheat() {
            return antiCheat;
        }

        public void setAntiCheat(int antiCheat) {
            this.antiCheat = antiCheat;
        }

        public String getSuccessPrompt() {
            return successPrompt;
        }

        public void setSuccessPrompt(String successPrompt) {
            this.successPrompt = successPrompt;
        }

        public int getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(int openStatus) {
            this.openStatus = openStatus;
        }

        public int getBizId() {
            return bizId;
        }

        public void setBizId(int bizId) {
            this.bizId = bizId;
        }

        public String getBizSource() {
            return bizSource;
        }

        public void setBizSource(String bizSource) {
            this.bizSource = bizSource;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getStepId() {
            return stepId;
        }

        public void setStepId(Object stepId) {
            this.stepId = stepId;
        }

        public int getTotalUserNum() {
            return totalUserNum;
        }

        public void setTotalUserNum(int totalUserNum) {
            this.totalUserNum = totalUserNum;
        }

        public int getSignInUserNum() {
            return signInUserNum;
        }

        public void setSignInUserNum(int signInUserNum) {
            this.signInUserNum = signInUserNum;
        }

        public String getOpentStatusName() {
            return opentStatusName;
        }

        public void setOpentStatusName(String opentStatusName) {
            this.opentStatusName = opentStatusName;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        public UserSignInBean getUserSignIn() {
            return userSignIn;
        }

        public void setUserSignIn(UserSignInBean userSignIn) {
            this.userSignIn = userSignIn;
        }

    }

    public class UserSignInBean implements Serializable {
        private String signinTime;

        public String getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(String signinTime) {
            this.signinTime = signinTime;
        }
    }
}
