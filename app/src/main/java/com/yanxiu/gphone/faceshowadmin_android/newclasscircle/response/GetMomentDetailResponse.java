package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

/**
 * @author frc on 2018/1/26.
 */

public class GetMomentDetailResponse extends FaceShowBaseResponse {




    private ClassCircleResponse.Data.Moments data;
    private long currentTime;

    public ClassCircleResponse.Data.Moments getData() {
        return data;
    }

    public void setData(ClassCircleResponse.Data.Moments data) {
        this.data = data;
    }
}
