package com.yanxiu.gphone.faceshowadmin_android.net.notice;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by lufengqing on 2017/11/3.
 */

public class NoticeSaveResponse extends FaceShowBaseResponse {

    /**
     * data : {"id":241,"title":"hdjd","content":"bjjj","authorId":23246747,"clazzId":14,"createTime":null,"updateTime":null,"state":1,"attachUrl":"28939432","readNum":0,"attachName":""}
     * currentUser :
     * currentTime : 1509681180275
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
         * id : 241
         * title : hdjd
         * content : bjjj
         * authorId : 23246747
         * clazzId : 14
         * createTime : null
         * updateTime : null
         * state : 1
         * attachUrl : 28939432
         * readNum : 0
         * attachName :
         */

        private String id;
        private String title;
        private String content;
        private int authorId;
        private int clazzId;
        private String createTime;
        private String updateTime;
        private int state;
        private String attachUrl;
        private int readNum;
        private String attachName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public int getClazzId() {
            return clazzId;
        }

        public void setClazzId(int clazzId) {
            this.clazzId = clazzId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAttachUrl() {
            return attachUrl;
        }

        public void setAttachUrl(String attachUrl) {
            this.attachUrl = attachUrl;
        }

        public int getReadNum() {
            return readNum;
        }

        public void setReadNum(int readNum) {
            this.readNum = readNum;
        }

        public String getAttachName() {
            return attachName;
        }

        public void setAttachName(String attachName) {
            this.attachName = attachName;
        }
    }
}
