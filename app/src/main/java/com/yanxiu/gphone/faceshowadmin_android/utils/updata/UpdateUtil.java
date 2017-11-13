package com.yanxiu.gphone.faceshowadmin_android.utils.updata;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.ActivityManger;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.updata.DownLoadRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.updata.InitializeReponse;
import com.yanxiu.gphone.faceshowadmin_android.net.updata.InitializeRequest;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.Logger;
import com.yanxiu.gphone.faceshowadmin_android.utils.SystemUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.io.File;
import java.util.List;

/**
 * 升级工具类
 * Created by Canghaixiao.
 * Time : 2017/6/7 16:36.
 * Function :
 */
public class UpdateUtil {

    private static final String TAG="updata";

    private static InitializeRequest mInitializeRequest;
//     private static UpdateDialog mUpdateDialog;
    private static SystemUpdataDialog mUpdateDialog;
    private static NotificationManager mNotificationManager;
    private static Notification mNotification;
    public static final int NOTIFICATION_ID = 0x11;

    private static Handler mHandler=new Handler(Looper.getMainLooper());

    public interface OnUpgradeCallBack {
        void onExit();

        void onDownloadApk(boolean isSuccess);

        void onInstallApk(boolean isSuccess);
    }

    public static void Initialize(final Context context, final boolean isFromUser) {
        if (!isFromUser && Constants.UPDATA_TYPE == 1) {
            return;
        }
        Constants.UPDATA_TYPE = 1;
        updateCancel();
        String channel = SystemUtil.getChannelName();
        mInitializeRequest = new InitializeRequest();
        mInitializeRequest.channel = channel;
        mInitializeRequest.startRequest(InitializeReponse.class, new HttpCallback<InitializeReponse>() {
            @Override
            public void onSuccess(RequestBase request, InitializeReponse ret) {
                if (ret.data != null && ret.data.size() > 0) {
                    boolean isUpgrade = checkIsShouldUpdate(Constants.versionName, ret.data.get(0).version);
                    if (isUpgrade) {
                        if (!TextUtils.isEmpty(ret.data.get(0).fileURL)) {
                            String[] str = ret.data.get(0).fileURL.split("\\.");
                            if (str.length>1&&"apk".equals(str[str.length-1])) {
//                                 if (true){
                                showUpdateDialog(context, ret.data.get(0), new OnUpgradeCallBack() {
                                    @Override
                                     public void onExit() {
                                        ActivityManger.destoryAll();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(-1);
                                    }

                                    @Override
                                    public void onDownloadApk(boolean isSuccess) {
                                    }

                                    @Override
                                    public void onInstallApk(boolean isSuccess) {

                                    }
                                });
                            }else {
//                                 ToastManager.showMsgOnDebug("这是版本更新接口提示,测试同学,你们辛苦了,请按照正常的作业流程走,不要乱跑,谢谢合作,此条消息只在debug环境下出现");
                            }
                        }else {
//                             ToastManager.showMsgOnDebug("这是版本更新接口提示,测试同学,你们辛苦了,请按照正常的作业流程走,不要乱跑,谢谢合作,此条消息只在debug环境下出现");
                        }
                    } else {
                        if (isFromUser) {
                            ToastUtil.showToast(context,R.string.update_new);
                        }
                    }
                }else {
                    if (isFromUser) {
                        ToastUtil.showToast(context, R.string.update_new);
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (isFromUser) {
                    ToastUtil.showToast(context,error.getMessage());
                }
            }
        });
    }

    private static class MyPermission implements OnPermissionCallback {

        Context context;
        InitializeReponse.Data data;
        OnUpgradeCallBack callBack;

        public MyPermission(Context context, InitializeReponse.Data data, OnUpgradeCallBack callBack){
            this.context=context;
            this.data=data;
            this.callBack=callBack;
        }

        @Override
        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotification = new Notification();
            downloadApk(context, data.fileURL, callBack);
        }

        @Override
        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {

        }
    }

    private static void showUpdateDialog(final Context context, final InitializeReponse.Data data, final OnUpgradeCallBack callBack) {
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }
//         mUpdateDialog = new UpdateDialog(context, data.upgradetype, new UpdateDialog.UpdateDialogCallBack() {
//             @Override
//             public void update() {
//                 FaceShowBaseActivity.requestWriteAndReadPermission(new MyPermission(context, data, callBack));
//             }
//
//             @Override
//             public void cancel() {
//             }
//
//             @Override
//             public void exit() {
//                 if (callBack != null) {
//                     callBack.onExit();
//                 }
//             }
//         });
//         mUpdateDialog.setTitles(data.title, data.version);
//         mUpdateDialog.setContent(data.content);
//         mUpdateDialog.setCanceledOnTouchOutside(false);
//         mUpdateDialog.show();
       mUpdateDialog=new SystemUpdataDialog(context,data.content, data.upgradetype, new SystemUpdataDialog.UpdateDialogCallBack() {
           @Override
           public void update() {
                FaceShowBaseActivity.requestWriteAndReadPermission(new MyPermission(context, data, callBack));
           }

           @Override
           public void cancel() {

           }

           @Override
           public void exit() {
                if (callBack != null) {
                    callBack.onExit();
                }
           }
       });
        mUpdateDialog.setTitles(data.title, data.version);
        mUpdateDialog.setContent(data.content);
        mUpdateDialog.show();
    }

    private static void downloadApk(final Context context, String fileURL, final OnUpgradeCallBack callBack) {
        DownLoadRequest.getInstense().download(fileURL, FileUtils.getExternalStorageAbsolutePath("faceshow_admin.apk"), new DownLoadRequest.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String saveDir) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                mNotification.icon = R.drawable.notification01;
                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading_success);
                mNotification.contentView.setProgressBar(R.id.progress_value, 100, 100, false);
                mNotification.contentView.setViewVisibility(R.id.progress_value, View.GONE);
                if (callBack != null) {
                    callBack.onDownloadApk(true);
                    installApk(context, saveDir);
                }
            }

            @Override
            public void onDownloadStart() {
                Intent intent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading);
                mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                mNotification.icon = R.drawable.notification04;
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_updata_layout);
                remoteViews.setTextViewText(R.id.app_name, context.getResources().getString(R.string.app_name));
                remoteViews.setTextViewText(R.id.progress_text, "0%");
                remoteViews.setProgressBar(R.id.progress_value, 100, 0, false);
                mNotification.contentView = remoteViews;
                mNotification.contentIntent = pendingIntent;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            }

            @Override
            public void onDownloading(int progress) {
                Logger.d(TAG,"Download"+progress);
                mUpdateDialog.setProgress((int) progress);
                mNotification.contentView.setProgressBar(R.id.progress_value, 100, (int) progress, false);
                mNotification.contentView.setTextViewText(R.id.progress_text, (int) progress + "%");
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            }

            @Override
            public void onDownloadFailed() {
                Logger.d(TAG,"onfail");
                mNotificationManager.cancel(NOTIFICATION_ID);
                mNotification.icon = R.drawable.notification01;
                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading_fail);
                if (callBack != null) {
                    callBack.onDownloadApk(false);
                }
            }
        });
    }

    private static void installApk(Context context, String filePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        SpManager.setFristStartUp(true);
        String type = "application/vnd.android.package-archive";
        File file = new File(filePath);
        intent.setDataAndType(Uri.fromFile(file), type);
        context.startActivity(intent);
    }

    private static boolean checkIsShouldUpdate(String nowVersion, String serverVersion) {
        if (!isEmpty(nowVersion) && !isEmpty(serverVersion)) {
            if (!nowVersion.equals(serverVersion)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (null != str) {
            if (str.length() > 4) {
                return false;
            }
        }
        return null == str || "".equals(str) || "NULL"
                .equals(str.toUpperCase());
    }

    private static void updateCancel() {
        if (mInitializeRequest != null) {
            mInitializeRequest.cancelRequest();
            mInitializeRequest = null;
        }
    }

}
