package com.yanxiu.gphone.faceshowadmin_android.model;

/**
 * 投票封装类
 */
public class VoteBean extends BaseBean {
    //1,单选 2,多选 3,填空
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_MULTI = 2;
    public static final int TYPE_TEXT = 3;

    private String interactType;
    private boolean isAnswer;
    private QusetionGroupBean questionGroup = new QusetionGroupBean();

    public String getInteractType() {
        return interactType;
    }

    public void setInteractType(String interactType) {
        this.interactType = interactType;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public QusetionGroupBean getQuestionGroup() {
        return questionGroup;
    }

    public void setQuestionGroup(QusetionGroupBean questionGroup) {
        this.questionGroup = questionGroup;
    }

}
