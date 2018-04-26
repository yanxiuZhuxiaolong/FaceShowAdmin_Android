package com.yanxiu.gphone.faceshowadmin_android.net.base;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.utils.NetWorkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 在Cookie中添加数据
 *
 * @author frc
 *         created at 17-6-2.
 */

public abstract class RequestWithCookie extends RequestBase {

    public HashMap<String, String> cookies;
    public String domain = "main.zgjiaoyan.com";

    @Override
    protected boolean shouldLog() {
        return false;
    }


    @Override
    public <T> UUID startRequest(final Class<T> clazz, final HttpCallback<T> callback) {
        if (!NetWorkUtils.isNetworkAvailable(FSAApplication.getInstance().getApplicationContext())) {
            callback.onFail(this, new Error(ResponseConfig.NET_ERROR));
            return null;
        }

        HttpCallback<T> baseCallback = new HttpCallback<T>() {
            @Override
            public void onSuccess(RequestBase request, T ret) {
                FaceShowBaseResponse response = (FaceShowBaseResponse) ret;
                if (response != null) {
                    callback.onSuccess(request, ret);
                } else {
                    Error error = new Error("数据解析异常");
                    onFail(request, error);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                callback.onFail(request, error);
            }
        };

        return super.startRequest(clazz, baseCallback);
    }

    //TODO
//    @Override
//    protected OkHttpClient setClient() {
//
//
//        return addCookieByInterceptor();
//    }

    private OkHttpClient addCookieByInterceptor() {
         /*添加Cookie*/
        return client = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();
                        String cookieString = "";
                        for (Map.Entry<String, String> entry : cookies.entrySet()) {
                            cookieString = cookieString + "" + entry.getKey() + "=" + entry.getValue() + ";";
                        }
                        final Request authorized = original.newBuilder().addHeader("Cookie",
                                cookieString).build();
                        return chain.proceed(authorized);
                    }
                }).build();

    }

    private OkHttpClient addCookieByCookieJar() {
        return client = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        final ArrayList<Cookie> cookieList = new ArrayList<>();
                        for (Map.Entry<String, String> entry : cookies.entrySet()) {
                            cookieList.add(createCookie(entry.getKey(), entry.getValue()));
                        }
                        return cookieList;
                    }
                }).build();
    }

    private Cookie createCookie(String name, String value) {
        return new Cookie.Builder()
                .domain(domain)
                .path("/")
                .name(name)
                .value(value)
                .httpOnly()
                .secure()
                .build();
    }
}
