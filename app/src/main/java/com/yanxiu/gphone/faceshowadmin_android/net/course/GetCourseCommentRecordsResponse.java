package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by frc on 17-11-10.
 */

public class GetCourseCommentRecordsResponse extends FaceShowBaseResponse {


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

    public static class ElementsBean implements Serializable {
        /**
         * id : 119
         * userId : 23248755
         * anonymous : 0
         * content : Very well
         * replyNum : 0
         * likeNum : 1
         * replyCommentRecordId : 0
         * commentId : 15
         * createTime : 2017-09-27 11:13:08
         * userName : 菜菜02
         * avatar : http://orz.yanxiu.com/easygo/file/2017/9/27/1506481003152l18126_100-100.jpg
         * userLiked : 0
         * replays : null
         */

        private int id;
        private int userId;
        private int anonymous;
        private String content;
        private int replyNum;
        private int likeNum;
        private int replyCommentRecordId;
        private int commentId;
        private String createTime;
        private String userName;
        private String avatar;
        private int userLiked;
        private Object replays;

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

        public int getAnonymous() {
            return anonymous;
        }

        public void setAnonymous(int anonymous) {
            this.anonymous = anonymous;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(int replyNum) {
            this.replyNum = replyNum;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getReplyCommentRecordId() {
            return replyCommentRecordId;
        }

        public void setReplyCommentRecordId(int replyCommentRecordId) {
            this.replyCommentRecordId = replyCommentRecordId;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserLiked() {
            return userLiked;
        }

        public void setUserLiked(int userLiked) {
            this.userLiked = userLiked;
        }

        public Object getReplays() {
            return replays;
        }

        public void setReplays(Object replays) {
            this.replays = replays;
        }
    }

    public static class CallbacksBean {
        /**
         * callbackParam : id
         * callbackValue : 99
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
