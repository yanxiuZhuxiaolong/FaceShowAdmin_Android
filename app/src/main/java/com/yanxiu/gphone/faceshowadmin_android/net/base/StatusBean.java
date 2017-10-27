package com.yanxiu.gphone.faceshowadmin_android.net.base;


import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

/**
 * 请求返回的状态bean
 */

public class StatusBean extends BaseBean {
    private String desc;
    private int code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
