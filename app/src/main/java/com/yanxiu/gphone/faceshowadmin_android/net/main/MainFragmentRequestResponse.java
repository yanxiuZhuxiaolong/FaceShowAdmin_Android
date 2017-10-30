package com.yanxiu.gphone.faceshowadmin_android.net.main;

import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * 课程安排
 */

public class MainFragmentRequestResponse extends FaceShowBaseResponse {

    private CourseArrangeBean data = new CourseArrangeBean();

    public CourseArrangeBean getData() {
        return data;
    }

    public void setData(CourseArrangeBean data) {
        this.data = data;
    }
}
