package com.test.yanxiu.im_ui.contacts.presenter;

import android.text.TextUtils;

import com.test.yanxiu.im_core.http.GetContactsRequest;
import com.test.yanxiu.im_core.http.GetContactsResponse;
import com.test.yanxiu.im_ui.Constants;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.model.ContactsModel;
import com.test.yanxiu.im_ui.contacts.view.IContactsView;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import java.util.List;


/**
 * 针对通讯录的presenter层
 *
 * @author by frc on 2018/3/13.
 */

public class ContactsPresenter<V extends IContactsView> extends BasePresenter<V> {

    private IContactsView view;
    private ContactsModel model = new ContactsModel();

    public ContactsPresenter(IContactsView view) {
        this.view = view;

    }


    public void loadDataByNet() {
        view.showLoadingView();
        final GetContactsRequest getContactsRequest = new GetContactsRequest();
        getContactsRequest.imToken = Constants.imToken;
        getContactsRequest.startRequest(GetContactsResponse.class, new HttpCallback<GetContactsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetContactsResponse ret) {

                if (ret.code == 0) {
                    if (ret.getData().getContacts() == null && ret.getData().getContacts().getGroups() == null || ret.getData().getContacts().getGroups().size() < 1) {
                        if (getClassList() != null && getClassList().size() > 0) {
                            view.showToast("数据更新失败");
                            getContactsList(null);
                        } else {
                            view.showNoDataView();
                        }
                    } else {
                        model.setData(ret);
                        getContactsList(null);
                    }

                } else {
                    if (getClassList() != null && getClassList().size() > 0) {
                        view.showToast(ret.error.message);
                        getContactsList(null);
                    } else {
                        view.showDataErrorView(ret.error.message);
                    }

                }
                view.hideLoadingView();


            }

            @Override
            public void onFail(RequestBase request, Error error) {
                view.hideLoadingView();
                if (getClassList() != null && getClassList().size() > 0) {
                    view.showToast("网络异常");
                    getContactsList(null);
                } else {
                    view.showNetError();
                }

            }
        });
    }

    /**
     * 根据班级获取对应的班级通讯录
     *
     * @param classBean 班级对象
     */
    public void getContactsList(ClassBean classBean) {
        if (null == classBean) {
            //初次加载数据  默认显示第一个班级的通讯录信息
            List<ClassBean> classBeans = model.getClassListData();
            ClassBean firstClass = classBeans.get(0);
            view.changeCurrentClassName(firstClass.getClassName());
            view.showContractsList(model.getPlayersDataByClass(firstClass));
        } else {
            view.showContractsList(model.getPlayersDataByClass(classBean));
        }
    }

    /**
     * 根据关键字进行模糊搜索
     *
     * @param key 关键字
     */
    public void queryPlayer(String key) {
        if (TextUtils.isEmpty(key)) {
            model.clearQueryKey();
            view.showQueryResultList(model.getCurrentClassPlayerList());
        } else {
            view.showQueryResultList(model.getQueryResult(key));
        }
    }

    /**
     * 显示切换班级列表
     */
    public void showChangeClassPopupWindow() {
        view.showChangeClassWindow(getClassList(), model.getCurrentClassPosition());
    }

    /**
     * 切换班级
     *
     * @param position 班级index
     */
    public void changeClass(int position) {
        model.setCurrentClassPosition(position);
        ClassBean currentClass = model.getClassListData().get(position);
        view.changeCurrentClassName(currentClass.getClassName());
        view.showContractsList(model.getPlayersDataByClass(currentClass));
    }

    private List<ClassBean> getClassList() {

        return model.getClassListData();
    }

    public void getItemData(int position) {
        view.showItemClickResult(model.getCurrentClassPlayerList().get(position));
    }


}
