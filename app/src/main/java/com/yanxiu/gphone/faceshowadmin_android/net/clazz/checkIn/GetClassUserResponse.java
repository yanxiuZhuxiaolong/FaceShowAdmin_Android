package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-2.
 */

public class GetClassUserResponse extends FaceShowBaseResponse {


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

        public static class ElementsBean {

            private int signInStatus;
            private String signInTime;
            private String unit;
            private String mobilePhone;
            private String signInDevice;
            private int id;
            private String userName;
            private int userId;

            public int getSignInStatus() {
                return signInStatus;
            }

            public void setSignInStatus(int signInStatus) {
                this.signInStatus = signInStatus;
            }

            public String getSignInTime() {
                return signInTime;
            }

            public void setSignInTime(String signInTime) {
                this.signInTime = signInTime;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            public String getSignInDevice() {
                return signInDevice;
            }

            public void setSignInDevice(String signInDevice) {
                this.signInDevice = signInDevice;
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

        public static class CallbacksBean {

            private String callbackParam;
            private Object callbackValue;

            public String getCallbackParam() {
                return callbackParam;
            }

            public void setCallbackParam(String callbackParam) {
                this.callbackParam = callbackParam;
            }

            public Object getCallbackValue() {
                return callbackValue;
            }

            public void setCallbackValue(Object callbackValue) {
                this.callbackValue = callbackValue;
            }
        }
    }
}
