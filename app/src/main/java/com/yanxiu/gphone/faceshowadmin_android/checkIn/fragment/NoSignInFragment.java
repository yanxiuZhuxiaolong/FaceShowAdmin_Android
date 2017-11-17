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
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.adapter.NoSignInAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserSignInsRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdata;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by fengrongcheng
 */
public class NoSignInFragment extends FaceShowBaseFragment {
    public static String STATUE_CODE = "0";
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private NoSignInAdapter mNoSignInAdapter;
    private String mSignInTime = "";
    private String id = "";
    private List<GetClassUserResponse.DataBean.ElementsBean> data = new ArrayList<>();
    private boolean isResume = false;
    private UUID mGetClassUserSignInsRequestUUID;
    public boolean mNeedToRefreshParentActivity = false;


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            id = "";
            mSignInTime = "";
            data.clear();
            initData();
        }
    };

    public void toRefresh() {
        id = "";
        mSignInTime = "";
        data.clear();
        mPublicLoadLayout.showLoadingView();
        initData();
    }

    private RecyclerViewItemClickListener mRecyclerViewItemClickListener = new RecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            EventUpdata.onSignInDetailRetroactive(getContext());
            toShowTimePickerView(data.get(position).getUserName(), position);
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
        mNoSignInAdapter = new NoSignInAdapter();
        mRecyclerView.setAdapter(mNoSignInAdapter);
        mNoSignInAdapter.addItemClickListener(mRecyclerViewItemClickListener);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mPublicLoadLayout.showLoadingView();
        initData();
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

    private String supplementalSignInTime;
    TimePickerView timePickerView;

    private void toShowTimePickerView(final String userName, final int position) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        timePickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                supplementalSignInTime = getTime(date);
            }
        })
                .setDate(selectedDate)
                .setLayoutRes(R.layout.time_picker_layout, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        ((TextView) v.findViewById(R.id.signature_member_name)).setText(userName);
                        v.findViewById(R.id.submit_a_supplement).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timePickerView.returnData();
                                supplementalSignIn(position);
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        timePickerView.show();

    }

    private void supplementalSignIn(int position) {
        mPublicLoadLayout.showLoadingView();
        SupplementalSignInRequest supplementalSignInRequest = new SupplementalSignInRequest();
        supplementalSignInRequest.signInTime = supplementalSignInTime;
        supplementalSignInRequest.stepId = getArguments().getString("stepId");
        supplementalSignInRequest.userId = String.valueOf(data.get(position).getUserId());
        supplementalSignInRequest.startRequest(SupplementalSignInResponse.class, new HttpCallback<SupplementalSignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SupplementalSignInResponse ret) {
                mPublicLoadLayout.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    timePickerView.dismiss();
                    mNeedToRefreshParentActivity = true;
                    ((CheckInDetailActivity) getActivity()).getCheckInDetail();
                    mOnRefreshListener.onRefresh();
                } else {
                    ToastUtil.showToast(getActivity(), ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getActivity(), error.getMessage());
            }
        });
    }

    private void initData() {
        GetClassUserSignInsRequest getClassUserSignInsRequest = new GetClassUserSignInsRequest();
        getClassUserSignInsRequest.status = STATUE_CODE;
        getClassUserSignInsRequest.stepId = getArguments().getString("stepId");
        getClassUserSignInsRequest.id = id;
        getClassUserSignInsRequest.signInTime = mSignInTime;
        mGetClassUserSignInsRequestUUID = getClassUserSignInsRequest.startRequest(GetClassUserResponse.class, new HttpCallback<GetClassUserResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClassUserResponse ret) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
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
                        mNoSignInAdapter.update(ret.getData().getElements());
                    } else {
                        if (data.size() == 0) {
                            mPublicLoadLayout.showOtherErrorView(getString(R.string.no_no_sign_in_data));
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

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mGetClassUserSignInsRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetClassUserSignInsRequestUUID);
        }
        mNeedToRefreshParentActivity = false;
    }
}
