package com.yanxiu.gphone.faceshowadmin_android.model;

import java.util.ArrayList;

/**
 * Created by lufengqing on 2017/9/18.
 */

public class ProjectTaskBean extends BaseBean {
    private String name;//任务名称
    private String time;
    private String imgUrl;
    private String previewurl;
    private String status;//任务完成状态

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPreviewurl() {
        return previewurl;
    }

    public void setPreviewurl(String previewurl) {
        this.previewurl = previewurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<ProjectTaskBean> getMockData() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ProjectTaskBean bean = new ProjectTaskBean();
            bean.setName("针对项目的培训");
            bean.setTime("2017.08.19 13:24");
            if(i%2 == 0) {
                bean.setStatus("0");
            } else {
                bean.setStatus("1");
            }
            list.add(bean);
        }
        return list;
    }
}
