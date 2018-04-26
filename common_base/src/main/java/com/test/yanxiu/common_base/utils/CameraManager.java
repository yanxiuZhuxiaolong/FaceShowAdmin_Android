package com.test.yanxiu.common_base.utils;

import android.hardware.Camera;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/24 14:45.
 * <p>
 * Function : 针对个别手机权限中存在每次询问选项，导致申请权限弹窗多次出现
 * 故暂时解法如下，后面询问产品与美工确认产品逻辑设计是否需要更改,当前效果
 * 类似于美颜相机等三方软件效果
 * <p>
 * （PS：正确解法应仿照QQ等应用，当前界面判断权限（但不申请权限,QQ在很多手机
 * 上面用的都是Android6.0之下的权限规则，所以他们不会申请权限，易学易练存
 * 在6.0之下与6.0之上两套权限处理规则，两套处理规则来历为Android自身原因，故在
 * 此不解释），没权限就退出
 */
public class CameraManager {

    private static CameraManager cameraManager;
    private Camera camera = null;

    public static CameraManager getInstence() {
        if (cameraManager == null) {
            cameraManager = new CameraManager();
        }
        return cameraManager;
    }

    private CameraManager() {

    }

    public Camera getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open(0);
            } catch (Exception e) {
                e.printStackTrace();
                camera = null;
            }
        }
        return camera;
    }

    public void releaseCamera() {
        if (camera != null) {
            camera = null;
        }
    }

}
