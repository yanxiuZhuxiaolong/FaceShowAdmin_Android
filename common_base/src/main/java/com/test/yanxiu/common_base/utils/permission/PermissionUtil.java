package com.test.yanxiu.common_base.utils.permission;

import android.hardware.Camera;
import android.os.Environment;

import com.test.yanxiu.common_base.utils.CameraManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * 权限相关
 * Created by 戴延枫 on 2017/7/12.
 */

public class PermissionUtil {
    public static final String DIR_ROOT = "/FaceShow";
    public static final String DIR_APP = "/app";
    public static final String DIR_IMAGE = "/image";
    public static final String ROOT_DIRECTORY_NAME = "faceshow";
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath() + DIR_ROOT;
    public static final String TESTFILE_NAME = "/testPermission.txt";

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = CameraManager.getInstence().getCamera();
        if (mCamera == null) {
            isCanUse = false;
        }
        return isCanUse;
    }

    /**
     * 创建file
     * 为了解决6.0一下某些机型创建file失败
     *
     * @return
     */
    private static File createFile() {
        File dir = new File(SDCARD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dirApp = new File(SDCARD_DIR + DIR_APP);
        if (!dirApp.exists()) {
            dirApp.mkdirs();
        }
        File file = new File(SDCARD_DIR + DIR_APP + DIR_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 检测读取权限
     *
     * @return
     */
    public static boolean checkReadPermission() {
        boolean result;
        File dir = createFile();
        FileInputStream fis = null;
        try {
            File file = new File(dir.getAbsolutePath() + TESTFILE_NAME);
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String result1 = br.readLine();
            result = true;
        } catch (Exception e) {
            // TODO 失败
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 检测写权限
     *
     * @return
     */
    public static boolean checkWritePermission() {
        boolean result;
        File dir = createFile();
        FileOutputStream fos = null;
        try {
            File file = new File(dir.getAbsolutePath() + TESTFILE_NAME);
            fos = new FileOutputStream(file);
            fos.write("hahaha".getBytes());
            fos.close();
            result = true;
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
