package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-8.
 */

public class GetClazsUserQuestionResponse extends FaceShowBaseResponse {


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

        private int totalElements;
        private Object callbackParam;
        private Object callbackValue;
        private List<ElementsBean> elements;
        private List<CallbacksBean> callbacks;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public Object getCallbackParam() {
            return callbackParam;
        }

        public void setCallbackParam(Object callbackParam) {
            this.callbackParam = callbackParam;
        }

        public Object getCallbackValue() {
            return callbackValue;
        }

        public void setCallbackValue(Object callbackValue) {
            this.callbackValue = callbackValue;
        }

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public List<CallbacksBean> getCallbacks() {
            return callbacks;
        }

        public void setCallbacks(List<CallbacksBean> callbacks) {
            this.callbacks = callbacks;
        }


    }

    public static class CallbacksBean {
        /**
         * callbackParam : id
         * callbackValue : 227
         */

        private String callbackParam;
        private int callbackValue;

        public String getCallbackParam() {
            return callbackParam;
        }

        public void setCallbackParam(String callbackParam) {
            this.callbackParam = callbackParam;
        }

        public int getCallbackValue() {
            return callbackValue;
        }

        public void setCallbackValue(int callbackValue) {
            this.callbackValue = callbackValue;
        }
    }

    public static class ElementsBean {

        private Object unit;
        private String mobilePhone;
        private int finishedStatus;
        private String finishedTime;
        private int id;
        private String userName;
        private int userId;

        public Object getUnit() {
            return unit;
        }

        public void setUnit(Object unit) {
            this.unit = unit;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public int getFinishedStatus() {
            return finishedStatus;
        }

        public void setFinishedStatus(int finishedStatus) {
            this.finishedStatus = finishedStatus;
        }

        public String getFinishedTime() {
            return finishedTime;
        }

        public void setFinishedTime(String finishedTime) {
            this.finishedTime = finishedTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

}
