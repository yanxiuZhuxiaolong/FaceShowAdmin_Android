package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceDataBean;

/**
 * 资源
 */

public class CreateResourceResponse extends FaceShowBaseResponse {

    private String desc;
    private String resid;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }
}
