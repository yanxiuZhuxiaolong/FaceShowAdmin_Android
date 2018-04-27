package com.test.yanxiu.im_ui.contacts.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by frc on 2018/3/16.
 */

public class BasePresenter<T> {
    protected WeakReference<T> mViewRef;

    //进行绑定
    public void attachView(T view) {
        this.mViewRef = new WeakReference<T>(view);
    }

    //进行解绑
    public void detachView() {
        mViewRef.clear();
    }
}
