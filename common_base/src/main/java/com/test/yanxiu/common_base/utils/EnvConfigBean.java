package com.test.yanxiu.common_base.utils;


import java.util.List;

/**
 * server接口配置封装类
 */

public class EnvConfigBean {
    private int currentIndex;   //当前使用的配置节点
    private boolean multiConfig; // 主要作为区分标记使用
    private List<UrlBean> data;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isMultiConfig() {
        return multiConfig;
    }

    public void setMultiConfig(boolean multiConfig) {
        this.multiConfig = multiConfig;
    }

    public List<UrlBean> getData() {
        return data;
    }

    public void setData(List<UrlBean> data) {
        this.data = data;
    }
}
