package com.yanxiu.gphone.faceshowadmin_android.main;

import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

/**
 * Created by 戴延枫 on 2017/10/30.
 */

public class MainTabBean extends BaseBean {
    public int getImgResourcesId() {
        return imgResourcesId;
    }

    public void setImgResourcesId(int imgResourcesId) {
        this.imgResourcesId = imgResourcesId;
    }

    private int imgResourcesId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
