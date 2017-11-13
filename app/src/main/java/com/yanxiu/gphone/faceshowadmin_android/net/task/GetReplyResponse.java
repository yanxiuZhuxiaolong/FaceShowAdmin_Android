package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-11.
 */

public class GetReplyResponse extends FaceShowBaseResponse {

    /**
     * data : [{"id":430,"userId":23248754,"answer":"寂寞的人","questionId":687,"userName":"菜菜01","avatar":""}]
     * currentUser :
     * currentTime : 1510386385082
     * error : null
     */

    private long currentTime;
    private List<DataBean> data;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 430
         * userId : 23248754
         * answer : 寂寞的人
         * questionId : 687
         * userName : 菜菜01
         * avatar :
         */

        private int id;
        private int userId;
        private String answer;
        private int questionId;
        private String userName;
        private String avatar;

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

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
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
    }
}
