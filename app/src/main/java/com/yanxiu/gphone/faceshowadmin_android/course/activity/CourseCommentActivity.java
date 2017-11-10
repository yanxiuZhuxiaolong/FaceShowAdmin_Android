package com.yanxiu.gphone.faceshowadmin_android.course.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CourseCommentAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseReplyRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseReplyResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class CourseCommentActivity extends FaceShowBaseActivity {

    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    CourseCommentAdapter mCourseCommentAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private UUID mGetCourseReplyRequestUUID;
    private UUID mGetCourseCommentRecordsRequestUUID;
    private String mStepId;
    private String mDescription;
    private String id = "";
    private List<GetCourseCommentRecordsResponse.ElementsBean> mRecords = new ArrayList<>();
    private boolean mRefresh = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_relpy_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.reply);

        mStepId = getIntent().getStringExtra("stepId");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                id = "";
                mRefresh = true;
                getCommentRecords();
            }
        });
        getCommentTitle();
    }

    private void getCommentRecords() {
        GetCourseCommentRecordsRequest getCourseCommentRecordsRequest = new GetCourseCommentRecordsRequest();
        getCourseCommentRecordsRequest.stepId = mStepId;
        getCourseCommentRecordsRequest.id = id;
        getCourseCommentRecordsRequest.limit = "20";
        getCourseCommentRecordsRequest.order = "desc";
        mGetCourseCommentRecordsRequestUUID = getCourseCommentRecordsRequest.startRequest(GetCourseCommentRecordsResponse.class, new HttpCallback<GetCourseCommentRecordsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseCommentRecordsResponse ret) {
                mPublicLoadLayout.hiddenOtherErrorView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ResponseConfig.SUCCESS == ret.getCode()) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        if (mRefresh) {
                            mRecords.clear();
                        }
                        id = String.valueOf(ret.getData().getCallbackValue());
                        mRecords.addAll(ret.getData().getElements());
                        if (mCourseCommentAdapter == null) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CourseCommentActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mCourseCommentAdapter = new CourseCommentAdapter(mRecords, mDescription, String.valueOf(ret.getData().getTotalElements()));
                            mRecyclerView.setAdapter(mCourseCommentAdapter);
                        } else {
                            mCourseCommentAdapter.update(mRecords, mDescription, String.valueOf(ret.getData().getTotalElements()));
                        }
                    } else {
                        if (TextUtils.isEmpty(id)) {
                            mPublicLoadLayout.showOtherErrorView(getString(R.string.no_reply_record));
                        } else {
                            ToastUtil.showToast(getApplicationContext(), R.string.no_more_reply_record);
                        }
                    }

                } else {
                    if (TextUtils.isEmpty(id)) {
                        mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                    } else {
                        ToastUtil.showToast(getApplicationContext(), ret.getMessage());
                    }
                }
                mRefresh = false;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRefresh = false;
                mPublicLoadLayout.hiddenOtherErrorView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (TextUtils.isEmpty(id)) {
                    mPublicLoadLayout.showOtherErrorView(error.getMessage());
                } else {
                    ToastUtil.showToast(getApplicationContext(), error.getMessage());
                }
            }
        });
    }

    private void getCommentTitle() {
        mPublicLoadLayout.showLoadingView();
        GetCourseReplyRequest getCourseReplyRequest = new GetCourseReplyRequest();
        getCourseReplyRequest.stepId = mStepId;
        mGetCourseReplyRequestUUID = getCourseReplyRequest.startRequest(GetCourseReplyResponse.class, new HttpCallback<GetCourseReplyResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseReplyResponse ret) {
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    mDescription = ret.getData().getDescription();
                    getCommentRecords();
                } else {
                    mPublicLoadLayout.hiddenOtherErrorView();
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenOtherErrorView();
                mPublicLoadLayout.showOtherErrorView(error.getMessage());

            }
        });


    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetCourseReplyRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseReplyRequestUUID);
        }
        if (mGetCourseCommentRecordsRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseCommentRecordsRequestUUID);
        }
    }
}
