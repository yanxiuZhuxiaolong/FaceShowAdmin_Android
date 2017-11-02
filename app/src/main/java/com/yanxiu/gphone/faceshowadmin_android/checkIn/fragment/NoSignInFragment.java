package com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.yanxiu.gphone.faceshowadmin_android.checkIn.adapter.NoSignInAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserSignInsRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private NoSignInAdapter mNoSignInAdapter;
    private String mSignInTime = "";
    private String id = "";

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
        mNoSignInAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                toShowTimePickerView(data.get(position).getUserName(), position);
            }
        });
        return mPublicLoadLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private String supplementalSignInTime;
    TimePickerView timePickerView;

    private void toShowTimePickerView(final String userName, final int position) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        timePickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                supplementalSignInTime = getTime(date);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_picker_layout, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        ((TextView) v.findViewById(R.id.signature_member_name)).setText(userName);
                        v.findViewById(R.id.submit_a_supplement).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supplementalSignIn(position);
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
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
                } else {

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();

            }
        });
    }

    private List<GetClassUserResponse.DataBean.ElementsBean> data = new ArrayList<>();

    private void initData() {
        mPublicLoadLayout.showLoadingView();
        GetClassUserSignInsRequest getClassUserSignInsRequest = new GetClassUserSignInsRequest();
        getClassUserSignInsRequest.status = STATUE_CODE;
        getClassUserSignInsRequest.stepId = getArguments().getString("stepId");
        getClassUserSignInsRequest.id = id;
        getClassUserSignInsRequest.signInTime = mSignInTime;
        getClassUserSignInsRequest.startRequest(GetClassUserResponse.class, new HttpCallback<GetClassUserResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClassUserResponse ret) {
                mPublicLoadLayout.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        data = ret.getData().getElements();
                        mNoSignInAdapter.update(ret.getData().getElements());
                    } else {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.data_empty));
                    }
                } else {
                    mPublicLoadLayout.showOtherErrorView(getString(R.string.data_error));

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();
                mPublicLoadLayout.showNetErrorView();
            }
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
