package com.yanxiu.gphone.faceshowadmin_android.model;

import java.util.ArrayList;

/**
 * 投票封装类
 */
public class QusetionGroupBean extends BaseBean {
    private int id;
    private String title;
    private String description;
    private int questionNum;
    private int groupType;
    private int groupStatus;
    private int bizId;
    private String bizSource;
    private String createTime;
    private int stepId;
    private int answerNum;
    private int answerUserNum;
    private int totalUserNum;
    private ArrayList<QusetionBean> questions = new ArrayList<>();

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

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
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

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getAnswerUserNum() {
        return answerUserNum;
    }

    public void setAnswerUserNum(int answerUserNum) {
        this.answerUserNum = answerUserNum;
    }

    public int getTotalUserNum() {
        return totalUserNum;
    }

    public void setTotalUserNum(int totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public ArrayList<QusetionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QusetionBean> questions) {
        this.questions = questions;
    }
}
