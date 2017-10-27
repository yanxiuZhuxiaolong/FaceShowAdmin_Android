package com.yanxiu.gphone.faceshowadmin_android.net.base;

import android.os.Handler;
import android.os.Message;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.NetWorkUtils;

import java.util.Random;
import java.util.UUID;

/**
 * 使用mock数据时使用的接口
 *
 * @author frc
 *         created at 17-5-22.
 */

public abstract class FaceShowMockRequest extends FaceShowBaseRequest {


    @Override
    public <T> UUID startRequest(final Class<T> clazz, final HttpCallback<T> callback) {
        final RequestBase request = this;

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!NetWorkUtils.isNetworkAvailable(FSAApplication.getInstance().getApplicationContext())) {
                    callback.onFail(request, new Error(ResponseConfig.NET_ERROR));
                } else {
                    try {
                        String url = fullUrl();
                    } catch (IllegalAccessException e) {
                    }
                    String json = FileUtils.getFromAssets(FSAApplication.getInstance().getApplicationContext(), getMockDataPath());
                    final T ret = RequestBase.gson.fromJson(json, clazz);
                    final FaceShowBaseResponse response = (FaceShowBaseResponse) ret;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Random random = new Random();
                            int errorMax = 0;
                            if (setMockRequestErrorProbability() <= 1 && setMockRequestErrorProbability() >= 0) {
                                errorMax = (int) (100 * setMockRequestErrorProbability());
                            } else {
                                throw new Error("setMockRequestErrorProbability can not < 0 || >1");
                            }
                            if (random.nextInt(100) < errorMax) {
                                callback.onFail(request, new Error("data error"));
                            } else {
//                                if (response.getStatus().getCode() == 0) {
                                callback.onSuccess(request, ret);
//                                } else {
//                                    callback.onFail(request, new Error(response.getStatus().getCode() + ""));
//                                }
                            }
                        }

                    });
                }
            }
        };

        int delayTime = getDelayTime();
        if (setMockDelayTime() > 10 || setMockDelayTime() < 0) {
            throw new Error("delayTime is 0~10");
        }
        delayTime = setMockDelayTime() * delayTime;
        handler.sendEmptyMessageDelayed(0,

                delayTime);
        return null;
    }

    /**
     * 设置mok接口反应时间
     * cwq
     */
    protected int getDelayTime() {
        final Random random = new Random();
        return random.nextInt(1000);
    }

    ;

    protected abstract String getMockDataPath();

    /**
     * @return 模拟网络请求的ConnectTime   数值在0~10 之间越大时间越长
     */
    protected int setMockDelayTime() {
        return 2;
    }

    /**
     * 设置网络请求失败的概率
     **/
    protected double setMockRequestErrorProbability() {
        return 0.2;
    }
}
