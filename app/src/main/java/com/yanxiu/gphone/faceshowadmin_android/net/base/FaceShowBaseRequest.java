package com.yanxiu.gphone.faceshowadmin_android.net.base;

import android.util.Log;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.envconfig.UrlRepository;

import java.util.Map;

import okhttp3.HttpUrl;


/**
 * requset基类
 */

public abstract class FaceShowBaseRequest extends RequestBase {
    // TODO 这里的参数，需要根据server的协议来定，目前都是易学易练的公用参数
    public String osType = Constants.osType;
    public String pcode = Constants.pcode;
    public String token = SpManager.getToken();
    public String trace_uid;
    public String version = Constants.version;


    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected boolean shouldLog() {
        return true;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrace_uid() {
        return trace_uid;
    }

    public void setTrace_uid(String trace_uid) {
        this.trace_uid = trace_uid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    protected String fullUrl() throws NullPointerException, IllegalAccessException, IllegalArgumentException {
            String server = urlServer();
            String path = urlPath();

            if (server == null) {
                throw new NullPointerException();
            }

            server = omitSlash(server);
            path = omitSlash(path);

            if (!urlServer().substring(0, 4).equals("http")) {
                server = "http://" + urlServer();
            }

            String fullUrl = server;
            if (path != null) {
                fullUrl = fullUrl + "/" + path;
            }

            HttpUrl.Builder urlBuilder = HttpUrl.parse(fullUrl).newBuilder();

            Map<String, Object> params = urlParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                try {
                    Object value = entry.getValue();
                    if (!(value instanceof String)) {
                        value = gson.toJson(entry.getValue());
                    }
                    urlBuilder.addQueryParameter(entry.getKey(), (String) value);
                } catch (Exception e) {
                }

            }
            fullUrl = urlBuilder.build().toString();
            return fullUrl;
    }
}
