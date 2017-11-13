package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-11-3.
 */

public class GetCheckInDetailResponse extends FaceShowBaseResponse {


    /**
     * data : {"interactType":6,"signIn":{"id":384,"title":"ios签到11","startTime":"2017-09-25 03:00:00","endTime":"2017-09-25 21:00:00","antiCheat":1,"qrcodeRefreshRate":10,"successPrompt":"成功不了","openStatus":7,"bizId":44,"bizSource":"course","createTime":"2017-09-26 14:06:58","stepId":null,"stepFinished":null,"stepFinishedTime":null,"totalUserNum":9,"signInUserNum":2,"opentStatusName":"已结束","percent":0.222,"userSignIn":null}}
     * currentUser :
     * currentTime : 1510542634775
     * error : null
     */

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
        /**
         * interactType : 6
         * signIn : {"id":384,"title":"ios签到11","startTime":"2017-09-25 03:00:00","endTime":"2017-09-25 21:00:00","antiCheat":1,"qrcodeRefreshRate":10,"successPrompt":"成功不了","openStatus":7,"bizId":44,"bizSource":"course","createTime":"2017-09-26 14:06:58","stepId":null,"stepFinished":null,"stepFinishedTime":null,"totalUserNum":9,"signInUserNum":2,"opentStatusName":"已结束","percent":0.222,"userSignIn":null}
         */

        private int interactType;
        private SignInBean signIn;

        public int getInteractType() {
            return interactType;
        }

        public void setInteractType(int interactType) {
            this.interactType = interactType;
        }

        public SignInBean getSignIn() {
            return signIn;
        }

        public void setSignIn(SignInBean signIn) {
            this.signIn = signIn;
        }

        public static class SignInBean {
            /**
             * id : 384
             * title : ios签到11
             * startTime : 2017-09-25 03:00:00
             * endTime : 2017-09-25 21:00:00
             * antiCheat : 1
             * qrcodeRefreshRate : 10
             * successPrompt : 成功不了
             * openStatus : 7
             * bizId : 44
             * bizSource : course
             * createTime : 2017-09-26 14:06:58
             * stepId : null
             * stepFinished : null
             * stepFinishedTime : null
             * totalUserNum : 9
             * signInUserNum : 2
             * opentStatusName : 已结束
             * percent : 0.222
             * userSignIn : null
             */

            private int id;
            private String title;
            private String startTime;
            private String endTime;
            private int antiCheat;
            private int qrcodeRefreshRate;
            private String successPrompt;
            private int openStatus;
            private int bizId;
            private String bizSource;
            private String createTime;
            private Object stepId;
            private Object stepFinished;
            private Object stepFinishedTime;
            private int totalUserNum;
            private int signInUserNum;
            private String opentStatusName;
            private double percent;
            private Object userSignIn;

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

            public int getQrcodeRefreshRate() {
                return qrcodeRefreshRate;
            }

            public void setQrcodeRefreshRate(int qrcodeRefreshRate) {
                this.qrcodeRefreshRate = qrcodeRefreshRate;
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

            public Object getStepFinished() {
                return stepFinished;
            }

            public void setStepFinished(Object stepFinished) {
                this.stepFinished = stepFinished;
            }

            public Object getStepFinishedTime() {
                return stepFinishedTime;
            }

            public void setStepFinishedTime(Object stepFinishedTime) {
                this.stepFinishedTime = stepFinishedTime;
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

            public double getPercent() {
                return percent;
            }

            public void setPercent(double percent) {
                this.percent = percent;
            }

            public Object getUserSignIn() {
                return userSignIn;
            }

            public void setUserSignIn(Object userSignIn) {
                this.userSignIn = userSignIn;
            }
        }
    }
}
