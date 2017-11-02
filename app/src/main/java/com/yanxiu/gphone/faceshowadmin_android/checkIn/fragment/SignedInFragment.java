package com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.adapter.SignedInAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserSignInsRequest;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by fengrongcheng
 */
public class SignedInFragment extends FaceShowBaseFragment {
    public static String STATUE_CODE = "1";
    public PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SignedInAdapter mSignedInAdapter;
    private String mSignInTime = "";
    private String id = "";
    private List<GetClassUserResponse.DataBean.ElementsBean> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_signed_in);
        mPublicLoadLayout.setErrorLayoutFullScreen();
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSignedInAdapter = new SignedInAdapter();
        mRecyclerView.setAdapter(mSignedInAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                id = "";
                mSignInTime = "";
                data.clear();
                initData();
            }
        });
        return mPublicLoadLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mPublicLoadLayout.showLoadingView();
        GetClassUserSignInsRequest getClassUserSignInsRequest = new GetClassUserSignInsRequest();
        getClassUserSignInsRequest.status = STATUE_CODE;
        getClassUserSignInsRequest.stepId = "9";
        getClassUserSignInsRequest.id = id;
        getClassUserSignInsRequest.signInTime = mSignInTime;
        getClassUserSignInsRequest.startRequest(GetClassUserResponse.class, new HttpCallback<GetClassUserResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClassUserResponse ret) {
                mPublicLoadLayout.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        for (int i = 0; i < ret.getData().getCallbacks().size(); i++) {
                            if (ret.getData().getCallbacks().get(i).getCallbackParam().endsWith("signinTime")) {
                                mSignInTime = String.valueOf(ret.getData().getCallbacks().get(i).getCallbackValue());
                            }
                            if (ret.getData().getCallbacks().get(i).getCallbackParam().endsWith("id")) {
                                id = String.valueOf(ret.getData().getCallbacks().get(i).getCallbackValue());
                            }
                        }
                        data.addAll(ret.getData().getElements());
                        mSignedInAdapter.update(ret.getData().getElements());
                    } else {
                        if (data.size() == 0) {
                            mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                        } else {
                            ToastUtil.showToast(getActivity(), ret.getMessage());
                        }
                    }
                } else {
                    if (data.size() == 0) {
                        mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                    } else {
                        ToastUtil.showToast(getActivity(), ret.getMessage());
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();
                if (data.size() == 0) {
                    mPublicLoadLayout.showNetErrorView();
                } else {
                    ToastUtil.showToast(getActivity().getApplicationContext(), error.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
