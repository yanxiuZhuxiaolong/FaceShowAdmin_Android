package com.yanxiu.gphone.faceshowadmin_android.task.fragment;

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
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetClazsUserQuestionRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetClazsUserQuestionResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.adapter.NoSubmitAdapter;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by fengrongcheng
 */
public class NoSubmitFragment extends FaceShowBaseFragment {
    public static String STATUE_CODE = "0";
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private NoSubmitAdapter mNoSubmitAdapter;
    private String id = "";
    private List<GetClazsUserQuestionResponse.ElementsBean> data = new ArrayList<>();
    private boolean isResume = false;
    private UUID mGetClassUserSignInsRequestUUID;
    public boolean mNeedToRefreshParentActivity = false;


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            id = "";
            data.clear();
            initData();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_no_sign_in);
        mPublicLoadLayout.setErrorLayoutFullScreen();
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNoSubmitAdapter = new NoSubmitAdapter();
        mRecyclerView.setAdapter(mNoSubmitAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mOnRefreshListener.onRefresh();
        return mPublicLoadLayout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResume) {
            mOnRefreshListener.onRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isResume = false;
    }

    private void initData() {
        GetClazsUserQuestionRequest getClazsUserQuestionRquest = new GetClazsUserQuestionRequest();
        getClazsUserQuestionRquest.status = STATUE_CODE;
        getClazsUserQuestionRquest.stepId = getArguments().getString("stepId");
        getClazsUserQuestionRquest.id = id;
        mGetClassUserSignInsRequestUUID = getClazsUserQuestionRquest.startRequest(GetClazsUserQuestionResponse.class, new HttpCallback<GetClazsUserQuestionResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClazsUserQuestionResponse ret) {
                mPublicLoadLayout.hiddenLoadingView();
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                            for (int i = 0; i < ret.getData().getCallbacks().size(); i++) {
                                if (ret.getData().getCallbacks().get(i).getCallbackParam().endsWith("id")) {
                                    id = String.valueOf(ret.getData().getCallbacks().get(i).getCallbackValue());
                                }
                            }
                        }

                        mPublicLoadLayout.hiddenOtherErrorView();
                        mPublicLoadLayout.hiddenNetErrorView();
                        data.addAll(ret.getData().getElements());
                        mNoSubmitAdapter.update(data);
                } else {
                    if (data.size() == 0) {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.no_no_submit_data));
                    } else {
                        ToastUtil.showToast(getActivity(), ret.getMessage());
                    }
                }
                    } else {
                        if (data.size() == 0) {
                        mPublicLoadLayout.showOtherErrorView(ret.getError().getMessage());
                    } else {
                        ToastUtil.showToast(getActivity(), ret.getError().getMessage());
                        }


                    }
                }


                @Override
                public void onFail (RequestBase request, Error error){
                    mPublicLoadLayout.hiddenLoadingView();
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    if (data.size() == 0) {
                        mPublicLoadLayout.showNetErrorView();
                    } else {
                        ToastUtil.showToast(getActivity(), error.getMessage());
                    }
                }
            });
        }

        @Override
        public void onDestroyView () {
            super.onDestroyView();
            unbinder.unbind();
            if (mGetClassUserSignInsRequestUUID != null) {
                RequestBase.cancelRequestWithUUID(mGetClassUserSignInsRequestUUID);
            }
            mNeedToRefreshParentActivity = false;
        }
    }
