package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-9.
 */

public class GetCourseResourcesResponse extends FaceShowBaseResponse {

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

        private ResourcesBean resources;

        public ResourcesBean getResources() {
            return resources;
        }

        public void setResources(ResourcesBean resources) {
            this.resources = resources;
        }

    }

    public static class ResourcesBean {

        private int totalElements;
        private String callbackParam;
        private int callbackValue;
        private List<ElementsBean> elements;
        private List<CallbacksBean> callbacks;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

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

    public static class ElementsBean {
        /**
         * id : 180
         * resName : 3333
         * type : 2
         * publisherId : 9768712
         * publisherName : 多隆测试
         * viewNum : 1
         * downNum : 0
         * state : 1
         * createTime : 2017-10-26 14:10:26
         * resId : 28939342
         * suffix : doc
         * url :
         * clazzId : 9
         * courseId : 0
         * createTimeStr : null
         * ai : null
         * resNum : null
         * viewNumSum : null
         * downNumSum : null
         * totalClazsStudentNum : 5
         * viewClazsStudentNum : 0
         */

        private int id;
        private String resName;
        private String type;
        private int publisherId;
        private String publisherName;
        private int viewNum;
        private int downNum;
        private int state;
        private String createTime;
        private int resId;
        private String suffix;
        private String url;
        private int clazzId;
        private int courseId;
        private Object createTimeStr;
        private Object ai;
        private Object resNum;
        private Object viewNumSum;
        private Object downNumSum;
        private int totalClazsStudentNum;
        private int viewClazsStudentNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getResName() {
            return resName;
        }

        public void setResName(String resName) {
            this.resName = resName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getPublisherId() {
            return publisherId;
        }

        public void setPublisherId(int publisherId) {
            this.publisherId = publisherId;
        }

        public String getPublisherName() {
            return publisherName;
        }

        public void setPublisherName(String publisherName) {
            this.publisherName = publisherName;
        }

        public int getViewNum() {
            return viewNum;
        }

        public void setViewNum(int viewNum) {
            this.viewNum = viewNum;
        }

        public int getDownNum() {
            return downNum;
        }

        public void setDownNum(int downNum) {
            this.downNum = downNum;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getClazzId() {
            return clazzId;
        }

        public void setClazzId(int clazzId) {
            this.clazzId = clazzId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public Object getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(Object createTimeStr) {
            this.createTimeStr = createTimeStr;
        }

        public Object getAi() {
            return ai;
        }

        public void setAi(Object ai) {
            this.ai = ai;
        }

        public Object getResNum() {
            return resNum;
        }

        public void setResNum(Object resNum) {
            this.resNum = resNum;
        }

        public Object getViewNumSum() {
            return viewNumSum;
        }

        public void setViewNumSum(Object viewNumSum) {
            this.viewNumSum = viewNumSum;
        }

        public Object getDownNumSum() {
            return downNumSum;
        }

        public void setDownNumSum(Object downNumSum) {
            this.downNumSum = downNumSum;
        }

        public int getTotalClazsStudentNum() {
            return totalClazsStudentNum;
        }

        public void setTotalClazsStudentNum(int totalClazsStudentNum) {
            this.totalClazsStudentNum = totalClazsStudentNum;
        }

        public int getViewClazsStudentNum() {
            return viewClazsStudentNum;
        }

        public void setViewClazsStudentNum(int viewClazsStudentNum) {
            this.viewClazsStudentNum = viewClazsStudentNum;
        }
    }

    public static class CallbacksBean {
        /**
         * callbackParam : id
         * callbackValue : 180
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

}
