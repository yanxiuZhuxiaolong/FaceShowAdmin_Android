package com.yanxiu.gphone.faceshowadmin_android.main.bean;

import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

/**
 * Created by 戴延枫 on 2017/10/30.
 * 今日签到
 */

public class TodaySignInBean extends BaseBean {
    private String id;
    private String title;
    private String startTime;
    private String endTime;
    private String antiCheat;
    private String qrcodeRefreshRate;
    private String successPrompt;
    private String openStatus;
    private String bizId;
    private String bizSource;
    private String createTime;
    private String stepId;
    private String stepFinished;
    private String stepFinishedTime;
    private String totalUserNum;
    private String signInUserNum;
    private String opentStatusName;
    private String percent;
    private String userSignIn;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAntiCheat() {
        return antiCheat;
    }

    public void setAntiCheat(String antiCheat) {
        this.antiCheat = antiCheat;
    }

    public String getQrcodeRefreshRate() {
        return qrcodeRefreshRate;
    }

    public void setQrcodeRefreshRate(String qrcodeRefreshRate) {
        this.qrcodeRefreshRate = qrcodeRefreshRate;
    }

    public String getSuccessPrompt() {
        return successPrompt;
    }

    public void setSuccessPrompt(String successPrompt) {
        this.successPrompt = successPrompt;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
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

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepFinished() {
        return stepFinished;
    }

    public void setStepFinished(String stepFinished) {
        this.stepFinished = stepFinished;
    }

    public String getStepFinishedTime() {
        return stepFinishedTime;
    }

    public void setStepFinishedTime(String stepFinishedTime) {
        this.stepFinishedTime = stepFinishedTime;
    }

    public String getTotalUserNum() {
        return totalUserNum;
    }

    public void setTotalUserNum(String totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public String getSignInUserNum() {
        return signInUserNum;
    }

    public void setSignInUserNum(String signInUserNum) {
        this.signInUserNum = signInUserNum;
    }

    public String getOpentStatusName() {
        return opentStatusName;
    }

    public void setOpentStatusName(String opentStatusName) {
        this.opentStatusName = opentStatusName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getUserSignIn() {
        return userSignIn;
    }

    public void setUserSignIn(String userSignIn) {
        this.userSignIn = userSignIn;
    }
}
