package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/26 9:23.
 * Function :
 */
public class UpLoadRequest {

    public interface findConstantParams {
        @NonNull
        String findUpdataUrl();

        int findFileNumber();

        @Nullable
        Map<String, String> findParams();
    }

    public interface findImgPath {
        @NonNull
        String getImgPath(int position);
    }

    public interface findImgTag {
        @Nullable
        Object getImgTag(int position);
    }

    public interface onUpLoadlistener {
        void onUpLoadStart(int position, Object tag);

        void onUpLoadSuccess(int position, Object tag, String jsonString);

        void onUpLoadFailed(int position, Object tag, String failMsg);

        void onError(String errorMsg);
    }

    public interface onProgressListener {
        void onRequestStart();

        void onProgress(int index, int position);

        void onRequestEnd();
    }

    private static final String IMGKEY = "easyfile";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int totalCount = 0;
    private int index = 0;

    private static UpLoadRequest mUpDataRequest;
    private final OkHttpClient mOkHttpClient;

    private findConstantParams mFindConstantParams;
    private findImgPath mFindImgPath;
    private findImgTag mFindImgTag;
    private onProgressListener mProgressListener;

    private List<Call> mCallList=new ArrayList<>();

    public static UpLoadRequest getInstense() {
        if (mUpDataRequest == null) {
            mUpDataRequest = new UpLoadRequest();
        }
        return mUpDataRequest;
    }

    public void cancle(){
        for (Call call:mCallList){
            call.cancel();
        }
        mCallList.clear();
    }

    private UpLoadRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public UpLoadRequest setConstantParams(findConstantParams findConstantParams) {
        this.mFindConstantParams = findConstantParams;
        return mUpDataRequest;
    }

    public UpLoadRequest setImgPath(@NonNull findImgPath findImgPath) {
        this.mFindImgPath = findImgPath;
        return mUpDataRequest;
    }

    public UpLoadRequest setTag(@Nullable findImgTag findImgTag) {
        this.mFindImgTag = findImgTag;
        return mUpDataRequest;
    }

    public UpLoadRequest setProgressListener(onProgressListener progressListener) {
        this.mProgressListener = progressListener;
        return mUpDataRequest;
    }

    public void setListener(@Nullable onUpLoadlistener upDatalistener) {
        int fileNumber;
        String url;
        Map<String, String> params;
        if (mFindConstantParams != null) {
            fileNumber = mFindConstantParams.findFileNumber();
            url = mFindConstantParams.findUpdataUrl();
            params = mFindConstantParams.findParams();
        } else {
            if (upDatalistener != null) {
                upDatalistener.onError("can not find constant params");
            }
            return;
        }
        index = 0;
        totalCount = fileNumber;
        if (mProgressListener != null) {
            mProgressListener.onRequestStart();
        }
        if (totalCount==0){
            postMainThread(new Runnable() {
                @Override
                public void run() {
                    mProgressListener.onRequestEnd();
                }
            });
        }else {
            for (int i = 0; i < fileNumber; i++) {
                upData(url, i, params, upDatalistener);
            }
        }
    }

    private void postMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    private void upData(final String url, final int position, final Map<String, String> params, final onUpLoadlistener upDatalistener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        final String imgPath;
        if (mFindImgPath != null) {
            imgPath = mFindImgPath.getImgPath(position);
        } else {
            if (upDatalistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDatalistener.onError("Can not find img path ");
                    }
                });
            }
            return;
        }

        if (TextUtils.isEmpty(imgPath)) {
            if (upDatalistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDatalistener.onError("The imgPath can not be NULL");
                    }
                });
            }
            return;
        }
        File f = new File(imgPath);
        builder.addFormDataPart(IMGKEY, f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));

        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        if (TextUtils.isEmpty(url)) {
            if (upDatalistener != null) {
                postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        upDatalistener.onError("The url can not be NULL");
                    }
                });
            }
            return;
        }
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        if (upDatalistener != null) {
            postMainThread(new Runnable() {
                @Override
                public void run() {
                    upDatalistener.onUpLoadStart(position, imgPath);
                }
            });
        }
        Call call=mOkHttpClient.newCall(request);
        mCallList.add(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallList.remove(call);
                if (mProgressListener != null) {
                    index++;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressListener.onProgress(index,position);
                        }
                    });
                }
                if (upDatalistener != null) {
                    final String message = e.getMessage();
                    Object tag = null;
                    if (mFindImgTag != null) {
                        tag = mFindImgTag.getImgTag(position);
                    }
                    final Object finalTag = tag;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            upDatalistener.onUpLoadFailed(position, finalTag, message);
                        }
                    });
                }
                if (index == totalCount) {
                    if (mProgressListener != null) {
                        postMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressListener.onRequestEnd();
                            }
                        });
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mCallList.remove(call);
                if (mProgressListener != null) {
                    index++;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressListener.onProgress(index,position);
                        }
                    });
                }
                if (upDatalistener != null) {
                    final String jsonString = response.body().string();
                    Object tag = null;
                    if (mFindImgTag != null) {
                        tag = mFindImgTag.getImgTag(position);
                    }
                    final Object finalTag = tag;
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            upDatalistener.onUpLoadSuccess(position, finalTag, jsonString);
                        }
                    });
                }
                if (index == totalCount) {
                    if (mProgressListener != null) {
                        postMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressListener.onRequestEnd();
                            }
                        });
                    }
                }
            }
        });
    }

}
