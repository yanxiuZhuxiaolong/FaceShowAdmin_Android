package com.test.yanxiu.common_base.utils.permission;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by 戴延枫 on 2017/7/12.
 */

public interface OnPermissionCallback {
    /**
     * 获取权限
     */
    void onPermissionsGranted(@Nullable List<String> deniedPermissions);

    /**
     * 没有权限
     * @param deniedPermissions
     */
    void onPermissionsDenied(@Nullable List<String> deniedPermissions);
}
