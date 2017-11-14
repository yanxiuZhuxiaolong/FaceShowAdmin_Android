package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.HeadimgUploadBean;
import com.yanxiu.gphone.faceshowadmin_android.common.bean.UserMessageChangedBean;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.UpLoadRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.UserMessageUpdataRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/9 10:40.
 * Function :
 */
public class UserMessageUpdateManager {

    public interface onRequestCallback {
        void onHttpStart();

        void onSuccess(FaceShowBaseResponse response, String updateMessage, String updataType);

        void onError(Error error);

        void onHttpFinish();
    }

    public static final String UPDATA_HEADIMG = "headimg";
    public static final String UPDATA_NAME = "name";
    public static final String UPDATA_SEX = "sex";
    public static final String UPDATA_SCHOOL = "school";

    private static UserMessageUpdateManager mUpdateManager;

    private String mRealName;
    private String mSex;
    private String mSchoolName;
    private String mUrl;

    private UUID mUpdataRequest;

    private UserMessageUpdateManager() {
    }

    public static UserMessageUpdateManager create() {
        if (mUpdateManager == null) {
            mUpdateManager = new UserMessageUpdateManager();
        }
        return mUpdateManager;
    }

    public void initData(String realName, String sex, String schoolName, String url) {
        this.mRealName = realName;
        this.mSchoolName = schoolName;
        this.mSex = sex;
        this.mUrl = url;
    }

    public void destory() {
        this.mRealName = null;
        this.mSchoolName = null;
        this.mSex = null;
        this.mUrl = null;
        mUpdateManager = null;
        requestFinish();
    }

    public void requestFinish() {
        if (mUpdataRequest != null) {
            RequestBase.cancelRequestWithUUID(mUpdataRequest);
            mUpdataRequest = null;
        }
        UpLoadRequest.getInstense().cancle();
    }

    public void updataRealName(String realName, onRequestCallback callback) {
        startUpdataUserMessage(realName, mSex, mSchoolName, mUrl, UPDATA_NAME, callback);
    }

    public void updataSchoolName(String schoolName, onRequestCallback callback) {
        startUpdataUserMessage(mRealName, mSex, schoolName, mUrl, UPDATA_SCHOOL, callback);
    }

    public void updataSex(String sex, onRequestCallback callback) {
        startUpdataUserMessage(mRealName, sex, mSchoolName, mUrl, UPDATA_SEX, callback);
    }

    public void updataHeadimg(String localPath, onRequestCallback callback) {
        uploadHeadimg(localPath, callback);
    }

    private void uploadHeadimg(final String path, final onRequestCallback callback) {
        UpLoadRequest.getInstense().setConstantParams(new UpLoadRequest.findConstantParams() {
            @NonNull
            @Override
            public String findUpdataUrl() {
                String token= SpManager.getToken();
                return UrlRepository.getInstance().getUploadServer()+"?token="+token+"&width=110&height=110";
            }
            @Override
            public int findFileNumber() {
                return 1;
            }

            @Nullable
            @Override
            public Map<String, String> findParams() {
                return null;
            }
        }).setImgPath(new UpLoadRequest.findImgPath() {
            @NonNull
            @Override
            public String getImgPath(int position) {
                return path;
            }
        }).setListener(new UpLoadRequest.onUpLoadlistener() {
            @Override
            public void onUpLoadStart(int position, Object tag) {
                if (callback!=null){
                    callback.onHttpStart();
                }
            }

            @Override
            public void onUpLoadSuccess(int position, Object tag, String jsonString) {
                try {
                    Gson gson=new Gson();
                    HeadimgUploadBean uploadBean=gson.fromJson(jsonString,HeadimgUploadBean.class);
                    if (uploadBean!=null&&uploadBean.tplData!=null&&uploadBean.tplData.data!=null&&uploadBean.tplData.data.size()>0) {
                        startUpdataUserMessage(mRealName,mSex,mSchoolName,uploadBean.tplData.data.get(0).url,UPDATA_HEADIMG,callback);
                    }else {
                        if (callback!=null){
                            callback.onHttpFinish();
                            callback.onError(new Error("头像上传失败"));
                        }
                    }
                }catch (Exception e){
                    if (callback!=null){
                        callback.onHttpFinish();
                        callback.onError(new Error("头像上传失败"));
                    }
                }
            }

            @Override
            public void onUpLoadFailed(int position, Object tag, String failMsg) {
                if (callback!=null){
                    callback.onHttpFinish();
                    callback.onError(new Error(failMsg));
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (callback!=null){
                    callback.onHttpFinish();
                    callback.onError(new Error(errorMsg));
                }
            }
        });
    }

    private void startUpdataUserMessage(final String realName, final String sex, final String schoolName, final String url, final String updataType, final onRequestCallback callback) {
        if (callback != null) {
            callback.onHttpStart();
        }
        UserMessageUpdataRequest updataRequest = new UserMessageUpdataRequest();
        updataRequest.realName = realName;
        updataRequest.school = schoolName;
        updataRequest.sex = sex;
        updataRequest.avatar = url;
        mUpdataRequest = updataRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mUpdataRequest = null;
                String message = null;
                if (ret != null && ret.getCode() == 0) {
                    switch (updataType) {
                        case UPDATA_HEADIMG:
                            message = mUrl = url;
                            break;
                        case UPDATA_NAME:
                            message = mRealName = realName;
                            break;
                        case UPDATA_SCHOOL:
                            message = mSchoolName = schoolName;
                            break;
                        case UPDATA_SEX:
                            message = mSex = sex;
                            break;
                    }
                }
                if (callback != null) {
                    callback.onHttpFinish();
                    callback.onSuccess(ret, message, updataType);
                }
                EventBus.getDefault().post(new UserMessageChangedBean());
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mUpdataRequest = null;
                if (callback != null) {
                    callback.onHttpFinish();
                    callback.onError(error);
                }
            }
        });
    }

}
