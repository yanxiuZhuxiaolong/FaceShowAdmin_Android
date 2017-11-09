package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceDataBean;

/**
 *  资源详情
 */

public class ResourceDetailResponse extends FaceShowBaseResponse {

    private ResourceDetailBean data;

    public ResourceDetailBean getData() {
        return data;
    }

    public void setData(ResourceDetailBean data) {
        this.data = data;
    }

    public static class ResourceDetailBean {
        /**
         * id : 1
         * resName : testname
         * type : 0
         * pulisherId : 0
         * publisherName : 多隆
         * viewNum : 0
         * downNum : 0
         * state : 1
         * createTime : 2017-09-18 19:22:57
         * resId : 73
         * suffix : pdf
         * url : null
         * createTimeStr : null
         */

        private int id;
        private String resName;
        private String type;
        private int pulisherId;
        private String publisherName;
        private int viewNum;
        private int downNum;
        private int state;
        private String createTime;
        private int resId;
        private String suffix;
        private String url;
        private String createTimeStr;
        private AttachmentInfosBean ai;

        public AttachmentInfosBean getAi() {
            return ai;
        }

        public void setAi(AttachmentInfosBean ai) {
            this.ai = ai;
        }

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

        public int getPulisherId() {
            return pulisherId;
        }

        public void setPulisherId(int pulisherId) {
            this.pulisherId = pulisherId;
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

        public String getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(String createTimeStr) {
            this.createTimeStr = createTimeStr;
        }
    }
}
