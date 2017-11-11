package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-11-10.
 */

public class GetCourseReplyResponse extends FaceShowBaseResponse {

    /**
     * data : {"id":3,"title":"讨论","description":"针对此课程您有什么想说的快来讨论吧！","commentStatus":1,"bizId":2,"bizSource":"clazs","createTime":"2017-09-18 12:26:33"}
     * currentUser :
     * error : null
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * title : 讨论
         * description : 针对此课程您有什么想说的快来讨论吧！
         * commentStatus : 1
         * bizId : 2
         * bizSource : clazs
         * createTime : 2017-09-18 12:26:33
         */

        private int id;
        private String title;
        private String description;
        private int commentStatus;
        private int bizId;
        private String bizSource;
        private String createTime;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getCommentStatus() {
            return commentStatus;
        }

        public void setCommentStatus(int commentStatus) {
            this.commentStatus = commentStatus;
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
    }
}
