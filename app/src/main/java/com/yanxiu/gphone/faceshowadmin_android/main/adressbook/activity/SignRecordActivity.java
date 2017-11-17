package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.adapter.SignRecordAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.SignRecordRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.SignRecordResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.SupplementalSignInResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:52.
 * Function :
 */
public class SignRecordActivity extends FaceShowBaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SignRecordAdapter.onSignClickListener {

    private static final String KEY_USERID = "key_userid";
    private static final String KEY_USERNAME = "key_userName";

    private Context mContext;
    private PublicLoadLayout rootView;

    private String mUserId;
    private String mUserName;
    private View mBackView;
    private TextView mTitleView;
    private SwipeRefreshLayout mRefreshView;
    private LoadMoreRecyclerView mSignRecordView;
    private SignRecordAdapter mAdapter;
    private TimePickerView mTimePickerView;
    private String mSupplementalSignInTime;

    private UUID mRecordRequest;
    private UUID mSupplementalSignInRequest;

    public static void LuanchActivity(Context context, String userId, String userName) {
        Intent intent = new Intent(context, SignRecordActivity.class);
        intent.putExtra(KEY_USERID, userId);
        intent.putExtra(KEY_USERNAME, userName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_signrecord);
        setContentView(rootView);
        mUserId = getIntent().getStringExtra(KEY_USERID);
        mUserName = getIntent().getStringExtra(KEY_USERNAME);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSupplementalSignInRequest!=null){
            RequestBase.cancelRequestWithUUID(mSupplementalSignInRequest);
            mSupplementalSignInRequest=null;
        }
        if (mRecordRequest!=null){
            RequestBase.cancelRequestWithUUID(mRecordRequest);
            mRecordRequest=null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);

        mRefreshView = findViewById(R.id.sw_refresh);
        mSignRecordView = findViewById(R.id.recy_signrecord);
        mSignRecordView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SignRecordAdapter(mContext);
        mSignRecordView.setAdapter(mAdapter);
    }

    private void listener() {
        mRefreshView.setOnRefreshListener(this);
        mBackView.setOnClickListener(this);
        mAdapter.setSignClickListener(this);
        rootView.setRetryButtonOnclickListener(this);
    }

    private void initData() {
        mTitleView.setText("签到记录");
        mSignRecordView.setLoadMoreEnable(false);
        onRefresh();
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.setRefreshing(true);
            }
        });
    }

    private void startSignRecordRequest() {
//        rootView.showLoadingView();
        rootView.hiddenNetErrorView();
        SignRecordRequest recordRequest = new SignRecordRequest();
        recordRequest.userId = mUserId;
        mRecordRequest=recordRequest.startRequest(SignRecordResponse.class, new HttpCallback<SignRecordResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignRecordResponse ret) {
//                rootView.hiddenLoadingView();
                mRecordRequest=null;
                mRefreshView.setRefreshing(false);
                if (ret != null && ret.data != null && ret.getCode() == 0) {
                    mAdapter.setData(ret.data.signIns);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
//                rootView.hiddenLoadingView();
                mRecordRequest=null;
                if (mAdapter.getItemCount() == 0) {
                    rootView.showNetErrorView();
                }
                mRefreshView.setRefreshing(false);
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    private void supplementalSignIn(final int position, String stepId) {
        rootView.showLoadingView();
        SupplementalSignInRequest supplementalSignInRequest = new SupplementalSignInRequest();
        supplementalSignInRequest.signInTime = mSupplementalSignInTime;
        supplementalSignInRequest.stepId = stepId;
        supplementalSignInRequest.userId = mUserId;
        mSupplementalSignInRequest=supplementalSignInRequest.startRequest(SupplementalSignInResponse.class, new HttpCallback<SupplementalSignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SupplementalSignInResponse ret) {
                mSupplementalSignInRequest=null;
                rootView.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    mTimePickerView.dismiss();
                    mAdapter.setSignSuccess(position);
                } else {
                    ToastUtil.showToast(mContext, ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mSupplementalSignInRequest=null;
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.retry_button:
                onRefresh();
                mRefreshView.setRefreshing(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        startSignRecordRequest();
    }

    @Override
    public void onSignClick(int position, SignRecordResponse.SignRecordData.SignIns signIns) {
        showTimePickerView(position, String.valueOf(signIns.stepId));
    }

    private void showTimePickerView(final int position, final String stepId) {
        Calendar selectedDate = Calendar.getInstance();
        mTimePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mSupplementalSignInTime = getTime(date);
            }
        })
                .setDate(selectedDate)
                .setLayoutRes(R.layout.time_picker_layout, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        ((TextView) v.findViewById(R.id.signature_member_name)).setText(mUserName);
                        v.findViewById(R.id.submit_a_supplement).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EventUpdate.onStudentRetroactive(mContext);
                                mTimePickerView.returnData();
                                supplementalSignIn(position, stepId);
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(false)
                .build();
        mTimePickerView.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

}
