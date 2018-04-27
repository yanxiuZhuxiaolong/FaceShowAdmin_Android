package com.test.yanxiu.im_ui.contacts.view;

import android.view.View;

import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;

import java.util.List;

/**
 * Created by frc on 2018/3/13.
 */

public interface IContactsView {

    void addCallBack();

    void showItemClickResult(ContactsPlayerBean memberInfo);

    void showQueryResultList(List<ContactsPlayerBean> data);

    void showContractsList(List<ContactsPlayerBean> data);

    void showChangeClassWindow(List<ClassBean> data, int currentClassPosition);

    void hideChangeClassWindow();

    void changeCurrentClassName(String className);

    void showLoadingView();

    void hideLoadingView();

    void showDataErrorView(String errorMessage);

    void showNoDataView();

    void showNetError();

    void showToast(String message);

}
