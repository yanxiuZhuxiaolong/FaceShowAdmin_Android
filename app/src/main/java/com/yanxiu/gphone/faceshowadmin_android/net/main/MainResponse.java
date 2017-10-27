package com.yanxiu.gphone.faceshowadmin_android.net.main;


import com.yanxiu.gphone.faceshowadmin_android.model.MainBean;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * 课程评价
 */

public class MainResponse extends FaceShowBaseResponse {

    private MainBean data = new MainBean();

    public MainBean getData() {
        return data;
    }

    public void setData(MainBean data) {
        this.data = data;
    }

    private long currentTime;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
