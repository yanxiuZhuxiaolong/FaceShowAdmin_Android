package com.yanxiu.gphone.faceshowadmin_android.model;

import java.util.ArrayList;

/**
 * 投票答案
 */
public class UserAnswerBean extends BaseBean {
    private int userId;
    private int questionId;

    private ArrayList<String> questionAnswers = new ArrayList<>();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public ArrayList<String> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(ArrayList<String> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}
