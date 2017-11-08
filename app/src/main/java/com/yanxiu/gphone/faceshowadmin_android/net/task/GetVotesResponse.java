package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.model.QusetionBean;
import com.yanxiu.gphone.faceshowadmin_android.model.QusetionGroupBean;
import com.yanxiu.gphone.faceshowadmin_android.model.VoteInfoBean;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-7.
 */

public class GetVotesResponse extends FaceShowBaseResponse {

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

        private int interactType;
        private QusetionGroupBean questionGroup;

        public int getInteractType() {
            return interactType;
        }

        public void setInteractType(int interactType) {
            this.interactType = interactType;
        }

        public QusetionGroupBean getQuestionGroup() {
            return questionGroup;
        }

        public void setQuestionGroup(QusetionGroupBean questionGroup) {
            this.questionGroup = questionGroup;
        }

    }
}
