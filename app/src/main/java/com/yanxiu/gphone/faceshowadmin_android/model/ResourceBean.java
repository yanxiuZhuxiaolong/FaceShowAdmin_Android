package com.yanxiu.gphone.faceshowadmin_android.model;

import java.util.ArrayList;

/**
 * Created by lufengqing on 2017/9/18.
 */

public class ResourceBean extends BaseBean {
    private String resourceName;//资源名称
    private String resourceTime;//资源上传时间
    private String resourceImg;
    private String previewurl;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceTime() {
        return resourceTime;
    }

    public void setResourceTime(String resourceTime) {
        this.resourceTime = resourceTime;
    }

    public String getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(String resourceImg) {
        this.resourceImg = resourceImg;
    }

    public String getPreviewurl() {
        return previewurl;
    }

    public void setPreviewurl(String previewurl) {
        this.previewurl = previewurl;
    }

    public static ArrayList<ResourceBean> getMockData() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ResourceBean bean = new ResourceBean();
                bean.setResourceName("专家教授有效评估");
                bean.setResourceTime("2017.08.19 13:24");
            list.add(bean);
        }
        return list;
    }
}
