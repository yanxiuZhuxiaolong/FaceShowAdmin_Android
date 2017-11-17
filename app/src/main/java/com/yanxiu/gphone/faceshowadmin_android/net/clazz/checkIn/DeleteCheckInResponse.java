package com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-11-16.
 */

public class DeleteCheckInResponse extends FaceShowBaseResponse {

    /**
     * currentUser :
     * data : null
     * error : {"code":210410,"title":"signin can't delete! signin user exist","message":"签到不能删除，用户签到数据已存在","data":null}
     */

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
