package com.test.yanxiu.im_core;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cailei on 05/03/2018.
 * 用于顺序执行添加进来的Requests
 */

public class RequestQueueHelper {
    public class Item<T> {
        RequestBase request;
        Class<T> clazz;
        HttpCallback<T> callback;
    }

    private List<Object> items = new ArrayList<>();
    private boolean hasOngoingRequest = false;
    private RequestBase ongoingRequest;
    private boolean bPaused;

    public <T> void addRequest(RequestBase request, final Class<T> clazz, final HttpCallback<T> callback) {
        HttpCallback<T> queueCallback = new HttpCallback<T>() {
            @Override
            public void onSuccess(RequestBase request, T ret) {
                hasOngoingRequest = false;
                ongoingRequest = null;
                removeFromQueue(request);
                callback.onSuccess(request, ret);
                doNextRequest();
            }

            @Override
            public void onFail(RequestBase request, Error error) {

                hasOngoingRequest = false;
                ongoingRequest = null;
                removeFromQueue(request);
                callback.onFail(request, error);    // 如果需要失败重试，则外部在onfail时重新加queue到index 0即可
                doNextRequest();
            }
        };

        Item<T> item = new Item<>();
        item.request = request;
        item.clazz = clazz;
        item.callback = queueCallback;
        items.add(item);
        doNextRequest();
    }

    private void removeFromQueue(RequestBase request) {
        for (Object item : items) {
            final Item<Object> i = (Item<Object>) item;
            if (i.request == request) {
                items.remove(i);
                break;
            }
        }
    }

    private void doNextRequest() {
        if (bPaused) {
            return;
        }

        if (hasOngoingRequest) {
            return;
        }
        if (items.size() == 0) {
            return;
        }

        final Item<Object> item = (Item<Object>) items.get(0);
        hasOngoingRequest = true;
        item.request.startRequest(item.clazz, item.callback);
        ongoingRequest = item.request;
    }

    public void setPause(boolean pause) {
        bPaused = true;
        if (ongoingRequest != null) {
            ongoingRequest.cancelRequest();
        }
    }

    public void setResume(boolean resume) {
        bPaused = false;
        doNextRequest();
    }
}
