package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * @author frc  on 17-11-11.
 */

public class LikeCommentRecordResponse extends FaceShowBaseResponse {

    private LikeCommentRecordResponse.DataBean data;

    public LikeCommentRecordResponse.DataBean getData() {
        return data;
    }

    public void setData(LikeCommentRecordResponse.DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private int userNum;
        private int totalScore;
        private int bizId;
        private String bizSource;
        private Object createTime;
        private Object avgScores;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
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

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getAvgScores() {
            return avgScores;
        }

        public void setAvgScores(Object avgScores) {
            this.avgScores = avgScores;
        }
    }
}
