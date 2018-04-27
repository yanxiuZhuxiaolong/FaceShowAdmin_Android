package com.test.yanxiu.im_core.http;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import java.util.UUID;

/**
 * Created by cailei on 02/03/2018.
 */
public class ImRequestBase extends RequestBase {
    public String bizSource;  // 来源，移动端用1
    public String bizId;      // 业务id，研修宝用1
    public String bizToken;         // App用的Token
    public String imToken;          // 专门为im用的Token
    public String reqId;         // 客户端生成的，保证唯一性的32位uuid

    ImRequestBase() {
        bizSource = "22";
        bizId = null;
        reqId = UUID.randomUUID().toString();
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getImServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String fullUrl() throws NullPointerException, IllegalAccessException, IllegalArgumentException {
        String url = super.fullUrl();
        SrtLogger.log("im http", url);
        return url;
    }

    @Override
    public <T> UUID startRequest(Class<T> clazz, final HttpCallback<T> callback) {
        return super.startRequest(clazz, new HttpCallback<T>() {
            @Override
            public void onSuccess(RequestBase request, T ret) {
                ImResponseBase retbase = (ImResponseBase)ret;
                if (retbase.code != 0) {
                    callback.onFail(request, new Error(((ImResponseBase) ret).message));
                    return;
                }

                callback.onSuccess(request, ret);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                callback.onFail(request, error);
            }
        });
    }
}
