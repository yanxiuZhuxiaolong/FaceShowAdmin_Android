package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-6.
 */

public class GetTasksRequest extends FaceShowBaseRequest {
    private String method = "app.manage.clazs.getTasks";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
