package com.test.yanxiu.im_ui.contacts.view;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_ui.contacts.presenter.BasePresenter;

/**
 * Created by frc on 2018/3/16.
 */

public abstract class ContactMvpBaseFragment<V, P extends BasePresenter<V>> extends FaceShowBaseFragment {
    public P presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V) this);
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
