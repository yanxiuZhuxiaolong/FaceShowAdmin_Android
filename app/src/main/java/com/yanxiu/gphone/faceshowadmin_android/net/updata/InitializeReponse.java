package com.yanxiu.gphone.faceshowadmin_android.net.updata;

import com.yanxiu.gphone.faceshowadmin_android.net.base.StatusBean;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/7 17:19.
 * Function :
 */
public class InitializeReponse {

    protected StatusBean status;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
    public List<Data> data;

    public class Data{
        public int id;
        public String version;
        public String title;
        public String resid;
        public String ostype;
        public String upgradetype;
        public String upgradeswitch;
        public String targetenv;
        public String uploadtime;
        public String modifytime;
        public String content;
        public String fileURL;
        public String fileLocalPath;
        public String productName;
        public String productLine;
        public String ifCheck;
    }
}
