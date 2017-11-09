package com.yanxiu.gphone.faceshowadmin_android.model;

/**
 * 投票的一个题目里的每一个选项
 */
public class VoteItemBean extends BaseBean {

    private String itemId;
    private String itemName;
    private String selectedNum;
    private String percent;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(String selectedNum) {
        this.selectedNum = selectedNum;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
