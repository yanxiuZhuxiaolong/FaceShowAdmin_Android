package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceDataBean;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleBean;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleDataBean;

/**
 * 资源
 */

public class ResourceResponse extends FaceShowBaseResponse {

    private ResourceDataBean data = new ResourceDataBean();

    public ResourceDataBean getData() {
        return data;
    }

    public void setData(ResourceDataBean data) {
        this.data = data;
    }
}
